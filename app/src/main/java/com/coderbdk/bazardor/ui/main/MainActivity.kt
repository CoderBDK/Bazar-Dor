package com.coderbdk.bazardor.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderbdk.bazardor.databinding.ActivityMainAppBinding
import com.coderbdk.bazardor.databinding.ContentBinding
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var contentMain: ContentBinding
    private lateinit var viewModel: MainViewModel
    private val dialogNetworkAlert by lazy { NetworkAlertDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        contentMain = binding.appBarMain.contentMainApp.content
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        onInit()

    }

    private fun onInit() {
        loadProductCategory()
        loadProductAdapter()
        loadNetworkResponseListener()
        loadDialogNetwork()
    }

    private fun loadDialogNetwork() {
        dialogNetworkAlert.addNetworkDialogListener(object :
            NetworkAlertDialog.NetworkDialogListener {
            override fun onPositiveEvent() {
                viewModel.retry()
            }

            override fun onNegativeEvent() {
                startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            }

        })
    }

    private fun loadNetworkResponseListener() {
        lifecycleScope.launch {
            viewModel.responseState.collect {
                when (it.second) {
                    MainViewModel.ResponseState.INITIAL -> {
                        showLoader()
                    }

                    MainViewModel.ResponseState.ACCEPTED -> {
                        dialogNetworkAlert.hide()
                        hideLoader()
                    }

                    MainViewModel.ResponseState.FAILED -> {
                        dialogNetworkAlert.show(it.first)
                        hideLoader()
                    }
                }
            }
        }
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


    fun showLoader() {
        if (!binding.appBarMain.universalLoaderIndicator.isVisible) binding.appBarMain.universalLoaderIndicator.isVisible =
            true
    }

    fun hideLoader() {
        if (binding.appBarMain.universalLoaderIndicator.isVisible) binding.appBarMain.universalLoaderIndicator.isVisible =
            false
    }
}