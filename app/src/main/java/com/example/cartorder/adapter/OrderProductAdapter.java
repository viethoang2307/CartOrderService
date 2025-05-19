package com.example.cartorder.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartorder.R;
import com.example.cartorder.entity.OrderProduct;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.OrderProductViewHolder> {

    private Context context;
    private List<OrderProduct> products;

    public OrderProductAdapter(Context context, List<OrderProduct> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public OrderProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_product, parent, false);
        return new OrderProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductViewHolder holder, int position) {
        OrderProduct product = products.get(position);

        holder.tvOrderProductName.setText(product.getName());
        holder.tvOrderProductQuantity.setText("Qty: " + product.getQuantity());
        holder.tvOrderProductPrice.setText(String.format("$%d", (int)product.getPrice()));
        holder.ivOrderProduct.setImageResource(Integer.parseInt(product.getImageResource()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class OrderProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOrderProduct;
        TextView tvOrderProductName;
        TextView tvOrderProductQuantity;
        TextView tvOrderProductPrice;

        public OrderProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrderProduct = itemView.findViewById(R.id.ivOrderProduct);
            tvOrderProductName = itemView.findViewById(R.id.tvOrderProductName);
            tvOrderProductQuantity = itemView.findViewById(R.id.tvOrderProductQuantity);
            tvOrderProductPrice = itemView.findViewById(R.id.tvOrderProductPrice);
        }
    }
}
