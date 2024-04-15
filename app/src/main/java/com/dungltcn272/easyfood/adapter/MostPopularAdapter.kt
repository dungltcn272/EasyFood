package com.dungltcn272.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dungltcn272.easyfood.databinding.ItemPopularBinding
import com.dungltcn272.easyfood.model.MealByCategory

class MostPopularAdapter: RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {

    private var mealList =ArrayList<MealByCategory>()
    lateinit var onItemClick : ((MealByCategory) -> Unit)
    var onItemLongClick : ((MealByCategory) -> Unit) ?=null

    fun setMeals(mealList : ArrayList<MealByCategory>){
        this.mealList =mealList
        notifyDataSetChanged()
    }
    class PopularMealViewHolder(var bind: ItemPopularBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealList[position].strMealThumb).into(holder.bind.imgPopularItem)
        holder.itemView.setOnClickListener {
            onItemClick.invoke(mealList[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClick?.invoke(mealList[position])
            true
        }
    }
}