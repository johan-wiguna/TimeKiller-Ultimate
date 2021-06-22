package com.example.timekiller.view.joke

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentJokeMainBinding
import com.example.timekiller.model.Joke
import com.example.timekiller.model.JokeThree
import com.example.timekiller.model.JokeTwo
import com.example.timekiller.util.NetworkUtil
import com.example.timekiller.viewmodel.JokeViewModel
import com.github.florent37.expansionpanel.ExpansionLayout

class JokeMainFragment: Fragment() {
    private lateinit var binding: FragmentJokeMainBinding
    private lateinit var jokeViewModel: JokeViewModel
    private var currJoke : Joke? = null
    private var currJokeTwo : String? = null
    private var currJokeThree : JokeThree? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentJokeMainBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{ Navigation.findNavController(binding.root).navigate(R.id.jokeMain_to_jokeHome)}
        binding.btnJoke.setOnClickListener {this.getRandomJoke()}

        binding.expansionLayout.addListener { expansionLayout, expanded ->
            if (expanded) {
                expansionLayout.expand(true)
            } else {
                expansionLayout.collapse(false)
            }
        }

        return binding.root
    }

    private fun getRandomJoke(){
        if (NetworkUtil.isNetworkConnected(requireContext())){
            jokeViewModel.getRandomizedJoke(requireContext())
        } else {
            Toast.makeText(requireContext(), R.string.no_connection, Toast.LENGTH_SHORT).show()
        }
    }

    private fun decodeHtml(string: String): String{
        return if (Build.VERSION.SDK_INT >= 24)
        {
            Html.fromHtml(string, Html.FROM_HTML_MODE_LEGACY).toString()
        }
        else
        {
            Html.fromHtml(string).toString()
        }
    }

    /**
     * Mengambil view model
     */
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        jokeViewModel = ViewModelProvider(this).get(JokeViewModel::class.java)

        //observe live data di sini
        jokeViewModel.getCurrJokes().observe(viewLifecycleOwner, {
            currJoke = it
            if (it != null){
                binding.llJoke.visibility = View.VISIBLE
                binding.tvCategoryLabel.visibility = View.VISIBLE
                binding.tvCategory.visibility = View.VISIBLE

                binding.tvCategory.text = it.getType().substring(0, 1).toUpperCase() + it.getType().substring(1).toLowerCase()
                binding.tvJokeSetup.text = it.getSetup()
                binding.tvJokePunchline.text = it.getPunchline()
            } else {
                binding.expansionLayout.collapse(true)
                binding.llJoke.visibility = View.INVISIBLE
                binding.tvCategoryLabel.visibility = View.INVISIBLE
                binding.tvCategory.visibility = View.INVISIBLE

                binding.tvCategory.text = ""
                binding.tvJokeSetup.text = ""
                binding.tvJokePunchline.text = ""
            }
        })

        jokeViewModel.getCurrJokesTwo().observe(viewLifecycleOwner, {
            currJokeTwo = it
            if (it != null){
                binding.tvJoke2.visibility = View.VISIBLE
                binding.tvCategoryLabel.visibility = View.VISIBLE
                binding.tvCategory.visibility = View.VISIBLE

                binding.tvCategory.text = requireContext().getString(R.string.dad_joke)
                binding.tvJoke2.text = it
            } else {
                binding.tvJoke2.visibility = View.INVISIBLE
                binding.tvCategoryLabel.visibility = View.INVISIBLE
                binding.tvCategory.visibility = View.INVISIBLE

                binding.tvCategory.text = ""
                binding.tvJoke2.text = ""
            }
        })

        jokeViewModel.getCurrJokesThree().observe(viewLifecycleOwner, {
            currJokeThree = it
            if (it != null){
                binding.tvJoke2.visibility = View.VISIBLE
                binding.tvCategoryLabel.visibility = View.VISIBLE
                binding.tvCategory.visibility = View.VISIBLE

                binding.tvCategory.text = requireContext().getString(R.string.cn_joke)
                binding.tvJoke2.text = decodeHtml(it.getJoke())
            } else {
                binding.tvJoke2.visibility = View.INVISIBLE
                binding.tvCategoryLabel.visibility = View.INVISIBLE
                binding.tvCategory.visibility = View.INVISIBLE

                binding.tvCategory.text = ""
                binding.tvJoke2.text = ""
            }
        })

        jokeViewModel.getErrorMessage().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), R.string.conn_error, Toast.LENGTH_SHORT).show()
        })

        jokeViewModel.getShowSpinner().observe(viewLifecycleOwner, {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.llJoke.visibility = View.INVISIBLE
                binding.ivJokeLogo.visibility = View.INVISIBLE
                binding.tvJoke2.visibility = View.INVISIBLE
                binding.tvCategoryLabel.visibility = View.INVISIBLE
                binding.tvCategory.visibility = View.INVISIBLE
            } else {
                binding.ivJokeLogo.visibility = View.VISIBLE
                binding.progressBar.visibility = View.INVISIBLE
            }
        })

        this.getRandomJoke()
    }
}