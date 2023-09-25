package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activity.CategoryMealsActivity
import com.example.easyfood.activity.MainActivity
import com.example.easyfood.adapter.CategoryAdapter
import com.example.easyfood.databinding.FragmentCategoriesBinding
import com.example.easyfood.viewmodel.HomeViewModel
class CategoriesFragment : Fragment() {
    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var viewModel : HomeViewModel

    private lateinit var categoriesAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecycleView()
        observeCategories()
        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {
            val intent =Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGORY_NAME, it.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        viewModel.observerCategoriesLiveData().observe(viewLifecycleOwner){
            categoriesAdapter.setCategoryList(it)
        }
    }

    private fun prepareRecycleView() {
        categoriesAdapter = CategoryAdapter()
        binding.rcvCategories.apply {
            layoutManager =GridLayoutManager(context, 3)
            adapter=categoriesAdapter
        }
    }
}