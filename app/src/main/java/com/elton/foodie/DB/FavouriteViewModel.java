package com.elton.foodie.DB;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavouriteViewModel extends AndroidViewModel {
    private FavouriteRepository mRepository;
    private LiveData<List<Favourite>> mFavourite;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavouriteRepository(application);
        mFavourite = mRepository.getAllFavourites();
    }

    public LiveData<List<Favourite>> getAllFavourites(){return mFavourite;}

    public void insert(Favourite favourite){mRepository.insert(favourite);}
    public String selectDish(String id){return mRepository.selectDish(id);}
    public void  delete(Favourite favourite){mRepository.deleteDish(favourite);}
}
