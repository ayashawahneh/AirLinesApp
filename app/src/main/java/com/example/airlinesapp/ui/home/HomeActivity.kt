package com.example.airlinesapp.ui.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.airlinesapp.R
import com.example.airlinesapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.bottomNavigationView.setupWithNavController(findNavController(R.id.hostFragment))
    }

    companion object {

        private val EXTRA_Home =
            HomeActivity::class.java.name + "_Home_EXTRA"

        fun newIntentWithStringExtra(context: Context, name: String) =
            Intent(context, HomeActivity::class.java)
                .putExtra(EXTRA_Home,name)
    }
}