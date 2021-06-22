package com.example.timekiller.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {
    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentHomeBinding.inflate(inflater)

        //set data pada view
        binding.cvJoke.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.home_to_jokeHome) }
        binding.cvComic.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.home_to_comicHome) }
        binding.cvQuiz.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.home_to_quizHome) }
        binding.cvAdvice.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.home_to_adviceHome) }

        return binding.root
    }
}
