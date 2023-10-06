package com.example.challengeempat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.challengeempat.dataclass.ListData
import com.example.challengeempat.R


class AdapterHome(
    private val dataMenu: ArrayList<ListData>,
    var isGrid: Boolean = true,
    private var onItemClick: ((ListData) -> Unit)? = null
) : RecyclerView.Adapter<AdapterHome.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutView = if (isGrid) R.layout.item_grid else R.layout.item_list
        val view: View = LayoutInflater.from(parent.context).inflate(layoutView, parent, false)
        return ListViewHolder(view)
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (gambar, namaImg, harga) = dataMenu[position]
        holder.imgMenuu.setImageResource(gambar)
        holder.namaMenu.text = namaImg
        holder.harga.text = harga.toString()

        val currentItem = dataMenu[position]
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)
        }
    }
    override fun getItemCount(): Int = dataMenu.size


    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgMenuu: ImageView = itemView.findViewById(R.id.imgMenu)
        val namaMenu: TextView = itemView.findViewById(R.id.tvNamaImg)
        val harga: TextView = itemView.findViewById(R.id.tvHarga)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<ListData>) {
        dataMenu.clear()
        dataMenu.addAll(newData)
        notifyDataSetChanged()
    }


}


