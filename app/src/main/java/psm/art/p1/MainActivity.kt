package psm.art.p1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.work.*
import androidx.work.Data.fromByteArray
import androidx.work.WorkManager
import java.lang.Exception
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {

    private lateinit var et : EditText
    private var state : String? = null
    private var sharedPref: SharedPreferences? = null

    private lateinit var b1: Button
    private lateinit var b2: Button
    private lateinit var b3: Button

    private var page : String? = null



    var getResul1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        if (it.resultCode == 111) {
            Toast.makeText(applicationContext,
                            String.format("%d", it.data?.getIntExtra("DATA1",0)),
                            Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val STATE = "STATE"
        private val ACTIVITY_LIFECYCLE_EVENT = "ACTIVITY_LIFECYCLE_EVENT"
        private val SHARE_PREF_1 = "SHARE_PREF_1"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onCretate")

        sharedPref = applicationContext?.getSharedPreferences(SHARE_PREF_1, Context.MODE_PRIVATE) ?: return
        state = savedInstanceState?.getString(STATE)
        if (savedInstanceState == null) {
            state = sharedPref!!.getString(STATE, "")
        }

        setContentView(R.layout.activity_main)

        et = findViewById(R.id.et)
        et.setText(state)


        /***************************************************/
        b1 = findViewById(R.id.b1)
        b1.setOnClickListener {
            go2Act2()
        }
        b2 = findViewById(R.id.b2)
        b2.setOnClickListener {
            go2Act2WithParams()
        }

        /********************************************************/
        b3 = findViewById(R.id.b3)
        b3.setOnClickListener {
            page = getPage()
            Log.i("ACTIVITY", page!!)
        }


    }

    private fun getPage(): String? {
        val d1 = Data.Builder()
        d1.putString("URL", "https://ktel.wat.edu.pl/")
        var s = ""
        val pageWorker:  WorkRequest =
            OneTimeWorkRequestBuilder<GetPageWorker>()
                .setInputData(d1.build())
                .build()
        val workManager: WorkManager = WorkManager.getInstance(applicationContext)
            workManager.enqueue(pageWorker)

        workManager.getWorkInfoByIdLiveData(pageWorker.id)
            .observe(this, Observer {
                if (it != null && it.state.isFinished) {
                    s = it.outputData.getString("RESULT")!!
                    Log.i("MAIN ACT",s)
                }
            })

        return s
    }


    override fun onStart() {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onStart")
        super.onStart()
    }

    override fun onResume() {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onStop")
        with(sharedPref?.edit() ?: return) {
            state = et.text.toString()
            putString(STATE, state)
            apply()
        }
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onDestroy")
        super.onDestroy()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onRestoreInstanceState")
        state = savedInstanceState.getString(STATE).toString()
        et.setText(state)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i(ACTIVITY_LIFECYCLE_EVENT, "onSaveInstanceState")
        state = et.text.toString()
        outState.run {
            putString(STATE, state)
        }
        super.onSaveInstanceState(outState)
    }

    /***************************************************************/
    private fun go2Act2WithParams() {
        var intent1: Intent = Intent(applicationContext, MainActivity2::class.java)
        intent1.putExtra("DATA1",100)
        getResul1.launch(intent1)
    }

    private fun go2Act2() {
        startActivity(Intent(applicationContext, MainActivity2::class.java))
    }

}