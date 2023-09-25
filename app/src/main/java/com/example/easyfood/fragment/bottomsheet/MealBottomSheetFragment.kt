package com.example.easyfood.fragment.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.easyfood.activity.MainActivity
import com.example.easyfood.activity.MealActivity
import com.example.easyfood.databinding.FragmentMealBottomSheetBinding
import com.example.easyfood.fragment.HomeFragment
import com.example.easyfood.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

private const val MEAL_ID = "param1"

class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding : FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel

    private var mealId: String? = null
    private var mealName: String ?=null
    private var mealThump: String ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel =(activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMealBottomSheetBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }
        viewModel.observeBottomSheetMealLiveData().observe(viewLifecycleOwner){
            Glide.with(this).load(it.strMealThumb).into(binding.imgMeal)
            binding.tvArea.text = it.strArea
            binding.tvCategory.text =it.strCategory
            binding.tvMealName.text =it.strMeal
            mealName=it.strMeal
            mealThump=it.strMealThumb
        }

        onMealBottomSheetClick()
    }
    private fun onMealBottomSheetClick() {
        binding.bottomSheet.setOnClickListener {
            val intent =Intent(activity, MealActivity::class.java)
            intent.apply {
                putExtra(HomeFragment.MEAL_NAME, mealName)
                putExtra(HomeFragment.MEAL_ID, mealId)
                putExtra(HomeFragment.MEAL_THUMP, mealThump)
            }
            startActivity(intent)

        }
    }

    companion object {
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}