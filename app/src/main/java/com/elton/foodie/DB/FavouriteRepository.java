package com.elton.foodie.DB;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class FavouriteRepository {
  public static String dish;
    private FavouriteDao mFavouriteDao;
    private LiveData<List<Favourite>> mFavourites;

    FavouriteRepository(Application application){
        FavouriteRoomDatabase db = FavouriteRoomDatabase.getDatabase(application);
        mFavouriteDao = db.favouriteDao();
        mFavourites = mFavouriteDao.getFavouriteDishes();
    }

    LiveData<List<Favourite>> getAllFavourites(){
        return mFavourites;
    }

    void insert(Favourite favourite){
        FavouriteRoomDatabase.databaseWriteExecutor.execute(()->mFavouriteDao.insert(favourite));
    }
    void deleteDish(Favourite favourite){
        new deleteDishAsycTask(mFavouriteDao).execute(favourite);
       // FavouriteRoomDatabase.databaseWriteExecutor.execute(()->mFavouriteDao.deleteDish(favourite));
    }
    String selectDish(String dishId){
        new selectDishAsycTask(mFavouriteDao).execute(dishId);
        return dish;

    }

    public  static class  deleteDishAsycTask extends AsyncTask<Favourite,Void,Void>{

            private FavouriteDao mFavouriteDao;

        public deleteDishAsycTask(FavouriteDao mFavouriteDao) {
            this.mFavouriteDao = mFavouriteDao;
        }

        @Override
        protected Void doInBackground(Favourite... favourite) {
            mFavouriteDao.deleteDish(favourite[0]);
            return null;
        }
    }
    public static class selectDishAsycTask extends AsyncTask<String,Void,Void>{

        private FavouriteDao mFavouriteDao;

        selectDishAsycTask(FavouriteDao mFavouriteDao) {
            this.mFavouriteDao = mFavouriteDao;
        }

        @Override
        protected Void doInBackground(String... dishId) {
           dish=mFavouriteDao.selectDish(dishId[0]);
            return null;
        }
    }
}
