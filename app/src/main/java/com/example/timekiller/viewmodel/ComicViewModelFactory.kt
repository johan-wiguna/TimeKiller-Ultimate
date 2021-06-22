package com.example.timekiller.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.timekiller.repository.ComicRepository
import java.lang.IllegalArgumentException

class ComicViewModelFactory(private val repository: ComicRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ComicViewModel::class.java)){
            return ComicViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")
    }
}