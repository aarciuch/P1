package psm.art.p1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {

    private lateinit var et2 : EditText
    private var data1: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        et2 = findViewById(R.id.et2)

    }

    override fun onResume() {
        super.onResume()
        data1 = intent.getIntExtra("DATA1",0)
        et2.setText(String.format("%d", data1))
        Log.i("PAR", "odebrano ${data1} w Akt2")
        data1 += 200;
        Toast.makeText(applicationContext,
            String.format("%d", data1),
            Toast.LENGTH_LONG).show()

    }

    override fun onBackPressed() {
        intent.putExtra("DATA1", data1)
        Log.i("PAR", "odesłano ${data1} z Akt2 do Akt1")
        setResult(111, intent)
        finish()
        super.onBackPressed()
    }
}