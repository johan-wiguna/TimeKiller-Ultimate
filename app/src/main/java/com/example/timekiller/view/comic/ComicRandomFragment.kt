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
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.db.entities.ComicDataEntity
import com.example.timekiller.repository.ComicRepository
import com.example.timekiller.util.NetworkUtil
import com.example.timekiller.viewmodel.ComicViewModel
import com.example.timekiller.viewmodel.ComicViewModelFactory

class ComicRandomFragment: Fragment() {
    private lateinit var binding : FragmentComicRandomBinding
    private var currComic : ComicDataEntity? = null
    private val comicViewModel: ComicViewModel by navGraphViewModels(R.id.comic_nav){
        ComicViewModelFactory(ComicRepository(TimekillerDatabase.getInstance(requireActivity().application).favComicDao))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  FragmentComicRandomBinding.inflate(inflater)

        binding.ivBack.setOnClickListener{Navigation.findNavController(binding.root).navigate(R.id.comicRandom_to_comicHome)}
        binding.btnComic.setOnClickListener {this.getRandomComic()}
        binding.ivFav.setOnClickListener {this.toggleFavorite()}

        return binding.root
    }

    private fun getRandomComic(){
        if (NetworkUtil.isNetworkConnected(requireContext())){
            comicViewModel.getRandomComic(requireContext())
        } else {
            Toast.makeText(requireContext(), R.string.no_connection, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Mengambil view model
     */
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //observe live data di sini
        comicViewModel.getCurrComicData().observe(viewLifecycleOwner, {
            currComic = it
            binding.tvComicTitle.text = it.title

            var date = ""
            date += it.day.toString() + " / " + it.month.toString()  + " / " + it.year.toString()
            binding.tvComicDate.text = date
        })

        comicViewModel.getCurrComicImg().observe(viewLifecycleOwner, {
            binding.ivComicMain.setImageBitmap(it)
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

        comicViewModel.getShowSpinner().observe(viewLifecycleOwner, {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
                binding.pageContent.visibility = View.GONE
            } else {
                binding.pageContent.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })


//        this.getRandomComic()
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