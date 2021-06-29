package com.vasu.appcenter.rateandfeedback.library_feedback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import androidx.core.content.FileProvider
import com.example.jdrodi.utilities.OnPositive
import com.example.jdrodi.utilities.showAlert
import com.vasu.appcenter.BuildConfig
import com.vasu.appcenter.R
import com.vasu.appcenter.utilities.fontPath
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*

/*
   * ToDo.. Confirmation Dialog
   */
fun Context.feedbackDialog() {
    val title = getAppLabel(false)
    val message = resources.getString(R.string.feedback_message)
    val positiveBtn = resources.getString(R.string.dialog_ok)
    val negativeBtn = resources.getString(R.string.dialog_cancel)
    showAlert(title, message, positiveBtn, negativeBtn, fontPath, object : OnPositive {
        override fun onYes() {
            sendEmail()
        }
    })
}

private fun Context.sendEmail() {
    val emailId = resources.getString(R.string.email_id)
    val emailIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
    emailIntent.type = "text/plain"
    emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailId))
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, getAppLabel(true))
    val mailBody = "..."
    val uris = ArrayList<Uri>()
    val deviceInfoUri = createFileFromString(getAllDeviceInfo(), "deviceInf.txt")
    uris.add(deviceInfoUri)
    val logUri = createFileFromString(extractLogToString(), "deviceLog.txt")
    uris.add(logUri)
    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    emailIntent.putExtra(Intent.EXTRA_TEXT, mailBody)
    startActivity(createEmailOnlyChooserIntent(emailIntent))
}

private fun Context.createFileFromString(text: String, name: String): Uri {
    val file = File(cacheDir, name)
    //create the file if it didn't exist
    if (!file.exists()) {
        try {
            file.createNewFile()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    try {
        //BufferedWriter for performance, false to overrite to file flag
        val buf = BufferedWriter(FileWriter(file, false))
        buf.write(text)
        buf.close()
    } catch (e: IOException) {
        // TODO Auto-generated catch block
        e.printStackTrace()
    }
    return FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
}

private fun Context.getAppLabel(isAppVersionRequired: Boolean): String {
    var subject = "Feedback of " + resources.getString(R.string.app_name)
    if (isAppVersionRequired) {
        subject = subject + " " + BuildConfig.VERSION_NAME
    }
    return subject
}

private fun Context.createEmailOnlyChooserIntent(source: Intent): Intent {
    val chooserTitle = "Send Feedback"
    val intents = Stack<Intent>()
    val i = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "info@domain.com", null))
    val activities = packageManager.queryIntentActivities(i, 0)
    for (ri in activities) {
        val target = Intent(source)
        target.setPackage(ri.activityInfo.packageName)
        intents.add(target)
    }
    return if (!intents.isEmpty()) {
        val chooserIntent = Intent.createChooser(intents.removeAt(0), chooserTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray<Parcelable>())
        chooserIntent
    } else {
        Intent.createChooser(source, chooserTitle)
    }
}