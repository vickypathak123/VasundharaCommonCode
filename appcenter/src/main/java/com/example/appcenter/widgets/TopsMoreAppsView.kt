package com.example.appcenter.widgets

import android.content.Context
import android.os.AsyncTask
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.appcenter.R
import com.example.appcenter.adapter.MoreAppsAdapter
import com.example.appcenter.jsonparsing.GetJsonResponseTask
import com.example.appcenter.jsonparsing.JsonParserCallback
import com.example.appcenter.retrofit.APIService
import com.example.appcenter.retrofit.model.ModelAppCenter
import com.example.appcenter.utilities.*
import kotlinx.android.synthetic.main.layout_no_internet.view.*
import kotlinx.android.synthetic.main.layout_progress.view.*
import kotlinx.android.synthetic.main.layout_tops_more_apps.view.*
import kotlinx.android.synthetic.main.layout_went_wrong.view.*
import kotlinx.coroutines.*
import retrofit2.Response
import kotlin.math.roundToInt

private val TAG = TopsMoreAppsView::class.java.simpleName

class TopsMoreAppsView(private val mContext: Context, attrs: AttributeSet) : ConstraintLayout(mContext, attrs), View.OnClickListener {

    private lateinit var job: Job

    init {
        initViews()
        initData()
        initActions()
    }

    private fun initViews() {
        inflate(context, R.layout.layout_tops_more_apps, this)
        val margin = mContext.getGridSpacing()
        //  tv_more_apps.typeface = Typeface.createFromAsset(mContext.assets, "app_fonts/Arial-Bold.ttf")
        ma_rv_more_apps.addItemDecoration(GridSpacingItemDecoration(4, margin, true))
    }

    fun Context.getGridSpacing(): Int {
        val margin = resources.getDimension(R.dimen._3sdp)
        return dpToPx(margin.toInt())
    }

    /**
     *  Get Pixel from dp
     *
     * @return The Pixel
     */
    fun dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics).roundToInt()
    }

    private fun initData() {
        job = Job()
        layout_cl_no_internet.visibility = View.GONE
        layout_went_wrong.visibility = View.GONE
        ma_rv_more_apps.visibility = View.GONE
        layout_progrssbar.visibility = View.VISIBLE

        if (mContext.isOnline()) {
            // Fetch App center data from the server
            if (isSDKBelow21()) {
                // Simple json parsing
                val url = mContext.getBaseUrlApps() + PKG_NAME
                GetJsonResponseTask(object : JsonParserCallback {
                    override fun onSuccess(response: String) {
                        context.saveAppCenter(response)
                        onSuccess(context.getAppCenter()!!)
                    }

                    override fun onFailure(message: String) {
                        tv_went_wrong.text = message
                        Log.e(TAG, message)
                        errorOnFetchData()
                    }

                }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url)

            } else {
                // Using retrofit with kotlin coroutine
                GlobalScope.launch(job + Dispatchers.Main) {
                    fetchDataFromServer(job)
                }
            }

        } else if (context.getAppCenter() != null) {
            onSuccess(context.getAppCenter()!!)
        } else {
            errorNoInternet()
        }
    }


    private fun initActions() {
        tv_no_internet_retry.setOnClickListener(this)
        tv_went_wrong_retry.setOnClickListener(this)

    }


    override fun onClick(v: View?) {

        when (v) {
            tv_no_internet_retry, tv_went_wrong_retry -> {
                if (mContext.isOnline()) {
                    initData()
                } else {
                    Toast.short(mContext, mContext.getString(R.string.label_check_internet))
                }

            }
        }

    }


    private suspend fun fetchDataFromServer(job: Job) {
        return withContext(job + Dispatchers.IO) {
            val retroApiInterface = APIService().getClientForMoreAppsView(mContext)
            try {
                val reqInvitations = retroApiInterface.getAppCenterAsync(PKG_NAME)
                onFetchResponse(reqInvitations.await())
            } catch (exception: Exception) {
                Log.e(TAG, exception.toString())
                errorOnFetchData()
            }
        }
    }

    private fun onFetchResponse(response: Response<ModelAppCenter>) {

        if (response.isSuccessful && response.body() != null) {
            Log.i(TAG, response.body()!!.message)
            onSuccess(response.body()!!)
        } else {
            Log.e(TAG, response.errorBody().toString())
            // errorOnFetchData()
        }
    }

    private fun errorOnFetchData() {
        GlobalScope.launch(job + Dispatchers.Main) {
            layout_cl_no_internet.visibility = View.GONE
            layout_went_wrong.visibility = View.VISIBLE
            ma_rv_more_apps.visibility = View.GONE
            layout_progrssbar.visibility = View.GONE
            cl_container.visibility = View.GONE
        }
    }

    private fun errorNoInternet() {


        Log.i(TAG, mContext.getString(R.string.label_offline))
        GlobalScope.launch(job + Dispatchers.Main) {
            layout_cl_no_internet.visibility = View.VISIBLE
            layout_went_wrong.visibility = View.GONE
            ma_rv_more_apps.visibility = View.GONE
            layout_progrssbar.visibility = View.GONE

            cl_container.visibility = View.GONE
        }
    }

    private fun onSuccess(modelAppCenter: ModelAppCenter) {
        Log.i(TAG, mContext.getString(R.string.label_success))
        GlobalScope.launch(job + Dispatchers.Main) {
            layout_cl_no_internet.visibility = View.GONE
            layout_went_wrong.visibility = View.GONE
            ma_rv_more_apps.visibility = View.VISIBLE
            cl_container.visibility = View.VISIBLE
            layout_progrssbar.visibility = View.GONE
            val homeApps = modelAppCenter.data.toMutableList()
            val adapter = MoreAppsAdapter(mContext, homeApps)
            ma_rv_more_apps.adapter = adapter
        }
    }

}
