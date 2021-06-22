package com.example.timekiller.view.quiz

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentQuizDifficultyBinding
import com.example.timekiller.viewmodel.QuizViewModel

class QuizDifficultyFragment: Fragment() {
    private lateinit var binding: FragmentQuizDifficultyBinding
    private val quizViewModel : QuizViewModel by navGraphViewModels(R.id.quiz_nav)
    val HIGH_SCORE_PREF = "HIGH_SCORE_PREF"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentQuizDifficultyBinding.inflate(inflater)

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.quizDifficulty_to_quizHome)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        quizViewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        quizViewModel.setDifficulty("")

        val sharedPref = activity?.getSharedPreferences(
            HIGH_SCORE_PREF, Context.MODE_PRIVATE)

        // EASY
        binding.cvEasy.setOnClickListener {
            quizViewModel.setDifficulty("easy")
        }

        binding.tvEasyHighScore.text = sharedPref!!.getInt("easy", 0).toString()

        // MEDIUM
        binding.cvMedium.setOnClickListener() {
            quizViewModel.setDifficulty("medium")
        }

        binding.tvMediumHighScore.text = sharedPref!!.getInt("medium", 0).toString()

        // HARD
        binding.cvHard.setOnClickListener() {
            quizViewModel.setDifficulty("hard")
        }

        binding.tvHardHighScore.text = sharedPref!!.getInt("hard", 0).toString()

        quizViewModel.getDifficulty().observe(viewLifecycleOwner, {
            Log.d("diff changed", it)
            if (it != "") {
                this.startQuiz()
            }
        })
    }

    private fun startQuiz(){
        val connMgr = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        if (networkInfo == null){
            //handle no connection error
            Toast.makeText(requireContext(), R.string.no_connection, Toast.LENGTH_SHORT).show()
        } else {
            Navigation.findNavController(binding.root).navigate(R.id.quizDifficulty_to_quizQuestion)
        }
    }
}