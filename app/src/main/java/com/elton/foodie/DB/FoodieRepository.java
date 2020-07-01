package com.elton.foodie.DB;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FoodieRepository {
  public static String dish;
    private FoodieDao mFoodieDao,mFoodieDao2;
    private LiveData<List<Favourite>> mFavourites;
    private LiveData<List<Dishes>> mDishes;
    public  String category;
    FoodieRepository(Application application){
        FoodieRoomDatabase db = FoodieRoomDatabase.getDatabase(application);
        mFoodieDao = db.foodDao();
        mFavourites = mFoodieDao.getFavouriteDishes();
        //mFoodieDao2 = db.foodDao();
        //mDishes = mFoodieDao2.getCategoryDishes(category);
    }

    LiveData<List<Favourite>> getAllFavourites(){

        //mFavourites.getValue();
        return mFavourites;
    }

//    LiveData<List<Dishes>> getAllDishes(String category){
//        this.category = category;
//        return mDishes;
//    }
//
//    void insert(Dishes dishes){
//       // FoodieRoomDatabase.databaseWriteExecutor.execute(()-> mFoodieDao2.insert(dishes));
//    }

    void insert(Favourite favourite){
        //Toast.makeText(this,"I am here",Toast.LENGTH_SHORT).show();
        Log.d("bookMark",favourite.getCalories());


        FoodieRoomDatabase.databaseWriteExecutor.execute(()-> mFoodieDao.insert(favourite));
       // new insertDishAsycTask(mFoodieDao).execute(favourite);
        Log.d("bookMark","item inserted");

    }
    void deleteDish(Favourite favourite){
        new deleteDishAsycTask(mFoodieDao).execute(favourite);
       // FavouriteRoomDatabase.databaseWriteExecutor.execute(()->mFavouriteDao.deleteDish(favourite));
    }
    String selectDish(String dishId){
        new selectDishAsycTask(mFoodieDao).execute(dishId);
        return dish;

    }

    public  static class  deleteDishAsycTask extends AsyncTask<Favourite,Void,Void>{

            private FoodieDao mFoodieDao;

        public deleteDishAsycTask(FoodieDao mFoodieDao) {
            this.mFoodieDao = mFoodieDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourite) {
            mFoodieDao.deleteDish(favourite[0]);
            return null;
        }
    }
    public static class selectDishAsycTask extends AsyncTask<String,Void,Void>{

        private FoodieDao mFoodieDao;

        selectDishAsycTask(FoodieDao mFoodieDao) {
            this.mFoodieDao = mFoodieDao;
        }

        @Override
        protected Void doInBackground(String... dishId) {
           dish= mFoodieDao.selectDish(dishId[0]);
            return null;
        }
    }

    public static class insertDishAsycTask extends AsyncTask<Favourite,Void,Void>{

        private FoodieDao mFoodieDao;

        insertDishAsycTask(FoodieDao mFoodieDao) {
            this.mFoodieDao = mFoodieDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourite) {
           // dish= mFoodieDao.selectDish(dishId[0]);
            mFoodieDao.insert(favourite[0]);
            return null;
        }
    }
}
