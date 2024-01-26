package com.example.easyfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.model.Meal
import com.example.easyfood.model.MealList
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(private val mealDatabase : MealDatabase): ViewModel() {
    private var mealDetailLiveData =MutableLiveData<Meal>()

    fun getMealDetail(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body()!=null){
                    mealDetailLiveData.value=response.body()!!.meals[0]
                }
                else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("MealActivity",  t.message.toString())
            }

        })
    }

    fun observerLiveData() :LiveData<Meal>{
        return mealDetailLiveData
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

}