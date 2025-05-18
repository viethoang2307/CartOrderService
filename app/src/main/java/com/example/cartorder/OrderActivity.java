package com.example.cartorder;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartorder.adapter.OrderAdapter;
import com.example.cartorder.entity.Order;
import com.example.cartorder.entity.OrderProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerOrders;
    private TextView tvEmptyOrders;
    private OrderAdapter orderAdapter;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initViews();
        setupOrdersRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerOrders = findViewById(R.id.recyclerOrders);
        tvEmptyOrders = findViewById(R.id.tvEmptyOrders);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupOrdersRecyclerView() {
        recyclerOrders.setLayoutManager(new LinearLayoutManager(this));

        // Sample data - in a real app, this would come from your data source
        List<Order> orders = getSampleOrders();

        if (orders.isEmpty()) {
            tvEmptyOrders.setVisibility(View.VISIBLE);
            recyclerOrders.setVisibility(View.GONE);
        } else {
            tvEmptyOrders.setVisibility(View.GONE);
            recyclerOrders.setVisibility(View.VISIBLE);

            orderAdapter = new OrderAdapter(this, orders, new OrderAdapter.OrderListener() {
                @Override
                public void onTrackOrder(Order order) {
                    // Track order logic would go here
                }

                @Override
                public void onReorder(Order order) {
                    // Reorder logic would go here
                }
            });

            recyclerOrders.setAdapter(orderAdapter);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<Order> getSampleOrders() {
        List<Order> orders = new ArrayList<>();

        // Order 1
        List<OrderProduct> products1 = new ArrayList<>();
        products1.add(new OrderProduct(1, "Jon Sinanggarwik sofa", 599, 1, R.drawable.placeholder_image));
        products1.add(new OrderProduct(2, "Swelom chair", 400, 1, R.drawable.placeholder_image));

        Order order1 = new Order(
                "12345",
                "May 15, 2023",
                "Delivered",
                products1,
                999
        );

        // Order 2
        List<OrderProduct> products2 = new ArrayList<>();
        products2.add(new OrderProduct(3, "Kallax chair", 199, 2, R.drawable.placeholder_image));
        products2.add(new OrderProduct(4, "Kirim sofa", 599, 1, R.drawable.placeholder_image));

        Order order2 = new Order(
                "12346",
                "April 28, 2023",
                "Processing",
                products2,
                997
        );

        // Order 3
        List<OrderProduct> products3 = new ArrayList<>();
        products3.add(new OrderProduct(5, "Grundlid sofa", 499, 1, R.drawable.placeholder_image));

        Order order3 = new Order(
                "12347",
                "April 10, 2023",
                "Cancelled",
                products3,
                499
        );

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        return orders;
    }
}
