package com.coderbdk.bazardor.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderbdk.bazardor.R
import com.coderbdk.bazardor.databinding.ActivityMainAppBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        loadAdapter()
    }

    private fun loadAdapter() {
        val adapter = MainAdapter()
        var recyclerView = binding.appBarMain.contentMainApp.recyclerView
        recyclerView.adapter = adapter
        var lm = LinearLayoutManager(this)
        lm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = lm
    }
}