package com.example.timekiller.view.comic

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentComicFavoriteBinding
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.repository.ComicRepository
import com.example.timekiller.viewmodel.ComicViewModel
import com.example.timekiller.viewmodel.ComicViewModelFactory

class ComicFavoriteFragment: Fragment() {
    private lateinit var binding : FragmentComicFavoriteBinding
    private lateinit var adapter: FavComicAdapter
    private val comicViewModel: ComicViewModel by navGraphViewModels(R.id.comic_nav){
        ComicViewModelFactory(ComicRepository(TimekillerDatabase.getInstance(requireActivity().application).favComicDao))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentComicFavoriteBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{Navigation.findNavController(binding.root).navigate(R.id.comicFav_to_comicHome)}
        binding.lvFavComic.layoutManager = LinearLayoutManager(requireContext())

        binding.swipeRefreshLayout.setOnRefreshListener { deleteUnfavedComics() }

        return binding.root
    }

    /**
     * Mengambil view model
     */
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val dao = TimekillerDatabase.getInstance(requireActivity().application).favComicDao
//        val repository = ComicRepository(dao)
//        val factory = ComicViewModelFactory(repository)
//        comicViewModel = ViewModelProvider(this,factory).get(ComicViewModel::class.java)
        adapter = FavComicAdapter(comicViewModel)

        //observe live data di sini
        comicViewModel.comics.observe(viewLifecycleOwner,{
            if (it.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.tvEmpty.visibility = View.GONE
            }
            adapter.setList(it)
        })
        comicViewModel.setIsSwipeHint(false)
        comicViewModel.setChangedStatus(false)
        comicViewModel.getIsSwipeHint().observe(viewLifecycleOwner, {
            Log.d("SwipeHint sh", it.toString())
            Log.d("SwipeHint cs", comicViewModel.getHasChangedStatus().value.toString())
            if (it && comicViewModel.getHasChangedStatus().value!!.not()){
                Toast.makeText(requireContext(), R.string.swipe_hint, Toast.LENGTH_SHORT).show()
                comicViewModel.setChangedStatus(true)
            }
        })

        adapter = FavComicAdapter(comicViewModel)
        binding.lvFavComic.adapter = adapter
        binding.lvFavComic.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
    }

    override fun onStop() {
        deleteUnfavedComics()
        super.onStop()
    }

    private fun deleteUnfavedComics(){
        comicViewModel.deleteFavAdvice(adapter.getRemovedComics())
        binding.swipeRefreshLayout.isRefreshing = false
    }
}