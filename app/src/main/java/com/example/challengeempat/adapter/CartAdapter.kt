package com.example.challengeempat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.challengeempat.database.CartData
import com.example.challengeempat.databinding.ItemCartBinding
import com.example.challengeempat.viewmodel.CartViewModel


class CartAdapter(
    private var cartItems: List<CartData>,
    private val viewModel: CartViewModel,
    private val isEditable: Boolean = true
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cartItem: CartData) {
            binding.imgMenuCart.setImageResource(cartItem.imageFood)
            binding.tvNamaImgCart.text = cartItem.nameFood
            binding.txtCartHarga.text = cartItem.hargaPerItem.toString()
            binding.quantityTextView.text = cartItem.quantity.toString()
            binding.tvCatatan.text = cartItem.note

            if (isEditable) {
                binding.btnPlusCart.visibility = View.VISIBLE
                binding.btnMinCart.visibility = View.VISIBLE
                binding.btnDelete.visibility = View.VISIBLE

            } else {
                binding.btnPlusCart.visibility = View.GONE
                binding.btnMinCart.visibility = View.GONE
                binding.btnDelete.visibility = View.GONE
            }

            binding.btnPlusCart.setOnClickListener {
                if (isEditable) {
                    val currentItem = cartItems[adapterPosition]
                    val newQuantity = currentItem.quantity + 1
                    viewModel.updateQuantity(currentItem, newQuantity)
                }
            }
            binding.btnMinCart.setOnClickListener {
                if (isEditable) {
                    val currentItem = cartItems[adapterPosition]
                    if (currentItem.quantity > 1) {
                        val newQuantity = currentItem.quantity - 1
                        viewModel.updateQuantity(currentItem, newQuantity)
                    }
                }
            }

            binding.btnDelete.setOnClickListener {
                if (isEditable) {
                    val currentItem = cartItems[adapterPosition]
                    viewModel.deleteCartItem(currentItem)
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCartBinding.inflate(layoutInflater, parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentItem = cartItems[position]
        holder.bind(currentItem)

    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataCart(newData: List<CartData>) {
        cartItems = newData
        notifyDataSetChanged()
    }
}
