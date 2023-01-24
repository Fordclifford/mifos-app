package org.mifos.mobile.ui.activities.mpesa

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    var mpesa_response: MutableLiveData<Resp?> = MutableLiveData()


    fun stkpush(mpesa: Mpesa) {
        viewModelScope.launch {
            val response = repository.stkpush(mpesa)
            println("Resp $response")
            if (response is Resource.Success<*>) {
                mpesa_response.postValue(response.data)

            } else if (response is Resource.Error<*>) {
                mpesa_response.postValue(null)
            }
        }

    }

}

