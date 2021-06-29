package com.vasu.appcenter.rateandfeedback

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.appcenter.jsonparsing.JsonParserCallback
import com.example.appcenter.utilities.*
import com.example.gallery.AGallery
import com.example.gallery.MimeType
import com.example.gallery.engine.impl.GlideEngine
import com.example.gallery.internal.entity.CaptureStrategy
import com.example.jdrodi.utilities.showPermissionsAlert
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.vasu.appcenter.BaseActivity
import com.vasu.appcenter.BuildConfig
import com.vasu.appcenter.R
import com.vasu.appcenter.adshelper.AdsManager
import com.vasu.appcenter.rateandfeedback.adapter.FeedbackAdapter
import com.vasu.appcenter.rateandfeedback.feedbackjsonparsing.GetFeedbackResponseTask
import com.vasu.appcenter.rateandfeedback.network.*
import com.vasu.appcenter.utilities.fontPath
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import kotlinx.android.synthetic.main.activity_feedback.*
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import retrofit2.Response
import java.io.*
import java.lang.reflect.Field

private val permission_gallery = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
private const val REQUEST_CODE_CHOOSE = 101
private const val KEY_SMILE = "key_smile"


class FeedbackActivity : BaseActivity() {
    val TAG = javaClass.simpleName
    private var imageArrayList: ArrayList<String> = ArrayList()
    private var mFeedbackAdapter: FeedbackAdapter? = null
    private var intentFilter: IntentFilter? = null

    var fieldName: String = ""
    var mSmileRate: Int = 1

    private var progressDialog: ProgressDialog? = null

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }


    companion object {
        fun newIntent(mContext: Context, rate: Int): Intent {
            val intent = Intent(mContext, FeedbackActivity::class.java)
            intent.putExtra(KEY_SMILE, rate)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

    }

    override fun getContext(): Activity {
        return this@FeedbackActivity
    }

    override fun initActions() {
        iv_add.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        edt_details.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tv_current_length.text = s.toString().length.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        edt_details.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (edt_details.hasFocus()) {
                    v!!.parent.requestDisallowInterceptTouchEvent(true)
                    when (event!!.action and MotionEvent.ACTION_MASK) {
                        MotionEvent.ACTION_SCROLL -> {
                            v.parent.requestDisallowInterceptTouchEvent(false)
                            return true
                        }
                    }
                }
                return false
            }

        })
    }


    override fun initData() {


        edt_details.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edt_details.isFocusableInTouchMode = true
                edt_details.isCursorVisible = false
                edt_details.error = null
            }
            false
        }

        edt_email.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                edt_email.isFocusableInTouchMode = true
                edt_email.isCursorVisible = false
                edt_email.error = null
            }
            false
        }
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage(getString(R.string.please_wait))
        progressDialog!!.setCancelable(false)
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.isIndeterminate = true
        progressDialog!!.progress = 0


        val fields: Array<Field> = VERSION_CODES::class.java.fields
        for (field in fields) {
            fieldName = field.name
        }
        mSmileRate = intent.getIntExtra(KEY_SMILE, 1)

        Log.e(TAG, "initData: version name $fieldName")
        Log.e(TAG, "initData: version code  " + BuildConfig.VERSION_NAME)
        Log.e(TAG, "initData: Smiley  $mSmileRate")

        intentFilter = IntentFilter("FeedbackActivity")

    }

    override fun onClick(view: View) {
        super.onClick(view)

        when (view) {
            iv_add -> {
                checkPermissions()
            }

            btn_submit -> {
                performSubmit()
            }

            iv_back -> {
                onBackPressed()
            }
        }

    }

    private fun checkPermissions() {

        Dexter.withContext(mContext)
            .withPermissions(*permission_gallery)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    when {
                        report.areAllPermissionsGranted() -> {
                            choosePicture()
                        }
                        report.isAnyPermissionPermanentlyDenied -> {
                            showPermissionsAlert(fontPath)
                        }
                        else -> {
                            toast("Required Permissions not granted")
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: List<PermissionRequest>, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()


    }

    private fun choosePicture() {
        val maxSelection = 4 - imageArrayList.size
        AGallery.from(mContext)
            .choose(MimeType.ofImage())
            .countable(true)
            .showSingleMediaType(true)
            .capture(false) // Camera option enable or disable
            .isNeedToShowAd(AdsManager(mContext).isNeedToShowAds())
            .captureStrategy(CaptureStrategy(false, "$packageName.fileprovider", "temp"))
            .maxSelectable(maxSelection) // Maximum image selection limit
            .minSelectable(0) // Minimum image selection limit
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .showPreview(true)
            .thumbnailScale(0.85f)
            .imageEngine(GlideEngine())
            .forResult(REQUEST_CODE_CHOOSE)
    }

    private fun performSubmit() {
        if (edt_details.text.toString().isEmpty()) {
            edt_details.error = "Please enter your problem"
        } else if (edt_email.text.isEmpty()) {
            edt_email.error = "Please enter your contact detail"
        } else if (!edt_email.text.toString().isValidContactInformation()) {
            edt_email.error = "Please enter valid contact detail"
        } else {
            if (isOnline()) {
                edt_details.error = null
                edt_email.error = null
                callShareAPI()
            } else {
                Toast.makeText(mContext, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(MultipartBody.FORM, descriptionString)
    }


    private fun callShareAPI() {
        progressDialog?.show()


        val reqFeedback = ModelRequestFeedback(
            package_name = packageName,
            review = edt_details.text.toString(),
            ratings = mSmileRate.toString(),
            files = imageArrayList,
            contact_information = edt_email.text.toString(),
            version_code = BuildConfig.VERSION_NAME,
            version_name = fieldName
        )

        if (mContext.isOnline()) {
            // Fetch App center data from the server
            if (isSDKBelow21()) {
                // Simple json parsing
                callShareBelow21(reqFeedback)

            } else {
                // Using retrofit with kotlin coroutine
                GlobalScope.launch(job + Dispatchers.Main) {
                    callShareRetrofitAPI(job, reqFeedback)
                }
            }
        }
    }

    private fun callShareBelow21(feedback: ModelRequestFeedback) {
        GetFeedbackResponseTask(mContext, feedback, object : JsonParserCallback {
            override fun onSuccess(response: String) {
                Log.i(TAG, "onResponse: $String")
                progressDialog?.dismiss()
                toast("Thank You For Your Feedback")
                finish()
            }

            override fun onFailure(message: String) {
                Log.e(TAG, "onResponse Failed:$message")
                toast(message)
                progressDialog?.dismiss()
            }
        }).execute()
    }


    private suspend fun callShareRetrofitAPI(job: Job, feedback: ModelRequestFeedback) {
        return withContext(job + Dispatchers.IO) {
            val retroApiInterface = FeedbackAPIService().getFeedbackClient(mContext)


            val imageArray: ArrayList<MultipartBody.Part> = ArrayList()
            for (i in imageArrayList.indices) {
                if (!imageArrayList[i].equals("null", ignoreCase = true)) {
                    val file = File(imageArrayList[i])
//                    val requestFile = RequestBody.create(MediaType.parse("*/*"), file)
//                    val a = MultipartBody.Part.createFormData("image[]", file.name, requestFile)
//                    imageArray.add(a)
                }
            }

            try {
                val reqFeedback = retroApiInterface.getFeedbackAsync(
                    createPartFromString(feedback.package_name),
                    createPartFromString(feedback.review),
                    createPartFromString(feedback.ratings),
                    imageArray,
                    createPartFromString(feedback.contact_information),
                    createPartFromString(feedback.version_name),
                    createPartFromString(feedback.version_code)
                )

                val resFeedback = reqFeedback.await()
                runOnUiThread {
                    progressDialog?.dismiss()
                    onRetrofitResponse(resFeedback)
                }
            } catch (exception: Exception) {
                Log.e(TAG, exception.toString())
                runOnUiThread {
                    progressDialog?.dismiss()
                    retryDialog(exception)
                }
            }
        }
    }

    private fun onRetrofitResponse(resFeedback: Response<ModelResponseFeedback>) {
        if (resFeedback.isSuccessful && resFeedback.body() != null) {
            val body = resFeedback.body()
            if (body!!.ResponseCode == 1) {
                Log.i(TAG, "onResponse: ${body.ResponseMessage}")
                toast("Thank You For Your Feedback")
                finish()
            } else {
                Log.e(TAG, "onResponse: ${body.ResponseMessage}")
                toast(body.ResponseMessage.toString())
            }
        } else {
            Log.e(TAG, "onResponse Failed:" + resFeedback.errorBody())
            toast(resFeedback.errorBody().toString())
        }
    }


    private fun retryDialog(t: Throwable) {

        val dialog = AlertDialog.Builder(this)
        var alert: AlertDialog? = null

        val title = "Error"
        val msg = t.toString()
        val positiveText = "Retry"
        val negativeText = "Cancel"


        dialog.setCancelable(false)

        // Initialize a new foreground color span instance
        val foregroundColorSpan = ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        val ssBuilder = SpannableStringBuilder(title)
        ssBuilder.setSpan(foregroundColorSpan, 0, title.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        dialog.setTitle(ssBuilder)

        dialog.setMessage(msg)

        dialog.setPositiveButton(positiveText) { _, _ ->
            alert?.dismiss()
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
            callShareAPI()
        }


        dialog.setNegativeButton(negativeText) { _, _ ->
            alert?.dismiss()
        }

        alert = dialog.create()
        alert.show()


        val textView = alert.findViewById<TextView>(android.R.id.message)
        try {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
            val face = Typeface.createFromAsset(assets, fontPath)
            textView.typeface = face
        } catch (e: Exception) {
            Log.e("showAlert", e.toString())
        }
    }

    private fun updateMediaList() {
        tv_current_image.text = imageArrayList.size.toString()
        if (imageArrayList.size > 0) {
            mFeedbackAdapter = FeedbackAdapter(mContext, imageArrayList)
            rv_media!!.adapter = mFeedbackAdapter
        }
        if (imageArrayList.size >= 4) {
            relative_img_1.visibility = View.GONE
        } else {
            relative_img_1.visibility = View.VISIBLE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_CHOOSE -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        val mSelected = AGallery.obtainResult(data)
                        if (mSelected.isNotEmpty() && mSelected.size > 0) {
                            val paths = AGallery.obtainPathResult(data)
                            for (path in paths) {
                                imageArrayList.add(path)
                            }
                            updateMediaList()
                        } else {
                            toast("You have selected " + mSelected.size + " images")
                        }
                    }
                    Activity.RESULT_CANCELED -> {
                        toast(getString(R.string.label_cancel_image_selection))
                    }
                    else -> {
                        toast(getString(R.string.label_failed_image_selection))
                    }
                }
            }
        }
    }

    private val feedbackStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateMediaList()
        }
    }


    public override fun onResume() {
        super.onResume()
        registerReceiver(feedbackStateReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(feedbackStateReceiver)
    }


}