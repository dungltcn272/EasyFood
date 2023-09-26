package com.example.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.ItemMealBinding
import com.example.easyfood.pojo.Meal

class MealAdapter : RecyclerView.Adapter<MealAdapter.FavoriteMealViewHolder>() {

    inner class FavoriteMealViewHolder(val binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick : ((Meal) -> Unit)?=null

    private val diffUtil =object: ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
    val differ =AsyncListDiffer(this, diffUtil)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealViewHolder {
        return FavoriteMealViewHolder(ItemMealBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: FavoriteMealViewHolder, position: Int) {
        val meal =differ.currentList[position]
        Glide.with(holder.itemView).load(meal.strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text=meal.strMeal
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(meal)
        }
    }
}