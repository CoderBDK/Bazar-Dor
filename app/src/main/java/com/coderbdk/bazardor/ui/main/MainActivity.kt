package com.coderbdk.bazardor.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderbdk.bazardor.databinding.ActivityMainAppBinding
import com.coderbdk.bazardor.databinding.ContentBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var contentMain: ContentBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        contentMain = binding.appBarMain.contentMainApp.content
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        loadProductCategory()
        loadProductAdapter()

    }

    private fun loadProductCategory() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val adapter = ProductCategoryAdapter(this, viewModel.productCategoryList.value!!)
        contentMain.spinnerProductCategory.adapter = adapter

        viewModel.productCategoryList.observe(this) {
            // list update
            adapter.updateData(it)
            adapter.notifyDataSetChanged()
            //Log.i("Result","${it.size}")
        }
        contentMain.spinnerProductCategory.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    //val tv = (p0?.findViewById(R.id.name) as TextView)
                    //tv.text = tv.text.toString().split(" ")[0]
                    //tv.setTextColor(Color.GREEN)
                    //Toast.makeText(this@MainActivity,"${adapter.getItem(p2).uid}",Toast.LENGTH_SHORT).show()
                    val uid = adapter.getItem(p2).uid
                    viewModel.getProductByCategoryUID(uid)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadProductAdapter() {
        val adapter = ProductAdapter(this, viewModel.productList.value!!)
        val recyclerView = contentMain.recyclerView
        recyclerView.adapter = adapter
        val lm = LinearLayoutManager(this)
        lm.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = lm

        viewModel.productList.observe(this) {
            adapter.updateData(it)
            adapter.notifyDataSetChanged()
        }

    }
}