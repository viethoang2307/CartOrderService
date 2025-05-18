package com.example.cartorder;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartorder.adapter.CartAdapter;
import com.example.cartorder.adapter.RecommendationAdapter;
import com.example.cartorder.entity.CartItem;
import com.example.cartorder.entity.RecommendationItem;

import java.util.ArrayList;
import java.util.List;



public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerCartItems;
    private RecyclerView recyclerRecommendations;
    private CartAdapter cartAdapter;
    private RecommendationAdapter recommendationAdapter;
    private TextView tvTotalPrice;
    private EditText etCouponCode;
    private Button btnApplyCoupon;
    private Button btnCheckout;
    private ImageButton btnBack;
    private Button btnViewOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        initViews();
        setupCartRecyclerView();
        setupRecommendationsRecyclerView();
        setupListeners();
    }

    private void initViews() {
        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        recyclerRecommendations = findViewById(R.id.recyclerRecommendations);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        etCouponCode = findViewById(R.id.etCouponCode);
        btnApplyCoupon = findViewById(R.id.btnApplyCoupon);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);
        btnViewOrders = findViewById(R.id.btnViewOrders);
    }

    private void setupCartRecyclerView() {
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));

        // Sample data - in a real app, this would come from your data source
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1, "Jon Sinanggarwik sofa", 599, 1, R.drawable.placeholder_image));
        cartItems.add(new CartItem(2, "Swelom chair", 400, 1, R.drawable.placeholder_image));
        cartItems.add(new CartItem(3, "Kallax chair", 199, 1, R.drawable.placeholder_image));

        cartAdapter = new CartAdapter(this, cartItems, new CartAdapter.CartItemListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                // Update quantity logic would go here
                updateTotalPrice();
            }

            @Override
            public void onRemoveItem(CartItem item) {
                // Remove item logic would go here
                updateTotalPrice();
            }
        });

        recyclerCartItems.setAdapter(cartAdapter);
    }

    private void setupRecommendationsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerRecommendations.setLayoutManager(layoutManager);

        // Sample data - in a real app, this would come from your data source
        List<RecommendationItem> recommendations = new ArrayList<>();
        recommendations.add(new RecommendationItem(1, "Kirim sofa", 599, R.drawable.placeholder_image, false));
        recommendations.add(new RecommendationItem(2, "Grundlid sofa", 499, R.drawable.placeholder_image, false));

        recommendationAdapter = new RecommendationAdapter(this, recommendations, new RecommendationAdapter.RecommendationListener() {
            @Override
            public void onFavoriteToggle(RecommendationItem item) {
                // Toggle favorite logic would go here
            }

            @Override
            public void onItemClick(RecommendationItem item) {
                // Item click logic would go here
            }
        });

        recyclerRecommendations.setAdapter(recommendationAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String couponCode = etCouponCode.getText().toString().trim();
                if (!couponCode.isEmpty()) {
                    // Apply coupon logic would go here
                    Toast.makeText(ShoppingCartActivity.this, "Coupon applied", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShoppingCartActivity.this, "Please enter a coupon code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checkout logic would go here
                Toast.makeText(ShoppingCartActivity.this, "Proceeding to checkout", Toast.LENGTH_SHORT).show();
            }
        });

        btnViewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Orders screen
                Intent intent = new Intent(ShoppingCartActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateTotalPrice() {
        // Calculate total price based on cart items
        double total = 0;
        for (CartItem item : cartAdapter.getCartItems()) {
            total += item.getPrice() * item.getQuantity();
        }

        tvTotalPrice.setText(String.format("$%,.0f", total));
    }
}
