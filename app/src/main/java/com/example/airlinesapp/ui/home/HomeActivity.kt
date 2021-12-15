package com.example.airlinesapp.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.airlinesapp.HomeNavGraphDirections
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityHomeBinding
import com.example.airlinesapp.ui.home.airlines.addAirline.AddAirlineActivity
import com.example.airlinesapp.ui.home.passengers.addPassenger.AddPassengerActivity
import com.example.airlinesapp.util.Constants.AIRLINE_RESULT_CODE
import com.example.airlinesapp.util.Constants.PASSENGER_RESULT_CODE
import com.google.android.material.snackbar.Snackbar

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    lateinit var launcher: ActivityResultLauncher<Intent>
    private set
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        navController = Navigation.findNavController(this, R.id.hostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        setupAddFloatingButton()
        launchActivity()
    }

    private fun setupAddFloatingButton() {
        binding.floatingActionButton.setOnClickListener {
            if (navController.currentDestination?.label.toString() == "fragment_airlines") {
                launcher.launch(AddAirlineActivity.newIntent(this))
            } else {
                launcher.launch(AddPassengerActivity.newIntent(this))
            }
        }
    }

    private fun launchActivity() {
        launcher = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            if (result.resultCode == PASSENGER_RESULT_CODE) {
                navController.navigate(HomeNavGraphDirections.actionGlobalPassengersFragment())
                val intent = result.data
                Snackbar.make(
                    binding.constraintLayout,
                    intent?.getStringExtra(EXTRA_HOME).toString(),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.successGreen
                    )
                )
                    .show()
            } else if (result.resultCode == AIRLINE_RESULT_CODE) {
                navController.navigate(HomeNavGraphDirections.actionGlobalAirlinesFragment())
                val intent = result.data
                Snackbar.make(
                    binding.constraintLayout,
                    intent?.getStringExtra(EXTRA_HOME).toString(),
                    Snackbar.LENGTH_LONG
                ).setBackgroundTint(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.successGreen
                    )
                )
                    .show()
            }
        }
    }

    companion object {

        private val EXTRA_HOME =
            HomeActivity::class.java.name + "_HOME_EXTRA"

        fun newIntentWithStringExtra(context: Context, stringExtra: String) =
            Intent(context, HomeActivity::class.java)
                .putExtra(EXTRA_HOME, stringExtra)
    }
}