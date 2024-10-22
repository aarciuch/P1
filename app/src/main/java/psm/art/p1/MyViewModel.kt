package psm.art.p1

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel() : ViewModel() {
    private var _licznik = MutableLiveData<Int>(0)
    val licznik : LiveData<Int> = _licznik

    fun updateLicznik() {
        _licznik.value = _licznik.value?.inc()
    }
}