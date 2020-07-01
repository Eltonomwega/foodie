package com.elton.foodie.DB;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FoodieViewModel extends AndroidViewModel {
    private FoodieRepository mRepository;
    private LiveData<List<Favourite>> mFavourite;
    private LiveData<List<Dishes>> mDishes;
    String Category;

    public FoodieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FoodieRepository(application);
        mFavourite = mRepository.getAllFavourites();
        //mDishes = mRepository.getAllDishes(Category);
    }

    public LiveData<List<Favourite>> getAllFavourites(){return mFavourite;}
 /*   public LiveData<List<Dishes>> getAllDishes(String category){
        Category = category;
        return mDishes;}*/


    public void insert(Favourite favourite){

        mRepository.insert(favourite);}
    public String selectDish(String id){return mRepository.selectDish(id);}
    public void  delete(Favourite favourite){mRepository.deleteDish(favourite);}
}
