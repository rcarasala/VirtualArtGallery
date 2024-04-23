package com.bittercode.model;

import java.io.Serializable;

public class Art implements Serializable {

    private String barcode;
    private String name;
    private String artist;
    private double price;
    private int quantity;

    public Art(String barcode, String name, String artist, double price, int quantity) {
        this.barcode = barcode;
        this.name = name;
        this.artist = artist;
        this.setPrice(price);
        this.quantity = quantity;
    }

    public Art() {
        super();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setartist(String artist) {
        this.artist = artist;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}
