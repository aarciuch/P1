package psm.art.p1

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import java.net.URL
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*


class GetPageWorker(ctx: Context, workerParameters: WorkerParameters) :
                    Worker(ctx, workerParameters) {

    private val TAG = "GetPageWorker"

    override fun doWork(): Result {
       val url = inputData.getString("URL")

       return try {
           var resultString = uploadPage(url!!)
           resultString += "\nLENGHT: " + resultString.length.toString()
           var output: Data?
           Log.i(TAG, "doWork success")
           if (resultString.length <= 1024 * 4) {
                output = workDataOf("RESULT" to resultString)
               Result.success(output)
           } else {
               resultString = resultString.substring(0,1024 * 4 - 1) + "\nLENGHT: " + resultString.length.toString()

               output = workDataOf("RESULT" to resultString)
               Result.success(output)
           }
       } catch (throwable : Throwable) {
           Log.e(TAG, "${throwable.message!!}")
           val output = workDataOf("RESULT" to "${throwable.message!!}")
           Result.failure(output)
       }
    }

    private fun uploadPage(url: String): String {
        var s = ""
        Log.i(TAG, url)
        Log.i(TAG, "doWork start working")
        val url1 = URL(url)
        HttpsTrustManager().allowAllSSL()
        try {
            with(url1.openConnection() as HttpsURLConnection) {
                requestMethod = "GET"
                inputStream.bufferedReader().use {
                    it.lines().forEach { line ->
                        s+= line
                        s+= '\n'
                    }
                }
            }
        } catch (e : Throwable) {
            Log.i(TAG, e.message!!)
            s = e.message!!
        }
        return s
    }
}