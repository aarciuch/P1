package psm.art.p1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import psm.art.p1.databinding.ActivityForMvvmBinding

class ForMvvm : AppCompatActivity() {

    lateinit var binding : ActivityForMvvmBinding
    lateinit var viewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForMvvmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        binding.viewModel4Layout = viewModel
        binding.lifecycleOwner = this

        viewModel.licznik.observe(this){
            binding.tv2.text = it.toString()
        }

    }
}