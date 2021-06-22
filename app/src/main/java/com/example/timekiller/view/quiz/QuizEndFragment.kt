package com.example.timekiller.view.quiz

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentQuizEndBinding
import com.example.timekiller.viewmodel.QuizViewModel
import java.net.URLDecoder

class QuizEndFragment: Fragment() {
    private val quizViewModel : QuizViewModel by navGraphViewModels(R.id.quiz_nav)
    private lateinit var binding: FragmentQuizEndBinding
    val HIGH_SCORE_PREF = "HIGH_SCORE_PREF"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val finalScore = quizViewModel.getScore().value
        val difficulty = quizViewModel.getDifficulty().value.toString()

        binding.tvFinalScore.text = finalScore.toString()

        //HIGH SCORE SHARED PREFERENCES
        val sharedPref = activity?.getSharedPreferences(
            HIGH_SCORE_PREF, Context.MODE_PRIVATE)
        val editor = sharedPref!!.edit()

        if(difficulty == "easy") {
            quizViewModel.setEasyHighScore(
                sharedPref.getInt("easy", 0)
            )

            Log.d("easy", quizViewModel.getEasyHighScore().value.toString())

            if(finalScore!! > quizViewModel.getEasyHighScore().value!!) {
                editor.putInt("easy", finalScore)
                quizViewModel.setEasyHighScore(finalScore)
                binding.tvNewHighScore.visibility = View.VISIBLE
            }

            Log.d("easy", "afterIf" + quizViewModel.getEasyHighScore().value.toString())

            editor.commit()
            binding.tvHighScore.text = sharedPref.getInt("easy", 0).toString()
        } else if(difficulty == "medium") {
            quizViewModel.setMediumHighScore(
                sharedPref.getInt("medium", 0)
            )

            if(finalScore!! > quizViewModel.getMediumHighScore().value!!) {
                editor.putInt("medium", finalScore)
                quizViewModel.setMediumHighScore(finalScore)
                binding.tvNewHighScore.visibility = View.VISIBLE
            }

            editor.commit()
            binding.tvHighScore.text = sharedPref.getInt("medium", 0).toString()
        } else {
            quizViewModel.setHardHighScore(
                sharedPref.getInt("hard", 0)
            )

            if(finalScore!! > quizViewModel.getHardHighScore().value!!) {
                editor.putInt("hard", finalScore)
                quizViewModel.setHardHighScore(finalScore)
                binding.tvNewHighScore.visibility = View.VISIBLE
            }

            editor.commit()
            binding.tvHighScore.text = sharedPref.getInt("hard", 0).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuizEndBinding.inflate(inflater)

        binding.cvReplay.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.quizEnd_to_quizDifficulty)
            binding.tvNewHighScore.visibility = View.GONE
        }

        binding.cvExit.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.quizEnd_to_home)
            binding.tvNewHighScore.visibility = View.GONE
        }

        return binding.root
    }
}