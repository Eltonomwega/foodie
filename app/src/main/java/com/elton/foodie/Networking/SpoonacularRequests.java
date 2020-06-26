package com.elton.foodie.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.elton.foodie.MainActivity;
import com.elton.foodie.Modals.APIModalClass;
import com.elton.foodie.R;
import com.elton.foodie.Utils.VolleyCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


public class SpoonacularRequests {
    private static final String TAG = "MyActivity";
    private APIModalClass apiModalClass = new APIModalClass();
    //MainActivity Variables
    public String imgUrl;
    public String message;
    private ShimmerFrameLayout mshimmerFrameLayout;
    Context context;
    View mView;

    public SpoonacularRequests(){

    }

    public SpoonacularRequests(Context mContext){
        this.context = mContext;
    }
    public SpoonacularRequests(View view){
        this.mView = view;
    }

    private MainActivity mainActivity = new MainActivity();


    public StringRequest getRecipeInfoStringRequest(final VolleyCallback volleyCallback, String ID,ImageView dishImage
    ,TextView foodTitle,TextView foodReadyMinutes, TextView foodServings, TextView foodCalory, TextView Price) {
        //https://api.spoonacular.com/recipes/716429/information?includeNutrition=true&apiKey=7992174586184c75bc46188438e23e9b


        imgUrl = apiModalClass.getImgURL() +"recipeImages/"+ ID + apiModalClass.getSize();

        final String url = apiModalClass.getURL_Prefix() + ID + apiModalClass.getSearchURL() +"&"+apiModalClass.getAPI_KEY();

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

    public StringRequest jokeStringRequest() {

        final String url;
        url = apiModalClass.getURL_JOKE_Prefix() +"?"+apiModalClass.getAPI_KEY();
        Log.d(TAG,url);

        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);

                            String Joketext = result.get("text").toString();
                            Log.d(TAG,url);
                            Log.d(TAG,Joketext);
                            TextView Recipes,Trending,MealPlan;

                            TextView joke = mView.findViewById(R.id.joke);
                            Recipes = mView.findViewById(R.id.HomeTitle);
                            Trending = mView.findViewById(R.id.HomeTrending);
                            MealPlan = mView.findViewById(R.id.HomeMealPlan);
                            MaterialCardView jokeCard = mView.findViewById(R.id.jokeCard);
                            joke.setText("\""+Joketext+"\"");

                            mshimmerFrameLayout = mView.findViewById(R.id.shimmer_view_container);

                            mshimmerFrameLayout.stopShimmer();
                            mshimmerFrameLayout.setVisibility(View.GONE);

                            Recipes.setVisibility(View.VISIBLE);
                            Trending.setVisibility(View.VISIBLE);
                            MealPlan.setVisibility(View.VISIBLE);
                            jokeCard.setVisibility(View.VISIBLE);

                            joke.setText("\""+Joketext+"\"");

                            mshimmerFrameLayout.stopShimmer();

                            mshimmerFrameLayout.setVisibility(View.GONE);

                            Recipes.setVisibility(View.VISIBLE);
                            Trending.setVisibility(View.VISIBLE);
                            MealPlan.setVisibility(View.VISIBLE);
                            jokeCard.setVisibility(View.VISIBLE);

                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
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
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(mView.getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }



    public StringRequest trendingStringRequest(final VolleyCallback volleyCallback) {
        final String number = "?number=5";
        final String url;
        url = apiModalClass.getURL_Prefix()+"random"+ number +"&"+apiModalClass.getAPI_KEY();
        Log.d(TAG,url);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            JSONArray resultArry = result.getJSONArray("recipes");

                            volleyCallback.onSuccess(resultArry);


                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(mView.getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }

    public StringRequest planStringRequest(VolleyCallback volleyMealCallback) {
        final String URL_PREFIX = "https://api.spoonacular.com/mealplanner/generate?timeFrame=day";
        final String url;
        url = URL_PREFIX +"&"+apiModalClass.getAPI_KEY();
        Log.d(TAG,url);
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // 3rd param - method onResponse lays the code procedure of success return
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            JSONArray resultArry = result.getJSONArray("meals");

                            volleyMealCallback.onSuccess2(resultArry);


                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(mView.getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }

    public boolean Connected() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) mView.getContext().getSystemService(mView.getContext().CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public StringRequest searchNameStringRequest(String nameSearch,VolleyCallback volleyCallback) {

        final String NAME_SEARCH = "search?query=";
        final String number = "&number=10";
        final String url;
        url = apiModalClass.getURL_Prefix() + NAME_SEARCH + nameSearch + number +"&"+apiModalClass.getAPI_KEY();
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


                            volleyCallback.onSuccess(resultArry);



                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            TextView textView = mView.findViewById(R.id.notFound);

                            Toast.makeText(mView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            textView.setVisibility(View.VISIBLE);
                            Log.d(TAG,Objects.requireNonNull(e.getMessage()));
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
                        if (error instanceof NetworkError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ServerError) {
                            message = "The server could not be found. Please try again after some time!!";
                        } else if (error instanceof AuthFailureError) {
                            message = "Cannot connect to Internet...Please check your connection!";
                        } else if (error instanceof ParseError) {
                            message = "Parsing error! Please try again after some time!!";
                        } else if (error instanceof TimeoutError) {
                            message = "Connection TimeOut! Please check your internet connection.";
                        }
                        Toast.makeText(mView.getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }

}
