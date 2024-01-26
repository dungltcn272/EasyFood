package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.activity.MainActivity
import com.example.easyfood.activity.MealActivity
import com.example.easyfood.adapter.MealAdapter
import com.example.easyfood.databinding.FragmentSearchBinding
import com.example.easyfood.viewmodel.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var searchMealsAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycleView()

        binding.imgSearch.setOnClickListener{
            val searchQuery =binding.edtSearch.text.toString()
            if (searchQuery.isNotEmpty()){
                viewModel.searchMeals(searchQuery)
            }
        }
        observeSearchMeal()

        var searchJob: Job? = null

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    viewModel.searchMeals(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.edtSearch.addTextChangedListener(textWatcher)

        searchMealsAdapter.onItemClick ={
            val intent = Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_ID, it.idMeal)
                putExtra(HomeFragment.MEAL_NAME, it.strMeal)
                putExtra(HomeFragment.MEAL_THUMP, it.strMealThumb)
            }
            startActivity(intent)
        }

    }

    private fun observeSearchMeal() {
        viewModel.observeSearchMealLiveData().observe(viewLifecycleOwner){
            searchMealsAdapter.differ.submitList(it)
        }
    }

    private fun prepareRecycleView() {
        searchMealsAdapter = MealAdapter()
        binding.rcvListMeal.apply {
            layoutManager =GridLayoutManager(context, 2)
            adapter=searchMealsAdapter
        }
    }
}