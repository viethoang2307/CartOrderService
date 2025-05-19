package com.example.cartorder.api;

import com.example.cartorder.entity.CartResponse;
import com.example.cartorder.entity.OrderListResponse;
import com.example.cartorder.entity.OrderRequest;
import com.example.cartorder.entity.OrderResponse;
import com.example.cartorder.entity.UpdateCartRequest;
import com.example.cartorder.entity.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartOrderService {
    // Cart endpoints
    @GET("cart")
    Call<CartResponse> getCart(@Query("userId") String userId);

    @POST("cart/items")
    Call<CartResponse> addToCart(@Body UpdateCartRequest request);

    @PUT("cart/items/{itemId}")
    Call<CartResponse> updateCartItem(@Path("itemId") int itemId, @Body UpdateCartRequest request);

    @DELETE("cart/items/{itemId}")
    Call<CartResponse> removeCartItem(@Path("itemId") int itemId);

    // Order endpoints
    @POST("orders")
    Call<OrderResponse> createOrder(@Body OrderRequest orderRequest);

    @GET("orders")
    Call<OrderListResponse> getOrderHistory(@Query("userId") String userId);

    @GET("orders/{orderId}")
    Call<OrderResponse> getOrderDetails(@Path("orderId") String orderId);

    // User information
    @GET("users/{userId}")
    Call<UserResponse> getUserInfo(@Path("userId") String userId);
}