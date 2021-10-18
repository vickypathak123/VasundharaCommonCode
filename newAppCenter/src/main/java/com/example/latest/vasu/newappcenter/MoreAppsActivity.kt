package com.example.latest.vasu.newappcenter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.latest.vasu.newappcenter.adapter.ViewPagerAdapter
import com.example.latest.vasu.newappcenter.base.BaseBindingActivity
import com.example.latest.vasu.newappcenter.databinding.ActivityMoreAppsBinding
import com.example.latest.vasu.newappcenter.fragments.HomeFragment
import com.example.latest.vasu.newappcenter.fragments.MoreAppFragment
import com.example.latest.vasu.newappcenter.model.MoreAppMainModel
import com.example.latest.vasu.newappcenter.newAPI.APICallEnqueue.getMoreAppResponse
import com.example.latest.vasu.newappcenter.newAPI.APIResponseListener
import com.example.latest.vasu.newappcenter.utils.*
import com.example.latest.vasu.newappcenter.widgets.BackPressedDialog
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*

class MoreAppsActivity : BaseBindingActivity<ActivityMoreAppsBinding>() {

    private var mBackPressedDialog: BackPressedDialog? = null
    private var mIsHomeEnable: Boolean = false

    override fun setBinding(layoutInflater: LayoutInflater): ActivityMoreAppsBinding {
        return ActivityMoreAppsBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): AppCompatActivity {
        return this@MoreAppsActivity
    }

    override fun onResume() {
        super.onResume()
        mBinding.maIvShare.isEnabled = true
    }

    override fun setParamBeforeLayoutInit() {
        super.setParamBeforeLayoutInit()
        changeStatusBarColor()
    }

    override fun initView() {
        super.initView()

        //<editor-fold desc="get Color From Intent">

        PKG_NAME = intent.getStringExtra(ARG_APP_PACKAGE_NAME) ?: PKG_NAME

        Log.e(TAG, "initView: PKG_NAME::$PKG_NAME")


        themeColor = try {
            Color.parseColor(intent.getStringExtra(ARG_THEME_COLOR))
        } catch (ignored: java.lang.Exception) {
            val error = ignored.toString()
            Log.e(TAG, "initView: ThemeColor: $error")
            ContextCompat.getColor(mActivity, R.color.colorPrimary)

        }

        textColor = try {
            Color.parseColor(intent.getStringExtra(ARG_THEME_TEXT_COLOR))
        } catch (ignored: java.lang.Exception) {
            val error = ignored.toString()
            Log.e(TAG, "initView: TextColor: $error")
            ContextCompat.getColor(mActivity, android.R.color.white)
        }
        //</editor-fold>

        themeColor?.let {
            mBinding.maAblHeader.setBackgroundColor(it)
            mBinding.maTabs.setBackgroundColor(it)

            mBinding.layoutProgress.layoutProgressbar.indeterminateDrawable.colorFilter =
                PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)

            mBinding.layoutNoInternet.tvNoInternetRetry.background =
                mActivity.shapeCategorySelectedDrawable
            mBinding.layoutWentWrong.tvWentWrongRetry.background =
                mActivity.shapeCategorySelectedDrawable
        }
    }

    override fun initViewAction() {
        super.initViewAction()

        mBinding.layoutNoInternet.layoutClNoInternet.gone
        mBinding.layoutWentWrong.layoutWentWrong.gone
        mBinding.layoutProgress.layoutProgressbar.visible

        when {
            mActivity.isOnline -> {

                MainScope().launch(coroutineContext) {

                    mActivity.getMoreAppResponse(
                        fJob = mJob,
                        fListener = object : APIResponseListener<MoreAppMainModel> {

                            override fun onSuccess(fResponse: MoreAppMainModel) {
                                Log.e(TAG, "onSuccess: response::$fResponse")
                                successOnFetchData(fResponse = fResponse)
                            }

                            override fun onError(fErrorMessage: String?) {
                                Log.e(TAG, "onError: ${fErrorMessage!!}")
                                errorOnFetchData()
                            }
                        }
                    )
                }

            }
            getAppCenter() != null -> {
                successOnFetchData(getAppCenter()!!)
            }
            else -> {
                errorNoInternet()
            }
        }
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.maIvBack,
            mBinding.maIvShare,
            mBinding.layoutNoInternet.tvNoInternetRetry,
            mBinding.layoutWentWrong.tvWentWrongRetry
        )
    }

    override fun onClick(v: View) {
        if (v != mBinding.maIvBack) {
            super.onClick(v)
        }

        when (v) {
            mBinding.maIvBack -> {
                finish()
            }
            mBinding.maIvShare -> {
                shareApp()
            }
            mBinding.layoutNoInternet.tvNoInternetRetry, mBinding.layoutWentWrong.tvWentWrongRetry -> {
                if (mActivity.isOnline) {
                    initViewAction()
                } else {
                    Toast.short(mActivity, getStringRes(R.string.label_check_internet))
                }
            }

        }
    }

    private fun shareApp() {
        intent.getStringExtra(ARG_SHARE_MSG)?.let {
            mBinding.maIvShare.isEnabled = false
            shareApp(it)
        }
    }

    //<editor-fold desc="Set API Data">
    private fun successOnFetchData(fResponse: MoreAppMainModel) {
        saveAppCenterModel(fResponse)

        MainScope().launch(coroutineContext) {
            Log.e(TAG, getStringRes(R.string.label_success))
            mBinding.layoutNoInternet.layoutClNoInternet.gone
            mBinding.layoutWentWrong.layoutWentWrong.gone
            mBinding.layoutProgress.layoutProgressbar.gone
            mBinding.tvNoPackage.gone

            if (fResponse.message == getStringRes(R.string.pkg_not_exist)) {
                mBinding.tvNoPackage.visible
            } else {

                mBackPressedDialog = BackPressedDialog(
                    activity = mActivity,
                    data = fResponse.data,
                    onShareClick = { shareApp() },
                    onExitClick = { finish() }
                )

                setupViewPager(fResponse = fResponse)
                mBinding.maTabs.setupWithViewPager(mBinding.maViewpager)
            }
        }
    }

    private fun errorOnFetchData() {
        MainScope().launch(coroutineContext) {
            mBinding.layoutNoInternet.layoutClNoInternet.gone
            mBinding.layoutWentWrong.layoutWentWrong.visible
            mBinding.layoutProgress.layoutProgressbar.gone
        }
    }

    private fun errorNoInternet() {
        Log.i(TAG, getStringRes(R.string.label_offline))
        MainScope().launch(coroutineContext) {
            mBinding.layoutNoInternet.layoutClNoInternet.visible
            mBinding.layoutWentWrong.layoutWentWrong.gone
            mBinding.layoutProgress.layoutProgressbar.gone
        }
    }
    //</editor-fold>

    //<editor-fold desc="Set Main UI">
    private fun setupViewPager(fResponse: MoreAppMainModel) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        with(fResponse) {
            Log.i(TAG, "setupViewPager: isHomeEnable::$isHomeEnable")
            mIsHomeEnable = isHomeEnable
            if (isHomeEnable) {
                adapter.addFragment(HomeFragment.newInstance(home), "HOME")
            }

            if (appCenter.isNotEmpty()) {
                for (moreAppCenter in appCenter) {
                    adapter.addFragment(
                        MoreAppFragment.newInstance(moreAppCenter.subCategory),
                        moreAppCenter.name
                    )
                }
            }

            mBinding.maViewpager.adapter = adapter
            mBinding.maViewpager.offscreenPageLimit = (appCenter.size + 1)
        }
        mBinding.maTabs.gone
        if (adapter.count > 1) {
            mBinding.maTabs.visible
        }

        mBinding.maTabs.tabMode = if (adapter.count > 2) {
            TabLayout.MODE_SCROLLABLE
        } else {
            TabLayout.MODE_FIXED
        }
    }

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = try {
                Color.parseColor(intent.getStringExtra(ARG_THEME_COLOR))
            } catch (ignored: java.lang.Exception) {
                val error = ignored.toString()
                Log.e(TAG, "changeStatusBarColor: ThemeColor: $error")
                ContextCompat.getColor(mActivity, R.color.colorPrimary)

            }
        }
    }
    //</editor-fold>

    override fun onBackPressed() {
//        if (mIsHomeEnable) {
//        mBackPressedDialog?.showBackPressedDialog()
//        } else {
            finish()
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
        Glide.with(applicationContext).pauseRequests()
    }
}