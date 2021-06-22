package com.example.timekiller.view.advice

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timekiller.R
import com.example.timekiller.databinding.ListFavAdviceBinding
import com.example.timekiller.db.entities.AdviceEntity
import com.example.timekiller.model.Advice
import com.example.timekiller.viewmodel.AdviceViewModel

class FavAdviceAdapter (private val vm: AdviceViewModel): RecyclerView.Adapter<AdviceViewHolder>()
{
    private val adviceList = ArrayList<Advice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListFavAdviceBinding.inflate(layoutInflater, parent,false)
        return AdviceViewHolder(binding, vm)
    }

    override fun getItemCount(): Int {
        return adviceList.size
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        holder.bind(adviceList[position])
    }

    fun setList(adviceEntities: List<AdviceEntity>) {
        adviceList.clear()
        for (i in adviceEntities){
            adviceList.add(Advice(i))
        }
        Log.d("adviceEntity list", adviceEntities.toString())
        Log.d("adviceModel list", adviceList.toString())
        notifyDataSetChanged()
    }
    fun getRemovedAdvices(): List<AdviceEntity>{
        val dump = ArrayList<AdviceEntity>()
        for (i in adviceList){
            if (!i.isFavorite){
                dump.add(i.adviceEntity)
            }
        }
        return dump
    }
}
class AdviceViewHolder(
        private var binding: ListFavAdviceBinding,
        private val vm: AdviceViewModel
):RecyclerView.ViewHolder(binding.root){
    private lateinit var fAdvice: Advice

    @SuppressLint("SetTextI18n")
    fun bind(fAdvice: Advice){
        this.fAdvice = fAdvice
        binding.tvAdvice.text = "\"${fAdvice.adviceEntity.advice}\""
        setHeartIcon()

        binding.ivFav.setOnClickListener{
            vm.setIsSwipeHint(true)
            this.fAdvice.toggleFav()
            setHeartIcon()
        }
    }

    private fun setHeartIcon(){
        binding.ivFav.setImageResource(
            if(this.fAdvice.isFavorite){
                R.drawable.ic_fav
            } else {
                R.drawable.ic_fav_border
            }
        )

        binding.ivFav.invalidate()
    }
}

