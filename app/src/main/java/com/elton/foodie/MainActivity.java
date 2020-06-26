package com.elton.foodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elton.foodie.Adapters.IngredientsAdapter;
import com.elton.foodie.DB.Favourite;
import com.elton.foodie.DB.FavouriteViewModel;
import com.elton.foodie.Fragments.SearchFragment;
import com.elton.foodie.Modals.APIModalClass;
import com.elton.foodie.Modals.IngredientsModalClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private static final String TAG = "MyActivity";
    private List<IngredientsModalClass> ingredientList = new ArrayList<>();
    ImageView dishImage;
    TextView foodTitle,foodReadyMinutes,foodServings,foodCalory;
    MaterialButton Price;
    String testId;
    String dishId;
    Uri uri;
    String imgUrl;
    private FavouriteViewModel mFavouriteViewModel;
    private VolleyCallback volleyCallback;
    APIModalClass apiModalClass = new APIModalClass();
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


        queue = Volley.newRequestQueue(this);
        volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject jsonObject) throws JSONException {

                JSONArray extendedIng = jsonObject.getJSONArray("extendedIngredients");
                for (int i=0; i<extendedIng.length();i++){
                    JSONObject Ingredient = extendedIng.getJSONObject(i);
                    uri = Uri.parse("https://spoonacular.com/cdn/ingredients_100x100/"+Ingredient.get("image").toString());
                    IngredientsModalClass ingredientsModalClass = new IngredientsModalClass(Ingredient.get("name").toString(), Ingredient.getString("amount")+" "+Ingredient.get("unit").toString(),uri);
                    ingredientList.add(ingredientsModalClass);
                }
                initRecyclerview();

            }
        };

        Intent intent = getIntent();
        dishId = intent.getStringExtra(SearchFragment.EXTRA_DISH_KEY);
        StringRequest stringRequest = getRecipeInfoStringRequest(volleyCallback,dishId);
        Log.e(TAG,"url");
        // executing the request (adding to queue)
        queue.add(stringRequest);

        mFavouriteViewModel = new ViewModelProvider(this).get(FavouriteViewModel.class);

        mFavouriteViewModel.getAllFavourites().observe(this, new Observer<List<Favourite>>() {
            @Override
            public void onChanged(List<Favourite> favourites) {

            }
        });
    }

    public void callApi(View view) {

        /*Toast.makeText(MainActivity.this, "Or", Toast.LENGTH_LONG).show();
        Log.d(TAG,"Here "+testId);*/
    }

    public void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingList);
        IngredientsAdapter mAdapter = new IngredientsAdapter(ingredientList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private StringRequest searchNameStringRequest(String nameSearch) {
        // url = url + id + "/information?includeNutrition=false&apiKey=db9a577a695640528bb7a67487f8a907";
        //https://spoonacular.com/recipeImages/579247-556x370.jpg
        final String API = "&apiKey=db9a577a695640528bb7a67487f8a907";
        final String NAME_SEARCH = "/search?query=";
        final String number = "&number=1";
        final String size = "-556x370.jpg";
        final String URL_PREFIX = "https://api.spoonacular.com/recipes";
        final String IMG_PREFIX = "https://spoonacular.com/";
        final String url;
            url = URL_PREFIX + NAME_SEARCH + nameSearch + number + API;
        Log.d(TAG,url);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                                JSONArray resultArry = result.getJSONArray("results");

                                    //gets each JSON object within the JSON array
                                    JSONObject jsonObject = resultArry.getJSONObject(0);

                                   String ID = jsonObject.get("id").toString();
                                   String urlImg = IMG_PREFIX +"recipeImages/"+ ID + size;
                                   Picasso.get().load(urlImg).into(dishImage);

                                   StringRequest stringRequest1 = getRecipeInfoStringRequest(volleyCallback,ID);
                                   queue.add(stringRequest1);

                                   Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_LONG).show();
                                   Log.d(TAG,urlImg);
                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(TAG,e.getMessage());
                        }
                    }
                    // public void onResponse(String response)
                }, // Response.Listener<String>()
                new Response.ErrorListener() {
                    // 4th param - method onErrorResponse lays the code procedure of error return
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(MainActivity.this, "Food source is not responding (USDA API)", Toast.LENGTH_LONG).show();
                        Log.e(TAG,error.getMessage());

                    }
                });
    }

    private StringRequest getRecipeInfoStringRequest(final VolleyCallback volleyCallback,String ID) {
        //https://api.spoonacular.com/recipes/716429/information?includeNutrition=true&apiKey=7992174586184c75bc46188438e23e9b


        imgUrl = apiModalClass.getImgURL() +"recipeImages/"+ ID + apiModalClass.getSize();

        final String url = apiModalClass.getURL_Prefix() + ID + apiModalClass.getSearchURL() + apiModalClass.getAPI_KEY();

        Picasso.get().load(imgUrl).into(dishImage);

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);


                            String title = result.get("title").toString();
                            String servings = result.get("servings").toString();
                            String ReadyInMinutes = result.get("readyInMinutes").toString();
                            String unitPrice = "Total price: $"+result.get("pricePerServing").toString();
                            JSONObject Nutrition = result.getJSONObject("nutrition");
                            JSONArray nutrients = Nutrition.getJSONArray("nutrients");
                            JSONObject Cal = nutrients.getJSONObject(0);
                            String Calories = Cal.get("amount").toString()+" calories";
                            foodTitle.setText(title);
                            foodReadyMinutes.setText(ReadyInMinutes);
                            foodServings.setText(servings);
                            foodCalory.setText(Calories);
                            Price.setText(unitPrice);
                            testId = Calories;

                            volleyCallback.onSuccess(result);



                        }catch (JSONException e){
                            Log.e(TAG,e.getMessage());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }


        );

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

    public interface VolleyCallback{

        void onSuccess(JSONObject jsonObject) throws JSONException;
    }


}
