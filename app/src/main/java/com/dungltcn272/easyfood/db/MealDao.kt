package com.dungltcn272.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dungltcn272.easyfood.model.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMeal(meal: Meal)

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM mealsInformation")
    fun getAllMeals() : LiveData<List<Meal>>
}