package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activity.CategoryMealsActivity
import com.example.easyfood.activity.MainActivity
import com.example.easyfood.activity.MealActivity
import com.example.easyfood.adapter.CategoryAdapter
import com.example.easyfood.adapter.MostPopularAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragment.bottomsheet.MealBottomSheetFragment
import com.example.easyfood.model.MealByCategory
import com.example.easyfood.model.Meal
import com.example.easyfood.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var randomMeal : Meal

    private lateinit var popularItemAdapter : MostPopularAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    companion object{
        const val MEAL_ID = "package com.example.easyfood.fragment.idMeal"
        const val MEAL_NAME = "package com.example.easyfood.fragment.idName"
        const val MEAL_THUMP = "package com.example.easyfood.fragment.idThump"
        const val CATEGORY_NAME = "package com.example.easyfood.fragment.categoryName"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =(activity as MainActivity).viewModel
        popularItemAdapter= MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View{
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingCase()
        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        prepareItemPopularRecycleView()
        viewModel.getPopularItem()
        observerPopularItemLiveData()
        onPopularItemClick()

        prepareItemCategoryRecycleView()
        viewModel.getCategories()
        observerCategoriesLiveData()
        onCategoryItemClick()

        onPopularItemLongClick()

        onSearchMealClick()
    }

    private fun loadingCase() {
        binding.loadingBar.visibility=View.VISIBLE
    }

    private fun onSearchMealClick() {
        binding.imgHomeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemAdapter.onItemLongClick = {
            val mealBottomSheetFragment =MealBottomSheetFragment.newInstance(it.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryItemClick() {
        categoryAdapter.onItemClick ={
            val intent =Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareItemCategoryRecycleView() {
        categoryAdapter = CategoryAdapter()
        binding.rcvCategories.apply {
            layoutManager =GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter=categoryAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        viewModel.observerCategoriesLiveData().observe(viewLifecycleOwner
        ) {
            categoryAdapter.setCategoryList(it)
        }
    }

    private fun onPopularItemClick() {
        popularItemAdapter.onItemClick = {
            Intent(activity,MealActivity::class.java).also {
                it.putExtra(MEAL_ID, randomMeal.idMeal)
                it.putExtra(MEAL_NAME, randomMeal.strMeal)
                it.putExtra(MEAL_THUMP, randomMeal.strMealThumb)
                if (randomMeal.strMeal != null && randomMeal.strMealThumb !=null){
                    startActivity(it)
                }
            }
        }
    }

    private fun observerPopularItemLiveData() {
        viewModel.observerPopularItemLiveData().observe(viewLifecycleOwner
        ) { value -> popularItemAdapter.setMeals(value as ArrayList<MealByCategory>) }
    }

    private fun onRandomMealClick() {
        binding.cardRandomMeal.setOnClickListener{
            Intent(activity,MealActivity::class.java).also {
                it.putExtra(MEAL_ID, randomMeal.idMeal)
                it.putExtra(MEAL_NAME, randomMeal.strMeal)
                it.putExtra(MEAL_THUMP, randomMeal.strMealThumb)
                if (randomMeal.strMeal != null && randomMeal.strMealThumb !=null){
                    startActivity(it)
                }
            }

        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { value ->
            responseCase()
            Glide.with(this@HomeFragment).load(value.strMealThumb).into(binding.imgRandomMeal)
            this.randomMeal =value
        }
    }

    private fun responseCase() {
        binding.loadingBar.visibility=View.GONE
    }

    private fun prepareItemPopularRecycleView(){
        binding.rcvPopularItem.apply {
            layoutManager =LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter=popularItemAdapter
        }
    }
}