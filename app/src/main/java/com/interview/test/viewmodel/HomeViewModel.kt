package com.interview.test.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel: ViewModel() {


    private val _showBottomBar: MutableLiveData<Boolean> = MutableLiveData()
    val showBottomBar: LiveData<Boolean> = _showBottomBar


    fun updateBottomBar(show: Boolean) {
        _showBottomBar.postValue(show)
    }
}