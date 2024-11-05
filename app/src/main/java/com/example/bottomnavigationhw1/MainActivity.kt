package com.example.bottomnavigationhw1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bottomnavigationhw1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_container)
        val appBarConfiguration = AppBarConfiguration (
            setOf(
                R.id.mainFragment,
                R.id.notesFragment,
                R.id.weatherFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.bottomNavigationView.setupWithNavController(navController)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}