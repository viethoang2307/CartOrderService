package com.example.cartorder;



import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.cartorder.api.ApiClient;
import com.example.cartorder.api.CartOrderService;
import com.example.cartorder.entity.CartItem;
import com.example.cartorder.entity.CartItemModel;
import com.example.cartorder.entity.CartResponse;
import com.example.cartorder.entity.OrderRequest;
import com.example.cartorder.entity.OrderResponse;
import com.example.cartorder.entity.RecommendationItem;
import com.example.cartorder.entity.UpdateCartRequest;

import java.util.ArrayList;
import java.util.List;



import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartActivity extends AppCompatActivity {

    private RecyclerView recyclerCartItems;
    private CartAdapter cartAdapter;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ImageButton btnBack;
    private Button btnViewOrders;
    private View loadingView;
    private TextView tvEmptyCart;

    private CartOrderService cartOrderService;
    private String userId;
    private List<CartItemModel> currentCartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        // Initialize API service
        cartOrderService = ApiClient.getClient().create(CartOrderService.class);

        // Get user ID from SharedPreferences (in a real app, this would come from your auth system)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getString("userId", "user123"); // Default to "user123" if not found

        initViews();
        setupCartRecyclerView();
        setupListeners();

        // Load cart data
        loadCartData();
    }

    private void initViews() {
        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);
        btnViewOrders = findViewById(R.id.btnViewOrders);
        loadingView = findViewById(R.id.loadingView);
        tvEmptyCart = findViewById(R.id.tvEmptyCart);
    }

    private void setupCartRecyclerView() {
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, new ArrayList<>(), new CartAdapter.CartItemListener() {
            @Override
            public void onQuantityChanged(CartItem item, int newQuantity) {
                updateCartItemQuantity(item.getId(), newQuantity);
            }

            @Override
            public void onRemoveItem(CartItem item) {
                removeCartItem(item.getId());
            }
        });
        recyclerCartItems.setAdapter(cartAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToCheckout();
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

    private void loadCartData() {
        showLoading(true);

        cartOrderService.getCart(userId).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    currentCartItems = cartResponse.getItems();
                    updateCartUI(cartResponse);
                } else {
                    showError("Failed to load cart data");
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                showLoading(false);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void updateCartUI(CartResponse cartResponse) {
        List<CartItemModel> cartItems = cartResponse.getItems();

        if (cartItems == null || cartItems.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            recyclerCartItems.setVisibility(View.GONE);
            btnCheckout.setEnabled(false);
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            recyclerCartItems.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(true);

            // Convert API models to UI models
            List<CartItem> uiCartItems = new ArrayList<>();
            for (CartItemModel item : cartItems) {
                uiCartItems.add(new CartItem(
                        item.getId(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getImageUrl()
                ));
            }

            cartAdapter.updateCartItems(uiCartItems);
        }

        // Update total price
        tvTotalPrice.setText(String.format("$%,.0f", cartResponse.getTotal()));
    }

    private void updateCartItemQuantity(int itemId, int newQuantity) {
        // Get productId from currentCartItems
        CartItemModel cartItem = null;
        for (CartItemModel item : currentCartItems) {
            if (item.getId() == itemId) {
                cartItem = item;
                break;
            }
        }

        if (cartItem == null) {
            showError("Item not found in cart");
            return;
        }

        UpdateCartRequest request = new UpdateCartRequest(userId, cartItem.getProductId(), newQuantity);

        cartOrderService.updateCartItem(itemId, request).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateCartUI(response.body());
                } else {
                    showError("Failed to update item quantity");
                    // Reload cart to ensure UI is in sync with server
                    loadCartData();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                showError("Network error: " + t.getMessage());
                loadCartData();
            }
        });
    }

    private void removeCartItem(int itemId) {
        cartOrderService.removeCartItem(itemId).enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateCartUI(response.body());
                    Toast.makeText(ShoppingCartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                } else {
                    showError("Failed to remove item");
                    loadCartData();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                showError("Network error: " + t.getMessage());
                loadCartData();
            }
        });
    }

    private void navigateToCheckout() {
        if (currentCartItems == null || currentCartItems.isEmpty()) {
            showError("Your cart is empty");
            return;
        }
        // Launch CheckoutActivity to collect shipping and payment details
        Intent intent = new Intent(ShoppingCartActivity.this, CheckoutActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            String shippingAddress = data.getStringExtra("shipping_address");
            String paymentMethod = data.getStringExtra("payment_method");
            createOrder(shippingAddress, paymentMethod);
        }
    }

    private void createOrder(String shippingAddress, String paymentMethod) {
        if (currentCartItems == null || currentCartItems.isEmpty()) {
            showError("Your cart is empty");
            return;
        }

        showLoading(true);

        OrderRequest orderRequest = new OrderRequest(userId, currentCartItems, shippingAddress, paymentMethod);

        cartOrderService.createOrder(orderRequest).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                showLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    // Order created successfully
                    Toast.makeText(ShoppingCartActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to order history
                    Intent intent = new Intent(ShoppingCartActivity.this, OrderActivity.class);
                    startActivity(intent);
                    finish(); // Close cart activity
                } else {
                    showError("Failed to place order");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                showLoading(false);
                showError("Network error: " + t.getMessage());
            }
        });
    }

    private void showLoading(boolean isLoading) {
        loadingView.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}