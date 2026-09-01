package com.abasyn.mvvmarchitecture.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.abasyn.mvvmarchitecture.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            childFragmentManager.findFragmentById(
                R.id.mainNavHostFragment
            ) as NavHostFragment

        val navController = navHostFragment.navController

        val bottomNavigation =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigation)

        // Connect Bottom Navigation with NavController
        bottomNavigation.setupWithNavController(navController)

        // Show/Hide Bottom Navigation based on destination
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                // Bottom Navigation visible
                R.id.homeFragment,
                R.id.favoriteFragment,
                R.id.settingsFragment -> {
                    bottomNavigation.isVisible = true
                }

                // Bottom Navigation hidden
                R.id.resultFragment,
                    -> {
                    bottomNavigation.isVisible = false
                }

                // Default: visible
                else -> {
                    bottomNavigation.isVisible = true
                }
            }
        }
    }
}