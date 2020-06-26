package com.elton.foodie.DB;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Favourite.class},version = 1,exportSchema = false)
public abstract class FavouriteRoomDatabase extends RoomDatabase {
    public  abstract FavouriteDao favouriteDao();

    private static volatile FavouriteRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FavouriteRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (FavouriteRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavouriteRoomDatabase.class, "Foodie_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
