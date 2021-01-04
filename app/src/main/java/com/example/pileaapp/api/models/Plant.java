package com.example.pileaapp.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Plant {



    @SerializedName("plantID")
    @Expose
    private Integer plantID;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("daysBetweenWatering")
    @Expose
    private Integer daysBetweenWatering;
    @SerializedName("lastWateredDate")
    @Expose
    private String lastWateredDate;
    @SerializedName("nextWateredDate")
    @Expose
    private String nextWateredDate;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("categoryID")
    @Expose
    private Integer categoryID;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("locationID")
    @Expose
    private Integer locationID;
    @SerializedName("user")
    @Expose
    private Object user;

    public Integer getPlantID() {
        return plantID;
    }

    public void setPlantID(Integer plantID) {
        this.plantID = plantID;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getDaysBetweenWatering() {
        return daysBetweenWatering;
    }

    public void setDaysBetweenWatering(Integer daysBetweenWatering) {
        this.daysBetweenWatering = daysBetweenWatering;
    }

    public String getLastWateredDate() {
        return lastWateredDate;
    }

    public void setLastWateredDate(String lastWateredDate) {
        this.lastWateredDate = lastWateredDate;
    }

    public String getNextWateredDate() {
        return nextWateredDate;
    }

    public void setNextWateredDate(String nextWateredDate) {
        this.nextWateredDate = nextWateredDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

}
