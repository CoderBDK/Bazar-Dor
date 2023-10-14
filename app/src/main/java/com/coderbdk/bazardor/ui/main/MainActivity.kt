package com.coderbdk.bazardor.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderbdk.bazardor.databinding.ActivityMainAppBinding
import com.coderbdk.bazardor.databinding.ContentMainAppBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var contentMain: ContentMainAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        contentMain = binding.appBarMain.contentMainApp
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        loadProductCategory()
        loadAdapter()
    }

    private fun loadProductCategory() {
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val adapter = ProductCategoryAdapter(this, viewModel.productCategoryList.value!!)
        contentMain.spinnerProductCategory.adapter = adapter

        viewModel.productCategoryList.observe(this) {
            // list update
            adapter.updateData(it)
            adapter.notifyDataSetChanged()
            //Log.i("Result","${it.size}")

        }
    }

    private fun loadAdapter() {
        val adapter = MainAdapter()
        val recyclerView = binding.appBarMain.contentMainApp.recyclerView
        recyclerView.adapter = adapter
        val lm = LinearLayoutManager(this)
        lm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = lm
    }
}