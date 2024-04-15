package com.dungltcn272.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dungltcn272.easyfood.databinding.ItemCategoryBinding

import com.dungltcn272.easyfood.model.Category

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var categoryList =ArrayList<Category>()
    lateinit var onItemClick: ((Category) -> Unit)
    inner class CategoryViewHolder(var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    fun setCategoryList(categoryList: List<Category>){
        this.categoryList=categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView).load(categoryList[position].strCategoryThumb).into(holder.binding.imgCategory)
        holder.binding.tvCategoryName.text=categoryList[position].strCategory
        holder.itemView.setOnClickListener {
            onItemClick.invoke(categoryList[position])
        }
    }
}