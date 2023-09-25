package com.example.easyfood.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.pojo.*
import com.example.easyfood.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val mealDatabase: MealDatabase) : ViewModel(){

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularListLiveData =MutableLiveData<List<MealByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchMealLiveData =MutableLiveData<List<Meal>>()

    private var factoryMealLiveData = mealDatabase.mealDao().getAllMeals()


    fun searchMeals(searchQuery: String){
        RetrofitInstance.api.searchMeal(searchQuery).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList =response.body()?.meals
                mealsList?.let {
                    searchMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun observeSearchMealLiveData() : LiveData<List<Meal>> = searchMealLiveData

    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetail(id).enqueue(object :Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal =response.body()?.meals?.first()
                meal?.let {
                    bottomSheetMealLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })

    }
    fun observeBottomSheetMealLiveData() : LiveData<Meal> = bottomSheetMealLiveData

    fun observeFactoryMealLiveData(): LiveData<List<Meal>>{
        return factoryMealLiveData
    }

    private var saveStateRandomMeal: Meal?=null
    fun getRandomMeal(){
        saveStateRandomMeal?.let {
            randomMealLiveData.postValue(it)
            return
        }

        RetrofitInstance.api.getRandomMeal().enqueue(object  : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() !=null){
                    val randomMeal=response.body()!!.meals[0]
                    randomMealLiveData.value=randomMeal
                    saveStateRandomMeal=randomMeal
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }

        })
    }

    fun observeRandomMealLiveData():LiveData<Meal>{
        return randomMealLiveData
    }

    fun getPopularItem(){
        RetrofitInstance.api.getPopularItem("Seafood").enqueue(object : Callback<MealByCategoryList>{
            override fun onResponse(call: Call<MealByCategoryList>, response: Response<MealByCategoryList>) {
                if(response.body()!=null){
                    popularListLiveData.value=response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealByCategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })

    }

    fun observerPopularItemLiveData(): LiveData<List<MealByCategory>>{
        return popularListLiveData
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if(response.body()!=null){
                    response.body()?.let {
                        categoriesLiveData.postValue(it.categories)
                    }
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }

        })
    }
    fun observerCategoriesLiveData() : LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }


}