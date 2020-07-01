package com.elton.foodie.DB;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FoodieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Favourite favourite);

    @Delete
    void deleteDish(Favourite favourite);

    @Query("DELETE FROM favourite_dishes WHERE id = :dishId")
    void deleteByDishId(String dishId);

    @Query("SELECT id from favourite_dishes WHERE id = :dishId LIMIT 1")
    String selectDish(String dishId);


    @Query("SELECT * from favourite_dishes ORDER BY title ASC")
    LiveData<List<Favourite>> getFavouriteDishes();

    //Dishes
    /*@Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Dishes dishes);*/

    @Delete
    void deleteDish(Dishes dishes);


    @Query("SELECT * from daily_dishes WHERE category = :dishcategory")
    LiveData<List<Dishes>> getCategoryDishes(String dishcategory);

}
