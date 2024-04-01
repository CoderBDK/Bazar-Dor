package com.coderbdk.bazardor.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.coderbdk.bazardor.R
import com.coderbdk.bazardor.databinding.ItemProductCategoryListBinding
import com.coderbdk.bazardor.data.network.main.ProductCategory

class ProductCategoryAdapter(
    private val context: Context,
    private var list: List<ProductCategory>
) : BaseAdapter() {

    private val _list: List<ProductCategory> = listOf(
        ProductCategory(10, "a",""),
        ProductCategory(12, "ab",""),
        ProductCategory(13, "abc","")
    )

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): ProductCategory {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return list[p0].uid
    }

    fun updateData(list: List<ProductCategory>) {
        this.list = list
    }


    override fun getView(p0: Int, converterView: View?, p2: ViewGroup?): View {
        var view = converterView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_product_category_list, p2,false)
        }
        val binding = ItemProductCategoryListBinding.bind(view!!)
        val data = getItem(p0)
        binding.name.text = data.name

        return view
    }

}