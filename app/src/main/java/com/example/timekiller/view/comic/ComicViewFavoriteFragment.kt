package com.example.timekiller.view.comic

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentComicRandomBinding
import com.example.timekiller.databinding.FragmentComicViewFavoriteBinding
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.db.entities.ComicDataEntity
import com.example.timekiller.repository.ComicRepository
import com.example.timekiller.util.NetworkUtil
import com.example.timekiller.viewmodel.ComicViewModel
import com.example.timekiller.viewmodel.ComicViewModelFactory

class ComicViewFavoriteFragment: Fragment() {
    private lateinit var binding : FragmentComicViewFavoriteBinding
    private var currComic : ComicDataEntity? = null
    private val comicViewModel: ComicViewModel by navGraphViewModels(R.id.comic_nav){
        ComicViewModelFactory(ComicRepository(TimekillerDatabase.getInstance(requireActivity().application).favComicDao))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentComicViewFavoriteBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{Navigation.findNavController(binding.root).navigate(R.id.comicViewFav_to_comicFav)}
        binding.ivFav.setOnClickListener {this.toggleFavorite()}

        return binding.root
    }


    /**
     * Mengambil view model
     */
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //observe live data di sini

        comicViewModel.getShowSpinner().observe(viewLifecycleOwner, {
            var currComic = comicViewModel.getCurrComicData().value
            if (currComic != null) {
                binding.tvComicTitle.text = currComic.title
                var date = ""
                date += currComic.day.toString() + " / " + currComic.month.toString()  + " / " + currComic.year.toString()
                binding.tvComicDate.text = date
            }

            binding.ivComicMain.setImageBitmap(comicViewModel.getCurrComicImg().value)

            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.pageContent.visibility = View.GONE
            } else {
                binding.pageContent.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })

        comicViewModel.getIsCurrComicFav().observe(viewLifecycleOwner, {
            if (it) {
                binding.ivFav.setImageResource(R.drawable.ic_fav)
                binding.ivFav.invalidate()
            } else {
                binding.ivFav.setImageResource(R.drawable.ic_fav_border)
                binding.ivFav.invalidate()
            }
        })

        comicViewModel.getErrorMessage().observe(viewLifecycleOwner, {
            Toast.makeText(requireContext(), R.string.conn_error, Toast.LENGTH_SHORT).show()
        })
    }

    fun toggleFavorite() {
        if (currComic != null){
            if (comicViewModel.getIsCurrComicFav().value!!){
                comicViewModel.deleteFavComic(currComic!!)
                Toast.makeText(requireContext(), R.string.unmark_favorite, Toast.LENGTH_SHORT).show()
            } else {
                comicViewModel.insertFavComic(currComic!!)
                Toast.makeText(requireContext(), R.string.mark_favorite, Toast.LENGTH_SHORT).show()
            }
        }
    }
}