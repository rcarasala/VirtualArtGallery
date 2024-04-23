package com.bittercode.model;

import java.io.Serializable;

public class Cart implements Serializable {

    private Art art;
    private int quantity;

    public Cart(Art art, int quantity) {
        this.art = art;
        this.quantity = quantity;
    }

    public Art getArt() {
        return art;
    }

    public void setArt(Art art) {
        this.art = art;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
