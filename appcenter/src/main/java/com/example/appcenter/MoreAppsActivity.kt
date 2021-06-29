package com.example.appcenter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.bumptech.glide.Glide
import com.example.appcenter.adapter.BackAppsAdapter
import com.example.appcenter.adapter.ViewPagerAdapter
import com.example.appcenter.fragments.HomeFragment
import com.example.appcenter.fragments.MoreAppFragment
import com.example.appcenter.jsonparsing.GetJsonResponseTask
import com.example.appcenter.jsonparsing.JsonParserCallback
import com.example.appcenter.retrofit.APIService
import com.example.appcenter.retrofit.getBodyErrorMessage
import com.example.appcenter.retrofit.model.ModelAppCenter
import com.example.appcenter.utilities.*
import com.example.jdrodi.utilities.getDisplayHeight
import com.example.jdrodi.utilities.getDisplayWidth
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_more_apps.*
import kotlinx.android.synthetic.main.layout_backpressed.view.*
import kotlinx.android.synthetic.main.layout_more_apps.view.*
import kotlinx.android.synthetic.main.layout_no_internet.*
import kotlinx.android.synthetic.main.layout_no_internet.view.*
import kotlinx.android.synthetic.main.layout_progress.*
import kotlinx.android.synthetic.main.layout_went_wrong.*
import kotlinx.android.synthetic.main.layout_went_wrong.view.*
import kotlinx.coroutines.*
import retrofit2.Response


private val TAG = MoreAppsActivity::class.java.simpleName
const val ARG_THEME_COLOR = "theme_color"
const val ARG_THEME_TEXT_COLOR = "text_color"
const val ARG_SHARE_MSG = "share_msg"

var themeColor: Int? = null
var textColor: Int? = null


class MoreAppsActivity : BaseActivity() {


    private lateinit var job: Job
    private var modelAppCenter: ModelAppCenter? = null
    private var shareAppMsg: String? = null


    companion object {
        fun launchIntent(mContext: Context, shareMsg: String, themeColor: Int, textColor: Int): Intent {

            val themeColorConvert = "#" + Integer.toHexString(themeColor).substring(2)
            val textColorConvert = "#" + Integer.toHexString(textColor).substring(2)
            val unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.shape_category_selected)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, themeColor)

            return Intent(mContext, MoreAppsActivity::class.java)
                .putExtra(ARG_THEME_COLOR, themeColorConvert)
                .putExtra(ARG_THEME_TEXT_COLOR, textColorConvert)
                .putExtra(ARG_SHARE_MSG, shareMsg)


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_apps)
        changeStatusBarColor()

    }

    override fun getContext(): Activity {
        return this@MoreAppsActivity
    }

    override fun initViews() {


    }

    override fun initData() {
        themeColor = try {
            Color.parseColor(intent.getStringExtra(ARG_THEME_COLOR))
        } catch (ignored: java.lang.Exception) {
            val error = ignored.toString()
            Log.e(TAG, "ThemeColor: $error")
            ContextCompat.getColor(mContext, R.color.colorPrimary)

        }


        val unwrappedDrawable: Drawable? = AppCompatResources.getDrawable(mContext, R.drawable.shape_category_selected)
        if (unwrappedDrawable != null) {
            val wrappedDrawable: Drawable = DrawableCompat.wrap(unwrappedDrawable)
            DrawableCompat.setTint(wrappedDrawable, themeColor!!)
        }

        val unwrappedDownloadDrawable: Drawable? = AppCompatResources.getDrawable(mContext, R.drawable.shape_download)
        if (unwrappedDownloadDrawable != null) {
            val wrappedDownloadDrawable: Drawable = DrawableCompat.wrap(unwrappedDownloadDrawable)
            DrawableCompat.setTint(wrappedDownloadDrawable, themeColor!!)
        }


        textColor = try {
            Color.parseColor(intent.getStringExtra(ARG_THEME_TEXT_COLOR))
        } catch (ignored: java.lang.Exception) {
            val error = ignored.toString()
            Log.e(TAG, "TextColor: $error")
            ContextCompat.getColor(mContext, android.R.color.white)
        }

        job = Job()
        layout_cl_no_internet.visibility = View.GONE
        layout_went_wrong.visibility = View.GONE
        layout_progrssbar.visibility = View.VISIBLE
        if (mContext.isOnline()) {
            // Fetch App center data from the server
            if (isSDKBelow21()) {
                // Simple json parsing
                val url = mContext.getBaseUrlApps() + PKG_NAME
                GetJsonResponseTask(object : JsonParserCallback {
                    override fun onSuccess(response: String) {
                        saveAppCenter(response)
                        onSuccess(getAppCenter()!!)
                    }

                    override fun onFailure(message: String) {
                        Log.e(TAG, message)
                     //   tv_went_wrong.text = message
                        errorOnFetchData(message)
                    }

                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)

            } else {
                // Using retrofit with kotlin coroutine
                GlobalScope.launch(job + Dispatchers.Main) {
                    fetchDataFromServer(job)
                }
            }

        } else if (getAppCenter() != null) {
            onSuccess(getAppCenter()!!)
        } else {
            errorNoInternet()
        }

        themeColor?.let {
            val unwrappedDrawable = AppCompatResources.getDrawable(mContext, R.drawable.shape_category_selected)
            val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, it)
            ma_abl_header.setBackgroundColor(it)

            val colorFilter = PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)
            layout_progrssbar.indeterminateDrawable.colorFilter = colorFilter

            tv_no_internet_retry.background = wrappedDrawable
            tv_went_wrong_retry.background = wrappedDrawable
        }

    }

    override fun initActions() {
        ma_iv_back.setOnClickListener { finish() }
        ma_iv_share.setOnClickListener(this)
        tv_no_internet_retry.setOnClickListener(this)
        tv_went_wrong_retry.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        super.onClick(view)
        when (view) {
            tv_no_internet_retry, tv_went_wrong_retry -> {
                if (mContext.isOnline()) {
                    initData()
                } else {
                    Toast.short(mContext, mContext.getString(R.string.label_check_internet))
                }

            }

            ma_iv_share -> {
                shareAppMsg = intent.getStringExtra(ARG_SHARE_MSG)
                shareAppMsg?.let {
                    shareApp(shareAppMsg)
                }
            }

        }
    }


    private suspend fun fetchDataFromServer(job: Job) {
        return withContext(job + Dispatchers.IO) {
            val retroApiInterface = APIService().getClient(mContext)
            try {
                val reqInvitations = retroApiInterface.getAppCenterAsync(PKG_NAME)
                onFetchResponse(reqInvitations.await())
            } catch (exception: Exception) {
                val errorMsg = exception.toString()
                Log.e(TAG, errorMsg)
               // tv_went_wrong.text = errorMsg
                errorOnFetchData(errorMsg)
            }
        }
    }

    private fun onFetchResponse(response: Response<ModelAppCenter>) {

        if (response.isSuccessful && response.body() != null) {
            Log.i(TAG, response.body()!!.message)
            onSuccess(response.body()!!)
        } else {
            val errorMsg = getBodyErrorMessage(response as Response<Any>)
            Log.e(TAG, errorMsg)
          //  tv_went_wrong.text = errorMsg

            errorOnFetchData(errorMsg)
        }
    }

    private fun errorOnFetchData(error: String) {
        GlobalScope.launch(job + Dispatchers.Main) {
            layout_cl_no_internet.visibility = View.GONE
            layout_went_wrong.visibility = View.VISIBLE
            layout_progrssbar.visibility = View.GONE
          //  tv_went_wrong.text = error
        }
    }

    private fun errorNoInternet() {
        Log.i(TAG, getString(R.string.label_offline))
        GlobalScope.launch(job + Dispatchers.Main) {
            layout_cl_no_internet.visibility = View.VISIBLE
            layout_went_wrong.visibility = View.GONE
            layout_progrssbar.visibility = View.GONE
        }
    }

    private fun onSuccess(modelAppCenter: ModelAppCenter) {
        saveAppCenterModel(modelAppCenter)
        this.modelAppCenter = modelAppCenter
        GlobalScope.launch(job + Dispatchers.Main) {
            Log.i(TAG, getString(R.string.label_success))

            layout_progrssbar.visibility = View.GONE

            if (modelAppCenter.message == getString(R.string.pkg_not_exist)) {
                tv_no_package.visibility = View.VISIBLE
            } else {

                tv_no_package.visibility = View.GONE

                layout_cl_no_internet.visibility = View.GONE
                layout_went_wrong.visibility = View.GONE
                setupViewPager()
                ma_tabs.setupWithViewPager(ma_viewpager)
            }

        }
    }


    override fun onBackPressed() {
        // No need to show exit dialog now
        /*   if (modelAppCenter != null && modelAppCenter!!.is_home_enable) {
               recommendedApps()
               return
           }*/
        super.onBackPressed()
    }

    private fun recommendedApps() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.layout_backpressed, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val dialog: AlertDialog = mBuilder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val homeApps = modelAppCenter!!.data.toMutableList()
        homeApps.reverse()
        val adapter = BackAppsAdapter(mContext, homeApps)
        mDialogView.back_rv_more_apps.adapter = adapter
        mDialogView.tv_exit.setOnClickListener {
            modelAppCenter = null
            onBackPressed()
        }
        mDialogView.tv_cancel.setOnClickListener {
            dialog.dismiss()
        }

        mDialogView.iv_share.setOnClickListener {
            dialog.dismiss()
            shareAppMsg = intent.getStringExtra(ARG_SHARE_MSG)
            shareAppMsg?.let {
                shareApp(it)
            }
        }
        dialog.window!!.setLayout(
            (getDisplayWidth() * .90).toInt(),
            (getDisplayHeight() * .80).toInt()
        )

        var corner = 20f
        if (isSDKBelow21()) {
            corner = 7f
        }
        mDialogView.iv_back_top.setCornerRadius(corner, corner, 0f, 0f)
        mDialogView.iv_back_cancel.setCornerRadius(0f, 0f, 0f, corner)
        mDialogView.iv_back_exit.setCornerRadius(0f, 0f, corner, 0f)

        // setup theme color
        themeColor?.let {
            mDialogView.iv_back_top.setColorFilter(it, PorterDuff.Mode.SRC_IN)
            mDialogView.iv_back_cancel.setColorFilter(it, PorterDuff.Mode.SRC_IN)
            mDialogView.iv_back_exit.setColorFilter(it, PorterDuff.Mode.SRC_IN)
        }

    }


    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        if (modelAppCenter!!.is_home_enable) {
            Log.i("HOMEEE", "true")
            adapter.addFragment(HomeFragment.newInstance(modelAppCenter!!.home), "HOME")
        } else {
            Log.i("HOMEEE", "false")
        }
      //  adapter.addFragment(HomeFragment.newInstance(modelAppCenter!!.home), "HOME")
        if (modelAppCenter!!.app_center.isNotEmpty()) {
            for (moreAppCenter in modelAppCenter!!.app_center) {
                adapter.addFragment(MoreAppFragment.newInstance(moreAppCenter.sub_category), moreAppCenter.name)
            }
        }

        ma_viewpager.adapter = adapter
        ma_viewpager.offscreenPageLimit = (modelAppCenter!!.app_center.size + 1)

        if (adapter.count > 1) {
            ma_tabs.visibility = View.VISIBLE
        } else {
            ma_tabs.visibility = View.GONE
        }

        if (adapter.count > 2) {
            ma_tabs.tabMode = TabLayout.MODE_SCROLLABLE
        } else {
            ma_tabs.tabMode = TabLayout.MODE_FIXED
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        // all children coroutines gets destroyed automatically
        job.cancel()
        Glide.with(applicationContext).pauseRequests()
    }


    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            themeColor?.let {
                window.statusBarColor = it
            }

        }
    }

}
