package com.example.challengeempat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengeempat.database.CartData
import com.example.challengeempat.databinding.ItemCartBinding
import com.example.challengeempat.viewmodel.CartViewModel

@Suppress("DEPRECATION", "UNUSED_EXPRESSION")
class CartAdapter(
    private val viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<CartData> = emptyList()

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val etNote = binding.tvCatatan

        init {
            etNote.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etNote.clearFocus()
                    val currentItem = cartItems[adapterPosition]
                    currentItem.note = etNote.text.toString().trim()
                    viewModel.updateNote(currentItem, etNote.text.toString().trim())
                    true
                }
                false
            }
        }


        fun bind(cartItem: CartData) {

            Glide.with(binding.imgMenuCart)
                .load(cartItem.image_url)
                .into(binding.imgMenuCart)

            binding.tvNamaImgCart.text = cartItem.nameFood
            binding.txtCartHarga.text = cartItem.hargaPerItem.toString()
            binding.quantityTextView.text = cartItem.quantity.toString()
            etNote.setText(cartItem.note)

            binding.btnPlusCart.setOnClickListener {
                val newQuantity = cartItem.quantity + 1
                viewModel.updateQuantity(cartItem, newQuantity)
            }

            binding.btnMinCart.setOnClickListener {
                if (cartItem.quantity > 1) {
                    val newQuantity = cartItem.quantity - 1
                    viewModel.updateQuantity(cartItem, newQuantity)
                }
            }

            binding.btnDelete.setOnClickListener {
                viewModel.deleteCartItem(cartItem)
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
