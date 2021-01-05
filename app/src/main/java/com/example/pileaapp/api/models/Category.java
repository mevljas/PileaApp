package com.example.pileaapp.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("plantCategory")
    @Expose
    private String plantCategory;
    @SerializedName("plants")
    @Expose
    private Object plants;
    @SerializedName("user")
    @Expose
    private Object user;

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getPlantCategory() {
        return plantCategory;
    }

    public void setPlantCategory(String plantCategory) {
        this.plantCategory = plantCategory;
    }

    public Object getPlants() {
        return plants;
    }

    public void setPlants(Object plants) {
        this.plants = plants;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return this.getPlantCategory();
    }
}
