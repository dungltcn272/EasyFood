package com.dungltcn272.easyfood.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.dungltcn272.easyfood.R
import com.dungltcn272.easyfood.db.MealDatabase
import com.dungltcn272.easyfood.viewmodel.HomeViewModel
import com.dungltcn272.easyfood.viewmodel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    val viewModel : HomeViewModel by lazy {
        val mealDatabase = MealDatabase.getInstance(this)
        val homeViewModelFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        val bottomNav =findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navController =Navigation.findNavController(this, R.id.nav_host)
        NavigationUI.setupWithNavController(bottomNav, navController)
    }
}