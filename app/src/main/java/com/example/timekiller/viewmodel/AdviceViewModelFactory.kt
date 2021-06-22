package com.example.timekiller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timekiller.repository.AdviceRepository
import java.lang.IllegalArgumentException

class AdviceViewModelFactory(private val repository: AdviceRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AdviceViewModel::class.java)){
            return AdviceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}