package com.example.android.politicalpreparedness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //initialize Timber
        Timber.plant(Timber.DebugTree())

        //find NavController
        val navController = findNavController(R.id.nav_host_fragment)

        //tie things up
        NavigationUI.setupActionBarWithNavController(this, navController)
    }


    override fun onSupportNavigateUp(): Boolean {
        //find NavController
        val navController = findNavController(R.id.nav_host_fragment)

        //this is what happens when the up button is clicked
        return navController.navigateUp()
    }
}
