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
import javax.net.ssl.HttpsURLConnection


class GetPageWorker(ctx: Context, workerParameters: WorkerParameters) :
                    Worker(ctx, workerParameters) {

    private val TAG = "GetPageWorker"

    override fun doWork(): Result {
       val url = inputData.getString("URL")

       return try {
           val result = uploadPage(url!!)
           Log.i(TAG, "doWork success")
           val output : Data = workDataOf("RESULT" to result)
           Log.i(TAG, result)
           Result.success(output)
       } catch (throwable : Throwable) {
           Log.e(TAG, "doWork failure ${throwable.message}")
           Result.failure()
       }
    }

    private fun uploadPage(url: String) : String {
        var s = ""
        Log.i(TAG, url)
        Log.i(TAG, "doWork start working")
        val url1 = URL(url)
        with(url1.openConnection() as HttpsURLConnection) {
            requestMethod = "GET"
            inputStream.bufferedReader().use {
                it.lines().forEach { line ->
                    s+= line
                    s+= '\n'
                }
            }
        }

        return s.substring(0,2047)
        //return s
    }

   /* private fun trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                return arrayOf()
            }

            @Throws(CertificateException::class)
            override fun checkClientTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?
            ) {
            }

            @Throws(CertificateException::class)
            override fun checkServerTrusted(
                chain: Array<X509Certificate?>?,
                authType: String?
            ) {
            }
        })
        try {
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            HttpsURLConnection
                .setDefaultSSLSocketFactory(sc.socketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/
}