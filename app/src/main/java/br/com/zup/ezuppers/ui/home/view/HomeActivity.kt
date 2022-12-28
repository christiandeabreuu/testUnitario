package br.com.zup.ezuppers.ui.home.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.com.zup.ezuppers.R
import br.com.zup.ezuppers.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(navController)

        binding.bottomNavigation.setupWithNavController(navController)

        binding.bottomNavigation.setOnItemReselectedListener { item ->
            val reselectedDestinationId = item.itemId
            navController.popBackStack(reselectedDestinationId, inclusive = false)
        }

        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id != R.id.loginFragment || nd.id != R.id.registerOptionalFragment || nd.id != R.id.registerLoginFragment) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}