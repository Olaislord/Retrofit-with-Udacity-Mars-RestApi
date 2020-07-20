package com.example.retrofitproject.overview

import android.telecom.Call
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.retrofitproject.network.MarsApi
import com.example.retrofitproject.network.MarsApiStatus
import com.example.retrofitproject.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class OverviewViewModel: ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    /*private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the request status String
    val response: LiveData<String>
        get() = _response*/

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
     val properties: LiveData<List<MarsProperty>>
        get() = _properties

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
       // getMarsRealEstateProperties()
        getMarsRealEstatePropertiesWithCoroutines()
    }

    private fun getMarsRealEstatePropertiesWithCoroutines() {
        coroutineScope.launch {
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()
            try {

                _status.value = MarsApiStatus.LOADING

                var listResult = getPropertiesDeferred.await()

                _status.value = MarsApiStatus.DONE

                if (listResult.size > 0){
                    _properties.value = listResult
                }

            }catch(e: Exception) {
                _status.value =MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    /*private fun getMarsRealEstateProperties() {
        MarsApi.retrofitService.getProperties().enqueue( object : Callback<List<MarsProperty>> {
            override fun onFailure(call: retrofit2.Call<List<MarsProperty>>, t: Throwable) {
                _response.value = "Failure : " + t.message
            }

            override fun onResponse(call: retrofit2.Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
                _response.value = "Success ${response.body()?.size} Mars property retrived"
            }
        })
    }*/
}