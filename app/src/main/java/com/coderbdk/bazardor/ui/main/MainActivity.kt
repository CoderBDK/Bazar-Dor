package com.coderbdk.bazardor.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var contentMain: ContentBinding
    private lateinit var viewModel: MainViewModel
    private var isNightMode = false
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
        checkThemeMode()
    }
    private fun checkThemeMode() {

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                isNightMode = false
                if (Build.VERSION.SDK_INT <= 23) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            } // Night mode is not active, we're using the light theme.
            Configuration.UI_MODE_NIGHT_YES -> {
                isNightMode = true
            } // Night mode is active, we're using dark theme.
        }
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
                        if(!contentMain.productLoading.isVisible) contentMain.productLoading.isVisible = true
                        if(contentMain.recyclerView.isVisible) contentMain.recyclerView.isVisible = false
                        showLoader()
                    }

                    MainViewModel.ResponseState.ACCEPTED -> {
                        dialogNetworkAlert.hide()
                        hideLoader()
                        if(contentMain.productLoading.isVisible)contentMain.productLoading.isVisible = false
                        if(!contentMain.recyclerView.isVisible)contentMain.recyclerView.isVisible = true
                    }

                    MainViewModel.ResponseState.FAILED -> {
                        dialogNetworkAlert.show(it.first)
                        hideLoader()
                    }
                }
            }
        }
    }

    private fun showSnackBar(message: String, color: Int) {
        Snackbar.make(this,binding.appBarMain.appBar,message,Snackbar.LENGTH_SHORT)
            .setBackgroundTint(color)
            .show()
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