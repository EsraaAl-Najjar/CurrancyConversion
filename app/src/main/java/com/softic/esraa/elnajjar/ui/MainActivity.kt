package com.softic.esraa.elnajjar.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.softic.esraa.elnajjar.R
import com.softic.esraa.elnajjar.databinding.ActivityMainBinding
import com.softic.esraa.elnajjar.utils.Constants.Companion.ACCESS_KEY
import com.softic.esraa.elnajjar.utils.PreferencesUtility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var navHostFragment: NavHostFragment? = null
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //startUi
        setupUI()
        setAccessKeyToSharedPref()

    }

    private fun setAccessKeyToSharedPref() {

        PreferencesUtility.setString(this,ACCESS_KEY,"3ceeb2e38bd289e7fa0b89f98900e740" )
    }

    private fun setupUI() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment?
        navController = navHostFragment?.navController
        navController!!.setGraph(R.navigation.mobile_navigation)
        navController!!.addOnDestinationChangedListener { _: NavController?, _: NavDestination?, _: Bundle? -> }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        super.onBackPressed()
       TODO( /* delete preference */)
    }
}