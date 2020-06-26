package com.elton.foodie.Modals;

import android.net.Uri;

public class TrendingModalClass {
    private String trendingName;
    private String trendingTime;
    private Uri trendingImage;
    private String DishId;


    public TrendingModalClass(String trendingName, String trendingTime, Uri trendingImage, String dishId) {
        this.trendingName = trendingName;
        this.trendingTime = trendingTime;
        this.trendingImage = trendingImage;
        DishId = dishId;
    }

    public String getTrendingName() {
        return trendingName;
    }

    public void setTrendingName(String trendingName) {
        this.trendingName = trendingName;
    }

    public String getTrendingTime() {
        return trendingTime;
    }

    public void setTrendingTime(String trendingTime) {
        this.trendingTime = trendingTime;
    }

    public Uri getTrendingImage() {
        return trendingImage;
    }

    public void setTrendingImage(Uri trendingImage) {
        this.trendingImage = trendingImage;
    }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
    }
}
