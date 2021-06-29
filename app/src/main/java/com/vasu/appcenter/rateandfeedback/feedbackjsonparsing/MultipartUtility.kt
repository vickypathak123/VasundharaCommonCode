package com.vasu.appcenter.rateandfeedback.feedbackjsonparsing

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.util.*

class MultipartUtility(requestURL: String?, private val charset: String) {
    private val boundary: String = "===" + System.currentTimeMillis() + "==="
    private var httpConn: HttpURLConnection? = null
    private var outputStream: OutputStream? = null
    private var writer: PrintWriter? = null
    private val maxBufferSize = 4096
    private var contentLength: Long = 0
    private val url: URL = URL(requestURL)
    private val TAG = "MultipartLargeUtility"
    private val fields: MutableList<FormField> = ArrayList()
    private val files: MutableList<FilePart> = ArrayList()


    private inner class FormField(var name: String, var value: String)
    private inner class FilePart(var fieldName: String, var uploadFile: File)

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    @Throws(UnsupportedEncodingException::class)
    fun addFormField(name: String, value: String) {
        var fieldContent = "--$boundary$LINE_FEED"
        fieldContent += "Content-Disposition: form-data; name=\"$name\"$LINE_FEED"
        fieldContent += "Content-Type: text/plain; charset=$charset$LINE_FEED"
        fieldContent += LINE_FEED
        fieldContent += value + LINE_FEED
        contentLength += fieldContent.toByteArray(charset(charset)).size.toLong()
        fields.add(FormField(name, value))
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..."></input>
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    @Throws(IOException::class)
    fun addFilePart(fieldName: String, uploadFile: File) {
        val fileName = uploadFile.name
        var fieldContent = "--$boundary$LINE_FEED"
        fieldContent += "Content-Disposition: form-data; name=\"$fieldName\"; filename=\"$fileName\"$LINE_FEED"
        fieldContent += "Content-Type: " + URLConnection.guessContentTypeFromName(fileName) + LINE_FEED
        fieldContent += "Content-Transfer-Encoding: binary$LINE_FEED"
        fieldContent += LINE_FEED
        // file content would go here
        fieldContent += LINE_FEED
        contentLength += fieldContent.toByteArray(charset(charset)).size.toLong()
        contentLength += uploadFile.length()
        files.add(FilePart(fieldName, uploadFile))
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun finish(): List<String?> {
        val response: MutableList<String?> = ArrayList()
        val content = "--$boundary--$LINE_FEED"
        contentLength += content.toByteArray(charset(charset)).size.toLong()
        if (!openConnection()) {
            return response
        }
        writeContent()

        // checks server's status code first
        val status = httpConn!!.responseCode
        if (status == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(
                InputStreamReader(
                    httpConn!!.inputStream
                )
            )
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                response.add(line)
            }
            reader.close()
            httpConn!!.disconnect()
        } else {
            throw IOException("Server returned non-OK status: $status")
        }
        return response
    }

    @Throws(IOException::class)
    private fun openConnection(): Boolean {
        httpConn = url.openConnection() as HttpURLConnection
        httpConn!!.useCaches = false
        httpConn!!.doOutput = true // indicates POST method
        httpConn!!.doInput = true
        //httpConn.setRequestProperty("Accept-Encoding", "identity");
        //  httpConn.setFixedLengthStreamingMode(contentLength);
        httpConn!!.setRequestProperty("Connection", "Keep-Alive")
        httpConn!!.setRequestProperty("Content-Type", "multipart/form-data; boundary=$boundary")
        outputStream = BufferedOutputStream(httpConn!!.outputStream)
        writer = PrintWriter(OutputStreamWriter(outputStream, charset), true)
        return true
    }

    @Throws(IOException::class)
    private fun writeContent() {
        for (field in fields) {
            writer!!.append("--$boundary").append(LINE_FEED)
            writer!!.append("Content-Disposition: form-data; name=\"" + field.name + "\"").append(LINE_FEED)
            writer!!.append("Content-Type: text/plain; charset=$charset").append(LINE_FEED)
            writer!!.append(LINE_FEED)
            writer!!.append(field.value).append(LINE_FEED)
            writer!!.flush()
        }
        for (filePart in files) {
            val fileName = filePart.uploadFile.name
            writer!!.append("--$boundary").append(LINE_FEED)
            writer!!.append("Content-Disposition: form-data; name=\"" + filePart.fieldName + "\"; filename=\"" + fileName + "\"").append(LINE_FEED)
            writer!!.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED)
            writer!!.append("Content-Transfer-Encoding: binary").append(LINE_FEED)
            writer!!.append(LINE_FEED)
            writer!!.flush()
            val inputStream = FileInputStream(filePart.uploadFile)
            val bufferSize = Math.min(inputStream.available(), maxBufferSize)
            val buffer = ByteArray(bufferSize)
            var bytesRead = -1
            while (inputStream.read(buffer, 0, bufferSize).also { bytesRead = it } != -1) {
                outputStream!!.write(buffer, 0, bytesRead)
            }
            outputStream!!.flush()
            inputStream.close()
            writer!!.append(LINE_FEED)
            writer!!.flush()
        }
        writer!!.append("--$boundary--").append(LINE_FEED)
        writer!!.close()
    }

    companion object {
        private const val LINE_FEED = "\r\n"
    }

}