package com.example.timekiller.view.comic

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.timekiller.R
import com.example.timekiller.databinding.ListFavComicBinding
import com.example.timekiller.db.entities.ComicDataEntity
import com.example.timekiller.model.Comic
import com.example.timekiller.viewmodel.ComicViewModel

class FavComicAdapter (private val vm: ComicViewModel): RecyclerView.Adapter<ComicViewHolder>()
{
    private val comicList = ArrayList<Comic>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListFavComicBinding.inflate(layoutInflater, parent,false)
        return ComicViewHolder(binding, vm)
    }

    override fun getItemCount(): Int {
        return comicList.size
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        holder.bind(comicList[position])
    }

    fun setList(comicDataEntities: List<ComicDataEntity>) {
        comicList.clear()
        for (i in comicDataEntities){
            comicList.add(Comic(i))
        }
        notifyDataSetChanged()
    }
    fun getRemovedComics(): List<ComicDataEntity>{
        val dump = ArrayList<ComicDataEntity>()
        for (i in comicList){
            if (!i.isFavorite){
                dump.add(i.comicDataEntity)
            }
        }
        return dump
    }
}
class ComicViewHolder(
    private var binding: ListFavComicBinding,
    private val vm: ComicViewModel
):RecyclerView.ViewHolder(binding.root){
    private lateinit var fComic: Comic

    @SuppressLint("SetTextI18n")
    fun bind(fComic: Comic){
        this.fComic = fComic
        binding.tvTitleComic.text = "\"${fComic.comicDataEntity.title}\""
        setHeartIcon()

        binding.ivFav.setOnClickListener {
//            vm.setIsSwipeHint(true)
            this.fComic.toggleFav()
            setHeartIcon()
        }

        binding.tvTitleComic.setOnClickListener {
            vm.setCurrComicData(fComic.comicDataEntity)
            vm.getComicImage(binding.root.context , fComic.comicDataEntity.img)
            Navigation.findNavController(binding.root).navigate(R.id.comicFav_to_comicViewFav)
        }
    }

    private fun setHeartIcon(){
        binding.ivFav.setImageResource(
            if(this.fComic.isFavorite){
                R.drawable.ic_fav
            } else {
                R.drawable.ic_fav_border
            }
        )

        binding.ivFav.invalidate()
    }
}

