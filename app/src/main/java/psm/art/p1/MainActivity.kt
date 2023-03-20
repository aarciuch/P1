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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.work.*

const val TAG = "MainActivity"
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
            var a = it.data?.getIntExtra("DATA1",0)
            Toast.makeText(applicationContext,
                            String.format("%d", it.data?.getIntExtra("DATA1",0)),
                            Toast.LENGTH_LONG).show()
                            Log.i("PAR", "odebrano $a z Akt2 w Akt1 " )
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
            getPage()
        }


    }

    private fun getPage() {

        if (et.text.toString() != "") {
            val d1 = Data.Builder()
            d1.putString("URL", et.text.toString())

            val pageWorker: WorkRequest =
                OneTimeWorkRequestBuilder<GetPageWorker>()
                    .setInputData(d1.build())
                    .build()
            val workManager: WorkManager = WorkManager.getInstance(applicationContext)
            workManager.enqueue(pageWorker)

            workManager.getWorkInfoByIdLiveData(pageWorker.id)
                .observe(this) { workInfo ->
                    if (workInfo != null && workInfo.state.isFinished) {
                       page = workInfo.outputData.getString("RESULT")!!
                       page?.let { Log.i(TAG, it) }
                    }
                }
        } else {
            Toast.makeText(this, "Edit text is empty", Toast.LENGTH_LONG).show()
        }
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
        var intent1 = Intent(applicationContext, MainActivity2::class.java)
        var a = 500
        intent1.putExtra("DATA1",a)
        Log.i("PAR", "wysłano ${a} z Akt1  do Akt2")
        getResul1.launch(intent1)
    }

    private fun go2Act2() {
        startActivity(Intent(applicationContext, MainActivity2::class.java))
    }



}