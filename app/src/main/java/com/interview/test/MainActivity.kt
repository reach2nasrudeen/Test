package com.interview.test

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.interview.test.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private var previousSelectedItem: Int = R.id.navigation_dashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.bottomNavigationView.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }

        binding.fab.setOnClickListener {
            Toast.makeText(this, "Test Action", Toast.LENGTH_SHORT).show()
        }
        (binding.navHostFragment.getFragment() as? NavHostFragment)?.navController?.let {
            navController = it
            binding.bottomNavigationView.setupWithNavController(navController)
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentSelectedItem = item.itemId

//            if (this.previousSelectedItem != currentSelectedItem) {
//                navController.popBackStack(previousSelectedItem, false)
//            }

//
//            // Clear the back stack by popping to the selected destination
//            navController.popBackStack(item.itemId, false)

            when (item.itemId) {
                R.id.navigation_action -> {
                    Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_statistics -> {
                    Toast.makeText(this, "Test Stats", Toast.LENGTH_SHORT).show()
                }
                R.id.navigation_profile -> {
                    Toast.makeText(this, "Test Profile", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
            this.previousSelectedItem = currentSelectedItem

            true
        }
    }
}