package com.example.cartorder.entity;


public class OrderProduct {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int imageResource;

    public OrderProduct(int id, String name, double price, int quantity, int imageResource) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResource = imageResource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getImageResource() {
        return imageResource;
    }
}
