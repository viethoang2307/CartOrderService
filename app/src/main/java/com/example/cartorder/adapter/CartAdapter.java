package com.example.cartorder.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cartorder.R;
import com.example.cartorder.entity.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private CartItemListener listener;

    public interface CartItemListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onRemoveItem(CartItem item);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, CartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvProductName.setText(item.getName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvQuantityValue.setText(String.valueOf(item.getQuantity()));
        holder.tvPrice.setText(String.format("$%d", (int)item.getPrice()));

        // Load image using Glide
        if (item.getImageResource() != null && !item.getImageResource().isEmpty()) {
            try {
                // Try to load as URL first
                Glide.with(context)
                        .load(item.getImageResource())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .into(holder.ivProduct);
            } catch (Exception e) {
                // If URL loading fails, try to load as resource ID
                try {
                    int resourceId = Integer.parseInt(item.getImageResource());
                    holder.ivProduct.setImageResource(resourceId);
                } catch (NumberFormatException ex) {
                    // If both fail, show placeholder
                    holder.ivProduct.setImageResource(R.drawable.placeholder_image);
                }
            }
        } else {
            holder.ivProduct.setImageResource(R.drawable.placeholder_image);
        }

        holder.btnDecrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity >= 1) {
                item.setQuantity(newQuantity);
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                holder.tvQuantityValue.setText(String.valueOf(newQuantity));
                listener.onQuantityChanged(item, newQuantity);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            item.setQuantity(newQuantity);
            holder.tvQuantity.setText(String.valueOf(newQuantity));
            holder.tvQuantityValue.setText(String.valueOf(newQuantity));
            listener.onQuantityChanged(item, newQuantity);
        });

        holder.ivRemove.setOnClickListener(v -> {
            listener.onRemoveItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void updateCartItems(List<CartItem> newItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newItems);
        notifyDataSetChanged();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvProductName;
        TextView tvQuantity;
        TextView tvQuantityValue;
        TextView tvPrice;
        Button btnDecrease;
        Button btnIncrease;
        ImageButton ivRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvQuantityValue = itemView.findViewById(R.id.tvQuantityValue);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            ivRemove = itemView.findViewById(R.id.ivRemove);
        }
    }
}

