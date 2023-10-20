package com.coderbdk.bazardor.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.coderbdk.bazardor.R
import com.coderbdk.bazardor.databinding.ItemProductListBinding
import com.coderbdk.bazardor.di.remote.main.Product

class ProductAdapter(val context: Context,private var list: List<Product>): RecyclerView.Adapter<ProductAdapter.MainHolder>() {


    /*private val _list = arrayOf(
        Product("Alu",100f,"kg",""),
        Product("Begun",10f,"kg",""),
        Product("Deros",300f,"kg",""),
        Product("Morich",10f,"kg",""),
        Product("Kola",500f,"kg","")
    )*/
    class MainHolder(val binding: ItemProductListBinding): ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
       return MainHolder(ItemProductListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }


    @SuppressLint("StringFormatMatches")
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val binding = holder.binding
        val data = list[position]
        binding.name.text = data.name
        binding.price.text = context.getString(R.string.price,data.price,data.priceUnit)
    }

    fun updateData(list: List<Product>) {
        this.list = list
    }
}