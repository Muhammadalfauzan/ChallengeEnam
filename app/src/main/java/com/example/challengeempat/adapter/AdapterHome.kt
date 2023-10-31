package com.example.challengeempat.adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengeempat.databinding.ItemGridBinding
import com.example.challengeempat.databinding.ItemListBinding
import com.example.challengeempat.model.Data


class AdapterHome(
    private var menuList: ArrayList<Data>,
    var isGrid: Boolean = true,
    private val onItemClick: ((Data) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_GRID = 1
        private const val VIEW_TYPE_LIST = 2
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: List<Data>) {
        menuList.clear()
        menuList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_GRID) {
            val binding = ItemGridBinding.inflate(layoutInflater, parent, false)
            GridViewHolder(binding)
        } else {
            val binding = ItemListBinding.inflate(layoutInflater, parent, false)
            ListViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = menuList[position]
        when (holder) {
            is GridViewHolder -> {
                holder.bindGrid(currentItem)
            }
            is ListViewHolder -> {
                holder.bindList(currentItem)
            }
        }
    }

    override fun getItemCount(): Int = menuList.size

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) VIEW_TYPE_GRID else VIEW_TYPE_LIST
    }

    inner class GridViewHolder(private val binding: ItemGridBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindGrid(postResponse: Data) {
            binding.tvNamaImg.text = postResponse.nama
            binding.tvHarga.text =postResponse.hargaFormat
            Glide.with(binding.root.context)
                .load(postResponse.imageUrl)
                .into(binding.imgMenu)

            binding.root.setOnClickListener {
                onItemClick?.invoke(postResponse)
            }
        }
    }
    inner class ListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindList(postResponse: Data) {
            binding.tvNamaImg.text = postResponse.nama
            binding.tvHarga.text = postResponse.hargaFormat
            Glide.with(binding.root.context)
                .load(postResponse.imageUrl)
                .into(binding.imgMenu)


            binding.root.setOnClickListener {
                onItemClick?.invoke(postResponse)
            }
        }
    }
}
