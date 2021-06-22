package com.example.timekiller.view.advice

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentAdviceFavoriteBinding
import com.example.timekiller.repository.AdviceRepository
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.viewmodel.AdviceViewModel
import com.example.timekiller.viewmodel.AdviceViewModelFactory

class AdviceFavoriteFragment: Fragment() {
    private lateinit var binding : FragmentAdviceFavoriteBinding
    private lateinit var adviceViewModel : AdviceViewModel
    private lateinit var adapter: FavAdviceAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentAdviceFavoriteBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{Navigation.findNavController(binding.root).navigate(R.id.adviceFavorite_to_adviceHome)}
        binding.lvFavAdvice.layoutManager = LinearLayoutManager(requireContext())

        binding.swipeRefreshLayout.setOnRefreshListener { deleteUnfavedAdvices() }

        return binding.root
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
        adviceViewModel.favAdvices.observe(viewLifecycleOwner,{
            if (it.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
            adapter.setList(it)
        })
        adviceViewModel.setIsSwipeHint(false)
        adviceViewModel.setChangedStatus(false)
        adviceViewModel.getIsSwipeHint().observe(viewLifecycleOwner, {
            Log.d("SwipeHint sh", it.toString())
            Log.d("SwipeHint cs", adviceViewModel.getHasChangedStatus().value.toString())
            if (it && adviceViewModel.getHasChangedStatus().value!!.not()){
                Toast.makeText(requireContext(), R.string.swipe_hint, Toast.LENGTH_SHORT).show()
                adviceViewModel.setChangedStatus(true)
            }
        })

        adapter = FavAdviceAdapter(adviceViewModel)
        binding.lvFavAdvice.adapter = adapter
        binding.lvFavAdvice.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onStop() {
        deleteUnfavedAdvices()
        super.onStop()
    }

    private fun deleteUnfavedAdvices(){
        adviceViewModel.deleteFavAdvice(adapter.getRemovedAdvices())
        binding.swipeRefreshLayout.isRefreshing = false
    }
}