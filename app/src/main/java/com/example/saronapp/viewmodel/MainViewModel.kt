package com.example.saronapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.saronapp.data.model.Prediction
import com.example.saronapp.data.repository.MainRepository
import com.example.saronapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {
    private val _currentPrediction = MutableLiveData<Resource<Prediction>>(Resource.loading())
    val currentPrediction: LiveData<Resource<Prediction>> = _currentPrediction

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> = _score

    fun postRequest(expectedBilah: RequestBody, audio: MultipartBody.Part) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            _currentPrediction.value = repository.predictBilah(expectedBilah, audio)
        }
    }

    fun setCurrentPrediction() {
        if (_currentPrediction.value?.data != null) {
            if (_currentPrediction.value?.data?.expected == _currentPrediction.value?.data?.result) {
                _score.value = _score.value?.plus(1)
                _isLoading.postValue(false)
            }
        } else {
            _isLoading.postValue(false)
            _errorMessage.postValue("An error has occurred!")
        }
    }
}