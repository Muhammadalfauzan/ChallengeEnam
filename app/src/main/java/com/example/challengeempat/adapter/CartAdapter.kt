package com.example.challengeempat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.challengeempat.database.CartData
import com.example.challengeempat.databinding.ItemCartBinding
import com.example.challengeempat.viewmodel.CartViewModel

@Suppress("DEPRECATION")
class CartAdapter(
    private val viewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems: List<CartData> = emptyList()

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val etNote = binding.tvCatatan

        init {
            // Handle the EditText focus change event
            etNote.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    val adapterPosition = adapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        val newNote = etNote.text.toString()
                        viewModel.updateNote(cartItems[adapterPosition], newNote)
                    }
                }
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

    fun updateDataCart(newData: List<CartData>) {
        cartItems = newData
        notifyDataSetChanged()
    }
}
