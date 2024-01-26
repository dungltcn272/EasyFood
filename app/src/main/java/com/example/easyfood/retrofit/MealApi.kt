package com.example.easyfood.retrofit

import com.example.easyfood.model.CategoryList
import com.example.easyfood.model.MealByCategoryList
import com.example.easyfood.model.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php")
    fun getMealDetail(@Query("i") id: String ): Call<MealList>

    @GET("filter.php")
    fun getPopularItem(@Query("c") categoryName: String): Call<MealByCategoryList>

    @GET("categories.php")
    fun getCategories() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String): Call<MealByCategoryList>

    @GET("search.php")
    fun searchMeal(@Query("s") searchQuery: String): Call<MealList>
}