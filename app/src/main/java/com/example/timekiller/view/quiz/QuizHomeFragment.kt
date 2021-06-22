package com.example.timekiller.view.quiz

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentQuizHomeBinding
import com.example.timekiller.viewmodel.QuizViewModel

class QuizHomeFragment: Fragment() {
    private lateinit var binding: FragmentQuizHomeBinding
    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentQuizHomeBinding.inflate(inflater)

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.quizHome_to_home)
        }

        binding.btnQuizStart.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.quizHome_to_quizDifficulty)
        }

        return binding.root
    }


}