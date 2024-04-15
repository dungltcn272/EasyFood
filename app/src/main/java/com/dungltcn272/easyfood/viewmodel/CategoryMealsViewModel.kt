package com.dungltcn272.easyfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dungltcn272.easyfood.model.MealByCategory
import com.dungltcn272.easyfood.model.MealByCategoryList
import com.dungltcn272.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {
    var mealsLiveData =MutableLiveData<List<MealByCategory>>()

    fun getMealsByCategory(category: String){
        RetrofitInstance.api.getMealsByCategory(category).enqueue(object :Callback<MealByCategoryList>{
            override fun onResponse(
                call: Call<MealByCategoryList>,
                response: Response<MealByCategoryList>,
            ) {
                response.body()?.let {
                    mealsLiveData.postValue(it.meals)
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d("CategoryMealsViewModel", t.message.toString())
            }
        })
    }

    fun observerMealsLiveData() :LiveData<List<MealByCategory>>{
        return mealsLiveData
    }

}