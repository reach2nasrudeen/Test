package com.interview.test.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.interview.test.R
import com.interview.test.databinding.ActivityMainBinding
import com.interview.test.model.CardsList
import com.interview.test.utils.getDrawableRes
import com.interview.test.utils.getObjectFromJson
import com.interview.test.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private var previousSelectedItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.fab.setOnClickListener {
            if (previousSelectedItem?.itemId == R.id.navigation_card_listing) {
                navController.navigate(R.id.navigation_add_card)
            } else {
                // No-Op
            }
        }
        (binding.navHostFragment.getFragment() as? NavHostFragment)?.navController?.let {
            navController = it
            binding.bottomNavigationView.setupWithNavController(navController)
        }

        setupBottomNavigation()
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.showBottomBar.observe(this) {
            binding.bottomAppBar.isVisible = it
            binding.fab.isVisible = it
        }


        lifecycleScope.launch {
            getObjectFromJson<CardsList>("cards.json").let {
                viewModel.updateCards(it)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigationView.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }

        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            binding.fab.setImageDrawable(getDrawableRes(if (item.itemId == R.id.navigation_card_listing) R.drawable.ic_add else R.drawable.ic_vd_send))

            when (item.itemId) {
                R.id.navigation_statistics -> {
                    Toast.makeText(this, "Stats not in scope", Toast.LENGTH_SHORT).show()
                    previousSelectedItem?.let {
                        NavigationUI.onNavDestinationSelected(it, navController)
                    }
                }

                R.id.navigation_profile -> {
                    Toast.makeText(this, "Profile not in scope", Toast.LENGTH_SHORT).show()
                }

                else -> NavigationUI.onNavDestinationSelected(item, navController)
            }
            this.previousSelectedItem = item

            true
        }
    }
}