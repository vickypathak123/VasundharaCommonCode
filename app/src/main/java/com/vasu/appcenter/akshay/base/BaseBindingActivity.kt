@file:Suppress("unused")

package com.vasu.appcenter.akshay.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.UiThread
import androidx.viewbinding.ViewBinding

/**
 * @author Akshay Harsoda
 * @since 18 Mar 2021
 *
 * BaseBindingActivity.kt - A simple class contains some modifications to the native Activity.
 * This Class use with ViewBinding property.
 * also you have use this class in JAVA or KOTLIN both language.
 *
 * use of this class
 * you have to extend your Activity using this class like.
 * in Java :- {class MainActivity extends BaseBindingActivity<ActivityMainBinding>}
 * in Kotlin :- {class MainActivity : BaseBindingActivity<ActivityMainBinding>()}
 *
 * @property VB this is your layout file binding object
 *
 * for more details {@see BaseActivity.kt}.
 */
abstract class BaseBindingActivity<VB : ViewBinding> : BaseActivity() {

    // your activity binding object
    lateinit var mBinding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        setParamBeforeLayoutInit()
        super.onCreate(null)
        setContentView(getInflatedLayout(layoutInflater))
    }

    override fun getLayoutRes(): Int? {
        return null
    }

    /**
     * internal functions for set layout using Binding object
     *
     * @param inflater your activity layoutInflater
     * @return this will return your binding view
     */
    @UiThread
    private fun getInflatedLayout(inflater: LayoutInflater): View {
        this.mBinding = setBinding(inflater)
        return this.mBinding.root
    }

    /**
     * For init your binding object
     *
     * @param layoutInflater your activity layoutInflater
     * @return Binding property
     * in Java :- {return ActivityMainBinding.inflate(layoutInflater);}
     * in Kotlin :- {return ActivityMainBinding.inflate(layoutInflater)}
     */
    @UiThread
    abstract fun setBinding(layoutInflater: LayoutInflater): VB
}