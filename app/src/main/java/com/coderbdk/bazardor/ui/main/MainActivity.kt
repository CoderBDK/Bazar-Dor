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
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.coderbdk.bazardor.R
import com.coderbdk.bazardor.databinding.ActivityMainAppBinding
import com.coderbdk.bazardor.databinding.ContentBinding
import com.coderbdk.bazardor.databinding.DialogDatePickerBinding
import com.coderbdk.bazardor.di.local.room.AppDatabase
import com.coderbdk.bazardor.di.local.shared.main.MainPrefs
import com.coderbdk.bazardor.di.repository.MainRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAppBinding
    private lateinit var contentMain: ContentBinding
    private var isNightMode = false
    private val dialogNetworkAlert by lazy { NetworkAlertDialog(this) }
    private val appDB by lazy { AppDatabase.getInstance(this)!! }
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(MainRepository(appDB.productCategoryDao(), appDB.productDao()))
    }
    private val mainPrefs by lazy { MainPrefs(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAppBinding.inflate(layoutInflater)
        contentMain = binding.appBarMain.contentMainApp.content
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        onInit()

        //appDB.productCategoryDao().insertProductCategory(ProductCategoryEntity(103,"name","url"))
    }

    private fun onInit() {
        loadProductCategory()
        loadProductAdapter()
        loadNetworkResponseListener()
        loadDialogNetwork()
        loadDatePicker()
        checkThemeMode()
    }

    private fun loadDatePicker() {
        val calender = Calendar.getInstance()
        val bindingDatePicker = DialogDatePickerBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.pick_date))
            .setView(bindingDatePicker.root)
            .setCancelable(false)
            .setNegativeButton("Cancel"){_,_->}
            .setPositiveButton("Confirm"){_,_->

                calender.set(
                    bindingDatePicker.datePicker.year,
                    bindingDatePicker.datePicker.month,
                    bindingDatePicker.datePicker.dayOfMonth
                )
                val timeMillis: Long = calender.timeInMillis

                viewModel.getProductByCategoryUID(
                    contentMain.spinnerProductCategory.selectedItemId,
                    viewModel.repository.formatTime(timeMillis)
                )
            }.create()
        contentMain.datePick.setOnClickListener {
            dialog.show()
        }
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
                        if (!contentMain.productLoading.isVisible) contentMain.productLoading.isVisible =
                            true
                        if (contentMain.recyclerView.isVisible) contentMain.recyclerView.isVisible =
                            false
                        showLoader()
                    }

                    MainViewModel.ResponseState.ACCEPTED -> {
                        dialogNetworkAlert.hide()
                        hideLoader()
                        if (contentMain.productLoading.isVisible) contentMain.productLoading.isVisible =
                            false
                        if (!contentMain.recyclerView.isVisible) contentMain.recyclerView.isVisible =
                            true
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
        Snackbar.make(this, binding.appBarMain.appBar, message, Snackbar.LENGTH_SHORT)
            .setBackgroundTint(color)
            .show()
    }


    private fun loadProductCategory() {
        // viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val adapter = ProductCategoryAdapter(this, viewModel.productCategoryList.value!!)
        contentMain.spinnerProductCategory.adapter = adapter

        var onLoad = true
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

                    viewModel.getProductByCategoryUID(
                        uid,
                        viewModel.repository.formatTime(System.currentTimeMillis())
                    )
                    mainPrefs.putItemPosition(p2)

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
        contentMain.spinnerProductCategory.post {
            contentMain.spinnerProductCategory.setSelection(mainPrefs.getItemPosition())
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