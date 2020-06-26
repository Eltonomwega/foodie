package com.elton.foodie.Modals;

import android.net.Uri;


public class IngredientsModalClass {
    private String ingredientName;
    private String ingredientQuantity;
    private Uri ingredientImage;


    public IngredientsModalClass(String ingredientName, String ingredientQuantity, Uri ingredientImage) {
        this.ingredientName = ingredientName;
        this.ingredientQuantity = ingredientQuantity;
        this.ingredientImage = ingredientImage;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(String ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public Uri getIngredientImage() {
        return ingredientImage;
    }

    public void setIngredientImage(Uri ingredientImage) {
        this.ingredientImage = ingredientImage;
    }




}
