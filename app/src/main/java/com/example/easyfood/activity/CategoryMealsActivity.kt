package com.example.easyfood.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapter.CategoryMealAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragment.HomeFragment
import com.example.easyfood.viewmodel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealsViewModel
    private lateinit var categoryMealAdapter: CategoryMealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareRecycleView()

        binding.tvCategoryCount.text = intent.getStringExtra(HomeFragment.CATEGORY_NAME)

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]


        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealsViewModel.observerMealsLiveData().observe(this) {
            categoryMealAdapter.setMealsList(it)
        }

        onCategoryMealClick()
    }

    private fun onCategoryMealClick() {
        categoryMealAdapter.onItemClick = {
            val intent = Intent(this, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID, it.idMeal)
                putExtra(HomeFragment.MEAL_NAME, it.strMeal)
                putExtra(HomeFragment.MEAL_THUMP, it.strMealThumb)
            }
            startActivity(intent)
        }
    }

    private fun prepareRecycleView() {
        categoryMealAdapter = CategoryMealAdapter()
        binding.rcvMeal.apply {
            layoutManager =GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter=categoryMealAdapter
        }
    }
}