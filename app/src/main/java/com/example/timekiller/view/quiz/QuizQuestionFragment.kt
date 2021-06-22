package com.example.timekiller.view.quiz

import android.app.AlertDialog
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentQuizQuestionBinding
import com.example.timekiller.model.QuizQuestion
import com.example.timekiller.util.NetworkUtil
import com.example.timekiller.viewmodel.QuizViewModel
import java.net.URLDecoder

class QuizQuestionFragment : Fragment() {
    private lateinit var binding: FragmentQuizQuestionBinding
    private val quizViewModel : QuizViewModel by navGraphViewModels(R.id.quiz_nav)
    private lateinit var adapter: AnswerAdapter
    private val looper = Looper.getMainLooper()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizQuestionBinding.inflate(inflater)

        binding.ivBack.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setMessage("Are you sure you want to forfeit the quiz?")

            builder.setPositiveButton(R.string.yes) { dialog, which ->
                Navigation.findNavController(binding.root).navigate(R.id.quizQuestion_to_quizDifficulty)
                dialog.dismiss()
            }

            builder.setNegativeButton(R.string.no) { dialog, which ->
                dialog.dismiss()
            }

            builder.show()
        }
        return binding.root
    }

    private fun getRandomQuestion() {
        if (NetworkUtil.isNetworkConnected(requireContext())) {
            quizViewModel.getRandomQuestion(requireContext())
        } else {
            Toast.makeText(requireContext(), R.string.no_connection, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = AnswerAdapter(this.quizViewModel, looper)
        binding.lvAnswers.adapter = adapter

        var questions: ArrayList<QuizQuestion> = ArrayList<QuizQuestion>()

        quizViewModel.getCurrQuizQuestion().observe(viewLifecycleOwner, {
            questions = it
            quizViewModel.setQuestionNumber(0)
            quizViewModel.setScore(0)
        })

        quizViewModel.getQuestionNumber().observe(viewLifecycleOwner, {
            binding.tvQuestion.text = URLDecoder.decode(questions[it].getQuestion())
            binding.tvCategory.text = URLDecoder.decode(questions[it].getCategory())
            binding.tvQuestionNumber.text = "Question " + (it + 1)
            adapter.setAnswers(URLDecoder.decode(questions[it].getCorrectAnswer()), questions[it].getIncorrectAnswer())
            adapter.setDifficulty(URLDecoder.decode(questions[it].getDifficulty()))
        })

        quizViewModel.getScore().observe(viewLifecycleOwner, {
            binding.tvScore.text = it.toString()
        })

        quizViewModel.getShowSpinner().observe(viewLifecycleOwner, {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.rlQuestion.visibility = View.GONE
            } else {
                binding.rlQuestion.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })

        this.getRandomQuestion()
    }
}