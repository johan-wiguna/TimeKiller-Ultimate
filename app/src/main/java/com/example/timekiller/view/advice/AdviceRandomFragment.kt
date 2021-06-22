package com.example.timekiller.view.advice

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentAdviceRandomBinding
import com.example.timekiller.repository.AdviceRepository
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.db.entities.AdviceEntity
import com.example.timekiller.util.NetworkUtil
import com.example.timekiller.viewmodel.AdviceViewModel
import com.example.timekiller.viewmodel.AdviceViewModelFactory

class AdviceRandomFragment: Fragment() {
    private lateinit var binding : FragmentAdviceRandomBinding
    private lateinit var adviceViewModel : AdviceViewModel
    private var currAdviceEntity : AdviceEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentAdviceRandomBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{Navigation.findNavController(binding.root).navigate(R.id.adviceRandom_to_adviceHome)}
        binding.btnAdvice.setOnClickListener {this.getRandomAdvice()}
        binding.ivFav.setOnClickListener {this.toggleFavorite()}

        return binding.root
    }

    private fun getRandomAdvice(){
        if (NetworkUtil.isNetworkConnected(requireContext())){
            adviceViewModel.getRandomAdvice(requireContext())
        } else {
            Toast.makeText(requireContext(), R.string.no_connection, Toast.LENGTH_SHORT).show()
        }
    }

    private fun toggleFavorite(){
        if (currAdviceEntity != null){
            if (adviceViewModel.getIsCurrAdviceFav().value!!){
                adviceViewModel.deleteFavAdvice(currAdviceEntity!!)
                Toast.makeText(requireContext(), R.string.unmark_favorite, Toast.LENGTH_SHORT).show()
            } else {
                adviceViewModel.insertFavAdvice(currAdviceEntity!!).toString()
                Toast.makeText(requireContext(), R.string.mark_favorite, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Mengambil view model
     */
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = TimekillerDatabase.getInstance(requireActivity().application).favAdviceDao
        val repository = AdviceRepository(dao)
        val factory = AdviceViewModelFactory(repository)
        adviceViewModel = ViewModelProvider(this,factory).get(AdviceViewModel::class.java)

        //observe live data di sini
        adviceViewModel.getCurrAdvice().observe(viewLifecycleOwner, {
            currAdviceEntity = it
            if (it != null){
                binding.tvAdviceContent.text = "\"${it.advice}\""
            } else {
                binding.tvAdviceContent.text = ""
            }
        })
        adviceViewModel.getErrorMessage().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), R.string.conn_error, Toast.LENGTH_SHORT).show()
        })

        adviceViewModel.getIsCurrAdviceFav().observe(viewLifecycleOwner, {
            if (it) {
                binding.ivFav.setImageResource(R.drawable.ic_fav)
                binding.ivFav.invalidate()
            } else {
                binding.ivFav.setImageResource(R.drawable.ic_fav_border)
                binding.ivFav.invalidate()
            }
        })

        adviceViewModel.getShowSpinner().observe(viewLifecycleOwner, {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.tvAdviceContent.visibility = View.GONE
            } else {
                binding.tvAdviceContent.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })

        this.getRandomAdvice()
    }
}