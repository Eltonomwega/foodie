package com.elton.foodie.Modals;

import android.net.Uri;

public class DishResultModalClass {
   private String DishName;
   private String DishTime;
   private Uri DishImage;
    private String DishId;

   public DishResultModalClass(String DishName, String DishTime, Uri DishImage,String DishId) {
       this.DishName = DishName;
       this.DishTime = DishTime;
       this.DishImage = DishImage;
       this.DishId = DishId;
   }

   public String getDishName() {
       return DishName;
   }

   public void setDishName(String dishName) {
       this.DishName = dishName;
   }

   public String getDishTime() {
       return DishTime;
   }

   public void setDishTime(String dishTime) {
       this.DishTime = dishTime;
   }

   public Uri getDishImage() {
       return DishImage;
   }

   public void setDishImage(Uri dishImage) {
       this.DishImage = dishImage;
   }

    public String getDishId() {
        return DishId;
    }

    public void setDishId(String dishId) {
        DishId = dishId;
    }

}
