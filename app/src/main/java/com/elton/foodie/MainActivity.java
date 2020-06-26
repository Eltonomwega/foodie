package com.elton.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elton.foodie.Adapters.IngredientsAdapter;
import com.elton.foodie.DB.Favourite;
import com.elton.foodie.DB.FavouriteViewModel;
import com.elton.foodie.Fragments.SearchFragment;
import com.elton.foodie.Modals.IngredientsModalClass;
import com.elton.foodie.Networking.SpoonacularRequests;
import com.elton.foodie.Utils.VolleyCallback;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private List<IngredientsModalClass> ingredientList = new ArrayList<>();
   public ImageView dishImage;
    public TextView foodTitle,foodReadyMinutes,foodServings,foodCalory;
   public MaterialButton Price;
   public String dishId;
  public   Uri uri;
   public String imgUrl;
    private FavouriteViewModel mFavouriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dishImage = findViewById(R.id.foodImage);
        foodTitle = findViewById(R.id.foodTitle);
        foodReadyMinutes = findViewById(R.id.time);
        foodServings = findViewById(R.id.servings);
        foodCalory = findViewById(R.id.Calories);
        Price = findViewById(R.id.Price);


        RequestQueue queue = Volley.newRequestQueue(this);
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {

                JSONArray extendedIng = jsonObject.getJSONArray("extendedIngredients");
                for (int i = 0; i < extendedIng.length(); i++) {
                    JSONObject Ingredient = extendedIng.getJSONObject(i);
                    uri = Uri.parse("https://spoonacular.com/cdn/ingredients_100x100/" + Ingredient.get("image").toString());
                    IngredientsModalClass ingredientsModalClass = new IngredientsModalClass(Ingredient.get("name").toString(), Ingredient.getString("amount") + " " + Ingredient.get("unit").toString(), uri);
                    ingredientList.add(ingredientsModalClass);
                }
                initRecyclerview();

            }

            @Override
            public void onSuccess2(JSONArray jsonArray){

            }

            @Override
            public void onSuccess(JSONArray jsonArray){

            }
        };

        Intent intent = getIntent();
        dishId = intent.getStringExtra(SearchFragment.EXTRA_DISH_KEY);
        SpoonacularRequests spoonacularRequests = new SpoonacularRequests();

        StringRequest stringRequest = spoonacularRequests.getRecipeInfoStringRequest(volleyCallback,dishId,dishImage,foodTitle,foodReadyMinutes,foodServings,foodCalory,Price);

        Log.e(TAG,"url");
        // executing the request (adding to queue)
        queue.add(stringRequest);

        mFavouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        mFavouriteViewModel.getAllFavourites().observe(this, favourites -> {

        });
    }


    public void initRecyclerview(){
        RecyclerView recyclerView = findViewById(R.id.ingList);
        IngredientsAdapter mAdapter = new IngredientsAdapter(ingredientList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }




    public void bookmarkDish(View view) {
        FloatingActionButton mfloatingActionButton;

        mfloatingActionButton = findViewById(R.id.Bookmark);
        String title = foodTitle.getText().toString();
        String readyTime = foodReadyMinutes.getText().toString();
        String calories = foodCalory.getText().toString();
        String img = imgUrl;
        if (title.isEmpty()&&readyTime.isEmpty()&&calories.isEmpty()&&img == null){
            Toast.makeText(getApplicationContext(),"Food item not found",Toast.LENGTH_LONG).show();

        }else {
            Favourite favourite = new Favourite(dishId, title, readyTime + " Min", img, calories);
            if (mFavouriteViewModel.selectDish(dishId)==null){
                //mfloatingActionButton.setBackgroundResource(R.drawable.ic_favorite);



                Log.d("bookMark","This already exists");
                mFavouriteViewModel.insert(favourite);
                mfloatingActionButton.setImageResource(R.drawable.ic_favourite_filled);

            }else {
                //mfloatingActionButton.setBackgroundResource(R.drawable.ic_favourite_filled);
                mFavouriteViewModel.delete(favourite);
                mfloatingActionButton.setImageResource(R.drawable.ic_favorite);
                Log.d("bookMark","This does not exists");
            }
        }


    }

}
