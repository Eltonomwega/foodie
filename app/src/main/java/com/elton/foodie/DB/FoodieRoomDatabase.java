package com.elton.foodie.DB;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Favourite.class,Dishes.class},version = 2,exportSchema = false)
public abstract class FoodieRoomDatabase extends RoomDatabase {
    public  abstract FoodieDao foodDao();
    private static volatile FoodieRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static FoodieRoomDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (FoodieRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodieRoomDatabase.class, "Foodie_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2=new
            Migration(1,2) {
                @Override
                public void migrate(@NonNull SupportSQLiteDatabase database) {
                    database.execSQL(
                            "CREATE TABLE IF NOT EXISTS `daily_dishes` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `readyTime` TEXT NOT NULL, `imgUrl` TEXT NOT NULL, `calories` TEXT NOT NULL, `category` TEXT NOT NULL, PRIMARY KEY(`id`))");
                }
            };

}
