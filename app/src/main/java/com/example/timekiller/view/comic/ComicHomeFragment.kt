package com.example.timekiller.view.comic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.navGraphViewModels
import com.example.timekiller.R
import com.example.timekiller.databinding.FragmentComicHomeBinding
import com.example.timekiller.db.TimekillerDatabase
import com.example.timekiller.repository.ComicRepository
import com.example.timekiller.viewmodel.*

class ComicHomeFragment : Fragment() {
    private lateinit var binding: FragmentComicHomeBinding
    private val comicViewModel: ComicViewModel by navGraphViewModels(R.id.comic_nav){
        ComicViewModelFactory(ComicRepository(TimekillerDatabase.getInstance(requireActivity().application).favComicDao))
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComicHomeBinding.inflate(inflater)

        binding.ivBack.setOnClickListener {

            Navigation.findNavController(binding.root).navigate(R.id.comicHome_to_home)
        }

        binding.btnComicStart.setOnClickListener {
            Log.d("volley", "clicked")
            comicViewModel.getRandomComic(requireContext())
            Navigation.findNavController(binding.root).navigate(R.id.comicHome_to_comicRandom)
        }

        binding.btnComicFav.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.comicHome_to_comicFav)
        }

        return binding.root
    }

    /**
     * Mengambil view model
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //observe live data di sini
        //ngetes fetch gambar random
//        comicViewModel.getCurrComicImg().observe(
//                viewLifecycleOwner,
//                {
//                    Log.d("volley random", "img changed")
//                    binding.ivComicLogo.setImageBitmap(it)
//                    binding.ivComicLogo.invalidate()
//                }
//        )
//        comicViewModel.getErrorMessage().observe(
//                viewLifecycleOwner,
//                {
//                    Log.d("volley random", it)
//                }
//        )
    }
}