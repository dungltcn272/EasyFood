package com.dungltcn272.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dungltcn272.easyfood.databinding.ItemMealBinding
import com.dungltcn272.easyfood.model.MealByCategory

class CategoryMealAdapter : RecyclerView.Adapter<CategoryMealAdapter.CategoryMealViewHolder>(){

    private var mealsList = ArrayList<MealByCategory>()

    var onItemClick : ((MealByCategory) ->Unit) ?=null

    fun setMealsList(mealsList: List<MealByCategory>){
        this.mealsList=mealsList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }
    inner class CategoryMealViewHolder(var binding: ItemMealBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealViewHolder {
        return CategoryMealViewHolder(ItemMealBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    override fun onBindViewHolder(holder: CategoryMealViewHolder, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text=mealsList[position].strMeal
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(mealsList[position])
        }
    }
}