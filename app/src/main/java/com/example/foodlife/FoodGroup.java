package com.example.foodlife;

/**
 * Created by OEM on 11/20/2017.
 */

public class FoodGroup {

    private String name;
    private int image;


    public FoodGroup(String name, int image){
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
