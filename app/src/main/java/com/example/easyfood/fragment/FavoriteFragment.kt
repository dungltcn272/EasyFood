package com.example.easyfood.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.activity.MainActivity
import com.example.easyfood.activity.MealActivity
import com.example.easyfood.adapter.MealAdapter
import com.example.easyfood.databinding.FragmentFavoriteBinding
import com.example.easyfood.viewmodel.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoriteFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecycleView()
        observeFavorites()
        onFavoriteMealClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deleteMeal = mealAdapter.differ.currentList[position]
                viewModel.deleteMeal(deleteMeal)
                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_SHORT)
                    .setAction("UNDO"
                    ) {
                        viewModel.insertMeal(deleteMeal)
                    }
                    .show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rcvMeal)
    }

    private fun onFavoriteMealClick() {
        mealAdapter.onItemClick = {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_NAME, it.strMeal)
            intent.putExtra(HomeFragment.MEAL_ID, it.idMeal)
            intent.putExtra(HomeFragment.MEAL_THUMP, it.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecycleView() {
        mealAdapter = MealAdapter()
        binding.rcvMeal.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = mealAdapter
        }
    }

    private fun observeFavorites() {
        viewModel.observeFactoryMealLiveData().observe(requireActivity()) {
            it?.let {
                mealAdapter.differ.submitList(it)
            }
        }
    }
}