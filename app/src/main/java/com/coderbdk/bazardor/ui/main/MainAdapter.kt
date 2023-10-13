package com.coderbdk.bazardor.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.coderbdk.bazardor.databinding.ItemProductListBinding

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainHolder>() {

    data class ProductData(
        val name: String,
        val price: Float
    )

    private val list = arrayOf(
        ProductData("Alu",100f),
        ProductData("Begun",10f),
        ProductData("Deros",300f),
        ProductData("Morich",10f),
        ProductData("Kola",500f)
    )
    class MainHolder(val binding: ItemProductListBinding): ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
       return MainHolder(ItemProductListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val binding = holder.binding
        val data = list[position]
        binding.name.text = data.name
        binding.price.text = "price: ${data.price}"
    }
}