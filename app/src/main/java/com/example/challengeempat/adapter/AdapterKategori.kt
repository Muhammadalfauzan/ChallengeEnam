package com.example.challengeempat.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengeempat.R
import com.example.challengeempat.model.DataCategory


class AdapterKategori(private var menuList: ArrayList<DataCategory>) :
    RecyclerView.Adapter<AdapterKategori.KategoriViewHolder>() {

    class KategoriViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuImage: ImageView = itemView.findViewById(R.id.iv_kategori)
        val menuName: TextView = itemView.findViewById(R.id.tv_kategori)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_kat, parent, false)
        return KategoriViewHolder(view)

    }

    override fun onBindViewHolder(holder: KategoriViewHolder, position: Int) {
        val menuItem = menuList[position]

        // Set data ke tampilan dalam item
        holder.menuName.text = menuItem.nama
        Glide.with(holder.itemView.context).load(menuItem.imageUrl).into(holder.menuImage)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateDataKat(newData: List<DataCategory>) {
        menuList.clear()
        menuList.addAll(newData)
        notifyDataSetChanged()
    }


}