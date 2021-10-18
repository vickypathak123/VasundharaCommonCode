package com.example.latest.vasu.newappcenter.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.latest.vasu.newappcenter.R
import com.example.latest.vasu.newappcenter.adapter.MoreAppsAdapter
import com.example.latest.vasu.newappcenter.databinding.LayoutTopsMoreAppsBinding
import com.example.latest.vasu.newappcenter.model.MoreAppMainModel
import com.example.latest.vasu.newappcenter.newAPI.APICallEnqueue.getMoreAppResponse
import com.example.latest.vasu.newappcenter.newAPI.APIResponseListener
import com.example.latest.vasu.newappcenter.themeColor
import com.example.latest.vasu.newappcenter.utils.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class TopsMoreAppsView : ConstraintLayout, CoroutineScope, View.OnClickListener {

    private val TAG = javaClass.simpleName

    private var mBinding: LayoutTopsMoreAppsBinding =
        LayoutTopsMoreAppsBinding.inflate(LayoutInflater.from(context), this, true)

    private enum class TextGravity {
        CENTER, LEFT, RIGHT
    }

    private var mTextGravity: TextGravity = TextGravity.CENTER

    private var mJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    //<editor-fold desc="Constructor">
    constructor(context: Context) : super(context) {
        initMainView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initMainView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initMainView(context, attrs)
    }
    //</editor-fold>

    @SuppressLint("RtlHardcoded")
    private fun initMainView(context: Context, attrs: AttributeSet?) {

        val styledData = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TopsMoreAppsView,
            0, 0
        )

        try {
            PKG_NAME =
                styledData.getString(R.styleable.TopsMoreAppsView_app_package_name) ?: PKG_NAME
            Log.e(TAG, "initMainView: PKG_NAME::$PKG_NAME")

            themeColor = styledData.getColor(
                R.styleable.TopsMoreAppsView_theme_color,
                context.getColorRes(R.color.colorPrimary)
            )

            mTextGravity = TextGravity.values()[styledData.getInt(
                R.styleable.TopsMoreAppsView_text_gravity,
                0
            )]

        } finally {
            styledData.recycle()
        }

        invalidate()

        with(mBinding) {
            maRvMoreApps.addItemDecoration(
                GridSpacingItemDecoration(
                    4,
                    context.dpToPx(context.sdpToPx(R.dimen._3sdp)),
                    true
                )
            )

            themeColor?.let {
                layoutProgress.layoutProgressbar.indeterminateDrawable.colorFilter =
                    PorterDuffColorFilter(it, PorterDuff.Mode.SRC_IN)

                layoutNoInternet.tvNoInternetRetry.background =
                    context.shapeCategorySelectedDrawable

                layoutWentWrong.tvWentWrongRetry.background =
                    context.shapeCategorySelectedDrawable
            }

            tvMoreApps.gravity = when (mTextGravity) {
                TextGravity.CENTER -> {
                    Gravity.CENTER or Gravity.CENTER_VERTICAL
                }
                TextGravity.LEFT -> {
                    Gravity.LEFT or Gravity.CENTER_VERTICAL
                }
                TextGravity.RIGHT -> {
                    Gravity.RIGHT or Gravity.CENTER_VERTICAL
                }
            }
        }

        initViewListener()
        initViewAction()
    }

    private fun initViewAction() {

        mBinding.layoutNoInternet.layoutClNoInternet.gone
        mBinding.layoutWentWrong.layoutWentWrong.gone
        mBinding.maRvMoreApps.gone
        mBinding.layoutProgress.layoutProgressbar.visible

        when {
            context.isOnline -> {

                MainScope().launch(coroutineContext) {

                    context.getMoreAppResponse(
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
            context.getAppCenter() != null -> {
                successOnFetchData(fResponse = context.getAppCenter()!!)
            }
            else -> {
                errorNoInternet()
            }
        }
    }

    private fun initViewListener() {

        setClickListener(
            mBinding.layoutNoInternet.tvNoInternetRetry,
            mBinding.layoutWentWrong.tvWentWrongRetry
        )
    }

    private fun setClickListener(vararg fViews: View) {
        for (lView in fViews) {
            lView.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v) {
            mBinding.layoutNoInternet.tvNoInternetRetry, mBinding.layoutWentWrong.tvWentWrongRetry -> {
                if (context.isOnline) {
                    initViewAction()
                } else {
                    Toast.short(context, context.getStringRes(R.string.label_check_internet))
                }
            }
        }
    }

    //<editor-fold desc="Set API Data">
    private fun successOnFetchData(fResponse: MoreAppMainModel) {
        context.saveAppCenterModel(fResponse)

        MainScope().launch(coroutineContext) {
            Log.e(TAG, context.getStringRes(R.string.label_success))
            mBinding.layoutNoInternet.layoutClNoInternet.gone
            mBinding.layoutWentWrong.layoutWentWrong.gone
            mBinding.layoutProgress.layoutProgressbar.gone
            mBinding.tvNoPackage.gone
            mBinding.clContainer.visible
            mBinding.maRvMoreApps.gone

            if (fResponse.message == context.getStringRes(R.string.pkg_not_exist)) {
                mBinding.clContainer.gone
                mBinding.tvNoPackage.visible
            } else {
                mBinding.maRvMoreApps.visible
                mBinding.maRvMoreApps.adapter = MoreAppsAdapter(context, fResponse.data)
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
        Log.i(TAG, context.getStringRes(R.string.label_offline))
        MainScope().launch(coroutineContext) {
            mBinding.layoutNoInternet.layoutClNoInternet.visible
            mBinding.layoutWentWrong.layoutWentWrong.gone
            mBinding.layoutProgress.layoutProgressbar.gone
        }
    }
    //</editor-fold>
}