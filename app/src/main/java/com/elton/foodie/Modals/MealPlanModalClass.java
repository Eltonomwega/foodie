package com.elton.foodie.Modals;

import android.net.Uri;

public class MealPlanModalClass {

    private String DishName;
    private String DishTime;
    private Uri DishImage;
    private String DishId;
    private String DishType;

    public MealPlanModalClass(String dishName, String dishTime, Uri dishImage, String dishId, String dishType) {
        DishName = dishName;
        DishTime = dishTime;
        DishImage = dishImage;
        DishId = dishId;
        DishType = dishType;
    }


    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public String getDishTime() {
        return DishTime;
    }

    public void setDishTime(String dishTime) {
        DishTime = dishTime;
    }

    public Uri getDishImage() {
        return DishImage;
    }

    public void setDishImage(Uri dishImage) {
        DishImage = dishImage;
    }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
    }

    public String getDishType() {
        return DishType;
    }

    public void setDishType(String dishType) {
        DishType = dishType;
    }
}
