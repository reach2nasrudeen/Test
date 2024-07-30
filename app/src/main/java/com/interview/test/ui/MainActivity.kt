package com.interview.test.ui

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.interview.test.R
import com.interview.test.databinding.ActivityMainBinding
import com.interview.test.utils.getDrawableRes
import com.interview.test.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val viewModel: HomeViewModel by viewModel<HomeViewModel>()
    private var previousSelectedItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.bottomNavigationView.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }

        binding.fab.setOnClickListener {
//            Toast.makeText(this, "Test Action", Toast.LENGTH_SHORT).show()
            if (previousSelectedItem?.itemId == R.id.navigation_card_listing) {
                navController.navigate(R.id.navigation_add_card)
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Test 2", Toast.LENGTH_SHORT).show()
            }
        }
        (binding.navHostFragment.getFragment() as? NavHostFragment)?.navController?.let {
            navController = it
            binding.bottomNavigationView.setupWithNavController(navController)
        }

        viewModel.showBottomBar.observe(this) {
            binding.bottomAppBar.isVisible = it
            binding.fab.isVisible = it
        }


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val currentSelectedItem = item.itemId

            Timber.e("came here")

//            if (this.previousSelectedItem != currentSelectedItem) {
//                navController.popBackStack(previousSelectedItem, false)
//            }

//
//            // Clear the back stack by popping to the selected destination
//            navController.popBackStack(item.itemId, false)
            binding.fab.setImageDrawable(getDrawableRes(if (item.itemId == R.id.navigation_card_listing) R.drawable.ic_add else R.drawable.ic_vd_send))

            when (item.itemId) {

                R.id.navigation_action -> {
                    if (item.itemId == R.id.navigation_card_listing) {
//                        viewModel.updateBottomBar(false)
                        navController.navigate(R.id.navigation_add_card)
                        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Test 2", Toast.LENGTH_SHORT).show()
                    }
                    previousSelectedItem?.let {
                        NavigationUI.onNavDestinationSelected(
                            it,
                            navController
                        )
                    }
                }

                R.id.navigation_statistics -> {
                    Toast.makeText(this, "Test Stats", Toast.LENGTH_SHORT).show()
                    previousSelectedItem?.let {
                        NavigationUI.onNavDestinationSelected(
                            it,
                            navController
                        )
                    }
                }

                R.id.navigation_profile -> {
                    Toast.makeText(this, "Test Profile", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    NavigationUI.onNavDestinationSelected(item, navController)
                }
            }
            this.previousSelectedItem = item

            true
        }
    }
}