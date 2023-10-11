package com.coderbdk.bazardor.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.coderbdk.bazardor.R
import com.coderbdk.bazardor.databinding.ActivityMainAppBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_app)
        val binding = ActivityMainAppBinding.inflate(layoutInflater)
        setSupportActionBar(binding.appBarMain.toolbar)

    }
}