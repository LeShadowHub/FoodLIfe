package com.example.foodlife.model;

import java.util.Date;

/**
 * Created by OEM on 12/2/2017.
 */

public class Item {
    private String id, title;
    private Date expiration;

    public Item(){
    }

    public Item(String id, String title, Date expiration) {
        this.id = id;
        this.title = title;
        this.expiration = expiration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}

