package com.vasu.appcenter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vasu.appcenter.akshay.adshelper.AdMobAdsListener
import com.vasu.appcenter.openad.isInternalCall
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {

    val ENGLISH = "en"
    val HINDI = "hi"
    val MARATHI = "mr"
    val GUJARATI = "gu"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    override fun getContext(): Activity {
        return this@TestActivity
    }

    override fun initActions() {
        btn_speak.setOnClickListener {
            checkAudioPermissions()
        }

    }

    override fun initData() {

    }


    // Record audio
    private fun checkAudioPermissions() {
        Dexter.withContext(mContext).withPermissions(*audioPermission).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                when {
                    report!!.areAllPermissionsGranted() -> {
                        startVoiceRecognitionActivity()
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?, token: PermissionToken?) {
                token!!.continuePermissionRequest()
            }
        }).check()

    }

    fun Activity.startVoiceRecognitionActivity() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, false)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "$GUJARATI-IN")
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-IN")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak number")
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE)
        isInternalCall = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (requestCode) {

            VOICE_RECOGNITION_REQUEST_CODE -> {
                val matches = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                if (matches != null && matches.isNotEmpty()) {
                    val number = matches[0].filter { it.isLetterOrDigit() }
                    Log.i("TAG_1", number)
                    et_text.setText(number)
                }
            }

        }

    }


}