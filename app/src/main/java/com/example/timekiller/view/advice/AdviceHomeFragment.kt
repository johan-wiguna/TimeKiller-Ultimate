package com.example.timekiller.view.advice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentAdviceHomeBinding
import com.example.timekiller.repository.AdviceRepository
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.viewmodel.AdviceViewModel
import com.example.timekiller.viewmodel.AdviceViewModelFactory

class AdviceHomeFragment: Fragment() {
    private lateinit var binding : FragmentAdviceHomeBinding
    private lateinit var adviceViewModel : AdviceViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentAdviceHomeBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{Navigation.findNavController(binding.root).navigate(R.id.adviceHome_to_home)}
        binding.btnAdvice.setOnClickListener {Navigation.findNavController(binding.root).navigate(R.id.adviceHome_to_adviceRandom)}
        binding.btnFavAdvice.setOnClickListener {Navigation.findNavController(binding.root).navigate(R.id.adviceHome_to_adviceFavorite)}

        return binding.root
    }

    /**
     * Mengambil view model
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val dao = TimekillerDatabase.getInstance(requireActivity().application).favAdviceDao
        val repository = AdviceRepository(dao)
        val factory = AdviceViewModelFactory(repository)
        adviceViewModel = ViewModelProvider(this,factory).get(AdviceViewModel::class.java)

        //observe live data di sini
    }

}