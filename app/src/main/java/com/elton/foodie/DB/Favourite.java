package com.elton.foodie.DB;


import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favourite_dishes")
public class Favourite {
    @NonNull
    @PrimaryKey
    private String id;

    public String getId() {
        return this.id;
    }

    public Favourite(@NonNull String id, @NonNull String title, @NonNull String readyTime,
                     @NonNull String imgUrl, @NonNull String calories){
        this.id=id;
        this.title=title;
        this.readyTime= readyTime;
        this.imgUrl = imgUrl;
        this.calories = calories;
    }

    @NonNull
    @ColumnInfo(name = "title")
    private String title;
    @NonNull
    public String getTitle(){return this.title;}

    @NonNull
    @ColumnInfo(name = "readyTime")
    private String readyTime;
    @NonNull
    public String getReadyTime(){return this.readyTime;}

    @NonNull
    @ColumnInfo(name = "imgUrl")
    private String imgUrl;
    @NonNull
    public String getImgUrl(){return this.imgUrl;}

    @NonNull
    @ColumnInfo(name = "calories")
    private String calories;

    @NonNull
    public String getCalories(){return this.calories;}

}
