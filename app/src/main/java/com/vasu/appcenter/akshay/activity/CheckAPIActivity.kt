package com.vasu.appcenter.akshay.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.vasu.appcenter.BuildConfig
import com.vasu.appcenter.akshay.adshelper.gone
import com.vasu.appcenter.akshay.adshelper.visible
import com.vasu.appcenter.akshay.api.APICallEnqueue.getForceUpdateStatus
import com.vasu.appcenter.akshay.api.APIResponseListener
import com.vasu.appcenter.akshay.base.BaseBindingActivity
import com.vasu.appcenter.databinding.ActivityCheckApiactivityBinding
import com.vasu.appcenter.retrofit.model.ForceUpdateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckAPIActivity : BaseBindingActivity<ActivityCheckApiactivityBinding>() {

    override fun setBinding(layoutInflater: LayoutInflater): ActivityCheckApiactivityBinding {
        return ActivityCheckApiactivityBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): AppCompatActivity {
        return this@CheckAPIActivity
    }

    override fun initViewListener() {
        super.initViewListener()
        setClickListener(mBinding.btnCallUpdateApi)
    }

    override fun onClick(v: View) {

        if (mBinding.clProgress.visibility == View.VISIBLE) {
            return
        }

        when (v) {
            mBinding.btnCallUpdateApi -> {

                mBinding.clProgress.visible

                GlobalScope.launch(mJob + Dispatchers.Main) {

                    mActivity.getForceUpdateStatus(
                        fJob = mJob,
//                        fPackageName = BuildConfig.APPLICATION_ID,
                        fPackageName = "com.hindi.english.translate.language.word.dictionary",
                        fVersionCode = BuildConfig.VERSION_NAME.toDouble(),
                        fListener = object : APIResponseListener<ForceUpdateModel> {
                            override fun onError(fErrorMessage: String?) {

                            }

                            override fun onSuccess(fResponse: ForceUpdateModel) {
                                Log.e(TAG, "onSuccess: fResponse -> $fResponse")
                                mBinding.clProgress.gone
                            }
                        }
                    )
                }
            }
        }
    }
}