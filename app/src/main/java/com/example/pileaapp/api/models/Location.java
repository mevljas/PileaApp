package com.example.pileaapp.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("locationID")
    @Expose
    private Integer locationID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("plants")
    @Expose
    private Object plants;
    @SerializedName("user")
    @Expose
    private Object user;

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return this.getName();
    }
}
