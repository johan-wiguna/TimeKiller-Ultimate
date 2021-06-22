package com.example.timekiller.view.joke

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentJokeHomeBinding
import com.example.timekiller.viewmodel.JokeViewModel

class JokeHomeFragment: Fragment() {
    private lateinit var binding: FragmentJokeHomeBinding
    private lateinit var jokeViewModel: JokeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentJokeHomeBinding.inflate(inflater)

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.jokeHome_to_home)
        }
        binding.btnJokeStart.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.jokeHome_to_jokeMain)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        jokeViewModel = ViewModelProvider(this).get(JokeViewModel::class.java)
    }
}