package com.dungltcn272.easyfood.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dungltcn272.easyfood.R
import com.dungltcn272.easyfood.databinding.ActivityMealBinding
import com.dungltcn272.easyfood.db.MealDatabase
import com.dungltcn272.easyfood.fragment.HomeFragment
import com.dungltcn272.easyfood.model.Meal
import com.dungltcn272.easyfood.viewmodel.MealViewModel
import com.dungltcn272.easyfood.viewmodel.MealViewModelFactory


class MealActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMealBinding
    private lateinit var mealId : String
    private lateinit var mealName : String
    private lateinit var mealThump : String

    private lateinit var mealMvvm : MealViewModel
    private lateinit var youtubeLink : String

    private var mealToSave : Meal? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mealDatabase =MealDatabase.getInstance(this)
        val viewModelFactory =MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory)[MealViewModel::class.java]

        loadingCase()

        getMealInformationFromIntent()
        setInformationInViews()
        mealMvvm.getMealDetail(mealId)
        observerMealDetailLiveData()
        onYoutubeImageClick()

        onFavoriteClick()
    }

    private fun onFavoriteClick() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                Toast.makeText(this, "Favorite added", Toast.LENGTH_SHORT).show()
                mealMvvm.insertMeal(mealToSave!!)
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.btnYoutube.setOnClickListener{
            val intentToYoutube = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intentToYoutube)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observerMealDetailLiveData() {
        mealMvvm.observerLiveData().observe(this
        ) { value ->
            onResponseCase()
            mealToSave=value
            binding.tvCategory.text = "Category : ${value.strCategory}"
            binding.tvArea.text = "Area : ${value.strArea}"
            binding.tvInstruction.text = value.strInstructions
            youtubeLink = value.strYoutube!!
        }
    }

    @Suppress("DEPRECATION")
    private fun setInformationInViews() {
        Glide.with(applicationContext).load(mealThump).into(binding.imgMealDetail)
        binding.collapsingToolbar.title=mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent=intent
        mealId =intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName =intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThump =intent.getStringExtra(HomeFragment.MEAL_THUMP)!!
    }

    private fun loadingCase(){
        binding.indicator.visibility = View.VISIBLE
        binding.btnAddToFav.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvInstruction.visibility=View.INVISIBLE
        binding.btnYoutube.visibility = View.INVISIBLE
    }
    private fun onResponseCase(){
        binding.indicator.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstruction.visibility=View.VISIBLE
        binding.btnYoutube.visibility = View.VISIBLE
    }
}