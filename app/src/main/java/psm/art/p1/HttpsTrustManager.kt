package psm.art.p1

import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

class HttpsTrustManager : X509TrustManager {

    private var trustManagers: Array<TrustManager> = emptyArray()
    private var acceptedIssuers = arrayOf<X509Certificate>()

   override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        throw java.security.cert.CertificateException()
    }

    fun isClientTrusted(chain: Array<X509Certificate?>?): Boolean {
        return true
    }

    fun isServerTrusted(chain: Array<X509Certificate?>?): Boolean {
        return true
    }

    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
        throw java.security.cert.CertificateException()
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return acceptedIssuers;
    }

    fun allowAllSSL() {

        HttpsURLConnection.setDefaultHostnameVerifier { arg0, arg1 -> true }
        var context: SSLContext? = null
        if (trustManagers == null) {
            trustManagers = arrayOf(HttpsTrustManager())
        }
        try {
            context = SSLContext.getInstance("TLS")
            context.init(null, trustManagers, SecureRandom())
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(
            context!!.socketFactory
        )
    }

}