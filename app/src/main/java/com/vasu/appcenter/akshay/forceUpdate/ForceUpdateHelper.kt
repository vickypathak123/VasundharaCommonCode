package com.vasu.appcenter.akshay.forceUpdate

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.appcenter.utilities.isOnline
import com.vasu.appcenter.BuildConfig
import com.vasu.appcenter.R
import com.vasu.appcenter.akshay.adshelper.gone
import com.vasu.appcenter.akshay.api.APICallEnqueue.getForceUpdateStatus
import com.vasu.appcenter.akshay.api.APIResponseListener
import com.vasu.appcenter.retrofit.model.ForceUpdateModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object ForceUpdateHelper {

    private val TAG = javaClass.simpleName

    fun FragmentActivity.checkForceUpdateStatus(@NonNull fJob: Job, @NonNull onUpdateNeeded: (shouldUpdate: Boolean) -> Unit) {

        if (isOnline) {

            GlobalScope.launch(fJob + Dispatchers.Main) {
                this@checkForceUpdateStatus.getForceUpdateStatus(
                    fJob = fJob,
//                        fPackageName = BuildConfig.APPLICATION_ID,
                    fPackageName = "com.hindi.english.translate.language.word.dictionary",
                    fVersionCode = BuildConfig.VERSION_NAME.toDouble(),
                    fListener = object : APIResponseListener<ForceUpdateModel> {
                        override fun onError(fErrorMessage: String?) {
                            onUpdateNeeded.invoke(false)
                        }

                        override fun onSuccess(fResponse: ForceUpdateModel) {
                            Log.e(TAG, "onSuccess: fResponse -> $fResponse")

                            if (fResponse.is_need_to_update) {
                                this@checkForceUpdateStatus.runOnUiThread {
                                    this@checkForceUpdateStatus.showAlert(
                                        this@checkForceUpdateStatus.getString(R.string.update_required),
                                        this@checkForceUpdateStatus.getString(R.string.update_message),
                                        this@checkForceUpdateStatus.getString(R.string.update_positive),
                                        this@checkForceUpdateStatus.getString(R.string.update_negative),
                                        null,
                                        onYes = {
                                            try {
                                                this@checkForceUpdateStatus.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this@checkForceUpdateStatus.packageName)))
                                            } catch (anfe: ActivityNotFoundException) {
                                                this@checkForceUpdateStatus.startActivity(
                                                    Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse("https://play.google.com/store/apps/details?id=" + this@checkForceUpdateStatus.packageName)
                                                    )
                                                )
                                            }
                                            this@checkForceUpdateStatus.finish()
                                        },
                                        onNo = {
                                            this@checkForceUpdateStatus.finishAffinity()
                                        }
                                    )
                                }
                                onUpdateNeeded.invoke(true)
                            } else {
                                onUpdateNeeded.invoke(false)
                            }
                        }
                    }
                )
            }
        } else {
            onUpdateNeeded.invoke(false)
        }
    }

    private fun Context.showAlert(
        title: String? = null,
        msg: String? = null,
        positiveText: String? = null,
        negativeText: String? = null,
        fontPath: String? = null,
        onYes: () -> Unit = {},
        onNo: () -> Unit = {}
    ) {

        val dialog = AlertDialog.Builder(this)
        var alert: AlertDialog? = null

        dialog.setCancelable(false)
        if (title != null) {
            // Initialize a new foreground color span instance
            val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            val ssBuilder = SpannableStringBuilder(title)
            ssBuilder.setSpan(foregroundColorSpan, 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            dialog.setTitle(ssBuilder)
        }
        if (msg != null) {
            dialog.setMessage(msg)
        }
        if (positiveText != null) {
            dialog.setPositiveButton(positiveText) { _, _ ->
                alert?.dismiss()
                onYes.invoke()
            }
        }
        if (negativeText != null) {
            dialog.setNegativeButton(negativeText) { _, _ ->
                alert?.dismiss()
                onNo.invoke()
            }
        }

        alert = dialog.create()
        alert.show()


        if (fontPath != null) {
            val textView = alert.findViewById<TextView>(android.R.id.message)
            try {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                val face = Typeface.createFromAsset(assets, fontPath)
                textView.typeface = face
            } catch (e: Exception) {
                Log.e("showAlert", e.toString())
            }
        }
    }

}