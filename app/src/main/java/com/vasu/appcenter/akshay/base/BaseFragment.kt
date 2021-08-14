@file:Suppress("unused")

package com.vasu.appcenter.akshay.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.LayoutRes
import androidx.annotation.UiThread
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.appcenter.utilities.isOnline
import com.vasu.appcenter.adshelper.AdsManager

/**
 * @author Akshay Harsoda
 * @since 18 Mar 2021
 *
 * BaseFragment.kt - A simple class contains some modifications to the native Fragment.
 * This Class use with or without ViewBinding property.
 * also you have use this class in JAVA or KOTLIN both language.
 *
 * use of this class
 * you have to extend your Activity using this class like.
 * in Java :- {class MainFragment extends BaseFragment}
 * in Kotlin :- {class MainFragment : BaseFragment()}
 */
abstract class BaseFragment : Fragment(), View.OnClickListener {

    // for log
    val TAG = javaClass.simpleName

    // your activity context object
    val mContext: FragmentActivity get() {
        return requireActivity()
    }

    private var mRequestCode: Int = 0

    private val launcher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            fromActivityResult(
                requestCode = mRequestCode,
                resultCode = result.resultCode,
                data = result.data
            )
        }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        getLayoutRes()?.let {
            return inflater.inflate(it, container, false)
        }

        return null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, null)

        initView()
        loadAds()
        initViewAction()
        initViewListener()
    }

    override fun onResume() {
        super.onResume()
        loadAds()
    }

    /**
     * This method for load your all type of ads
     */
    private fun loadAds() {
        if (mContext.isOnline && AdsManager(mContext).isNeedToShowAds()) {
            initAds()
        }
    }

    /**
     * This method for set your layout
     *
     * without using viewBinding @return Your Layout File Resource Id Lick This 'return R.layout.fragment_main'
     *
     * or if you use viewBinding then @return 'return null' for base reference
     */
    @UiThread
    @LayoutRes
    abstract fun getLayoutRes(): Int?

    /**
     * For Init Your Layout File's All View
     */
    @UiThread
    open fun initView() {
    }

    /**
     * For Init All Ads.
     */
    @UiThread
    open fun initAds() {
    }

    /**
     * For Init Your Default Action Performance On View
     */
    @UiThread
    open fun initViewAction() {
    }

    /**
     * For Set Your All Type Of Listeners
     */
    @UiThread
    open fun initViewListener() {
    }

    /**
     * For Set Your All View Click Listener,
     * now you no need to write multiple line code for 'View.setOnClickListener(this)'
     * just call this method and pass your all view like
     *
     * setClickListener(view1, view2)
     *
     * @param fViews list of your all passed view's.
     */
    @UiThread
    open fun setClickListener(vararg fViews: View) {
        for (lView in fViews) {
            lView.setOnClickListener(this)
        }
    }

    /**
     * This Method for get your next activity intent
     *
     * @param fNextActivityClass Your Next Activity
     * @param isAddFlag [Default value:- true] for set up your activity flag in your intent
     * @param fBundle lambda fun for pass data throw intent
     */
    open fun <T : Activity> getActivityIntent(
        fNextActivityClass: Class<out T>,
        isAddFlag: Boolean = true,
        fBundle: Bundle.() -> Unit = {},
    ): Intent {
        val lIntent = Intent(mContext, fNextActivityClass)

        if (isAddFlag) {
            lIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            lIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        lIntent.putExtras(Bundle().apply(fBundle))

        return lIntent
    }

    /**
     * This Method will replace your default method of [startActivityForResult]
     *
     * @param fIntent Your Launcher Screen Intent
     * @param fRequestCode Your Request Code For Get Result Of Your Next Activity
     * @param fEnterAnimId your activity Enter animation
     * @param fExitAnimId your activity Exit animation
     */
    open fun launchActivityForResult(
        fIntent: Intent,
        fRequestCode: Int,
        @AnimatorRes @AnimRes fEnterAnimId: Int = android.R.anim.fade_in,
        @AnimatorRes @AnimRes fExitAnimId: Int = android.R.anim.fade_out
    ) {
        mRequestCode = fRequestCode
        launcher.launch(fIntent, ActivityOptionsCompat.makeCustomAnimation(mContext, fEnterAnimId, fExitAnimId))
    }

    /**
     * This Method will replace your default method of [startActivity]
     *
     * @param fIntent Your Launcher Screen Intent
     * @param isNeedToFinish [Default value:- false] pass [isNeedToFinish = true] for finish your caller screen after call next screen
     * @param fEnterAnimId your activity Enter animation
     * @param fExitAnimId your activity Exit animation
     */
    open fun launchActivity(
        fIntent: Intent,
        isNeedToFinish: Boolean = false,
        @AnimatorRes @AnimRes fEnterAnimId: Int = android.R.anim.fade_in,
        @AnimatorRes @AnimRes fExitAnimId: Int = android.R.anim.fade_out
    ) {
        mContext.startActivity(fIntent)
        mContext.overridePendingTransition(fEnterAnimId, fExitAnimId)

        if (isNeedToFinish) {
            mContext.finish()
        }
    }

    /**
     * This Method will replace your default method of [onActivityResult]
     *
     * @param requestCode The integer request code originally supplied to launchActivityForResult(), allowing you to identify who this result came from.
     * @param resultCode The integer result code returned by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
     */
    @UiThread
    open fun fromActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }


}