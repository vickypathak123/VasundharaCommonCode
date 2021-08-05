package com.vasu.appcenter

import android.os.Bundle
import com.example.jdrodi.BaseActivity
import com.example.jdrodi.utilities.hideKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : BaseActivity(), CoroutineScope {




    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}