package com.example.timekiller.view.quiz

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.ListAnswerBinding
import com.example.timekiller.viewmodel.QuizViewModel
import java.net.URLDecoder
import android.os.Handler
import android.os.Looper
import java.lang.Thread.sleep

class AnswerAdapter(viewModel: QuizViewModel, looper: Looper): BaseAdapter() {
    private val viewModel = viewModel
    private var correctAnswer = ""
    private val answers = ArrayList<String>()
    private var difficulty = ""
    private var isClicked = false

    val handler = Handler(looper)

    override fun getCount(): Int {
        return answers.size
    }

    override fun getItem(position: Int): Any {
        return answers[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        lateinit var viewHolder: ViewHolder
        lateinit var view: View

        if (convertView == null) {
            view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_answer, parent, false)
            viewHolder = ViewHolder(view, position)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.updateView(this.getItem(position) as String)
        return view
    }

    fun setAnswers(correctAnswer: String, list: ArrayList<String>) {
        this.correctAnswer = correctAnswer
        answers.clear()
        answers.addAll(list)
        answers.add(this.correctAnswer)
        answers.shuffle()

        notifyDataSetChanged()
    }

    fun setDifficulty(difficulty: String) {
        this.difficulty = difficulty
    }

    private inner class ViewHolder(
        private var view: View,
        private var pos: Int
    ) {
        private var binding = ListAnswerBinding.bind(view)

        @SuppressLint("ResourceAsColor")
        fun updateView(answer: String) {
            var answerDecoded = URLDecoder.decode(answer)

            binding.cvAnswer.setCardBackgroundColor(R.color.darker_blue)
            binding.tvAnswer.text = answerDecoded

            binding.cvAnswer.setOnClickListener {
                if(!isClicked) {
                    if(binding.tvAnswer.text == correctAnswer) {
                        viewModel.addScore(20)
                        val color = binding.cvAnswer.context.resources.getColor(R.color.green)
                        binding.cvAnswer.setCardBackgroundColor(color)
                        binding.cvAnswer.invalidate()
                        goToNextPage()
                    } else {
                        if(viewModel.getScore().value!! >= 10) {
                            viewModel.subScore(10)
                        }

                        val color = binding.cvAnswer.context.resources.getColor(R.color.red)
                        binding.cvAnswer.setCardBackgroundColor(color)
                        binding.cvAnswer.invalidate()
                        goToNextPage()
                    }
                    isClicked = true
                }
            }

        }

        private fun goToNextPage(){
            handler.postDelayed({
                Log.d("answer", correctAnswer)

                isClicked = false

                if(viewModel.getQuestionNumber().value == 19) {
                    Navigation.findNavController(binding.root).navigate(R.id.quizQuestion_to_quizEnd)
                } else {
                    viewModel.incrementQuestionNumber()
                }
            }, 500)
        }
    }
}