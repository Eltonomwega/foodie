package com.elton.foodie.Fragments;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elton.foodie.MainActivity;
import com.elton.foodie.Adapters.MealPlanAdapter;
import com.elton.foodie.Modals.MealPlanModalClass;
import com.elton.foodie.R;
import com.elton.foodie.Adapters.TrendingAdapter;
import com.elton.foodie.Modals.TrendingModalClass;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<TrendingModalClass> TrendingList = new ArrayList<>();
    private List<MealPlanModalClass> PlanList = new ArrayList<>();
    private List<String> MealTypes = new ArrayList<>();


    VolleyCallback volleyCallback,volleyMealCallback;
    public String message;
    View viewGroup;
    private RequestQueue queue;
    public static final String EXTRA_DISH_KEY = "MY KEY FOR DISH";
    private ShimmerFrameLayout mshimmerFrameLayout;

    private static final String TAG = "FragmentHome";

    private View.OnClickListener onItemClickListenerTrending = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            TrendingModalClass thisItem = TrendingList.get(position);

            Toast.makeText(getContext(), "You Clicked: " + thisItem.getDishId(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra(EXTRA_DISH_KEY,thisItem.getDishId());
            startActivity(i);
        }
    };
    private View.OnClickListener onItemClickListenerPlan = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            MealPlanModalClass thisItem = PlanList.get(position);

            Toast.makeText(getContext(), "You Clicked: " + thisItem.getDishId(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra(EXTRA_DISH_KEY,thisItem.getDishId());
            startActivity(i);
        }
    };
    public HomeFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queue = Volley.newRequestQueue(getContext());
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        viewGroup = root;
        mshimmerFrameLayout = root.findViewById(R.id.shimmer_view_container);

        RelativeLayout relativeLayout= root.findViewById(R.id.connectionHome);
        if (haveNetworkConnection()) {
            relativeLayout.setVisibility(View.INVISIBLE);


            NestedScrollView nestedScrollView = root.findViewById(R.id.homeContent);
            nestedScrollView.setVisibility(View.VISIBLE);
            StringRequest stringRequest = jokeStringRequest();
            // executing the request (adding to queue)
            queue.add(stringRequest);


            //Calling the trending meals function
            volleyCallback = new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) throws JSONException {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject recipe = jsonArray.getJSONObject(i);

                        Uri uri = Uri.parse("https://spoonacular.com/recipeImages/" + recipe.get("id").toString() + "-556x370.jpg");
                        TrendingModalClass trendingModalClass = new TrendingModalClass(recipe.get("title").toString(), recipe.getString("readyInMinutes"), uri, recipe.getString("id"));
                        TrendingList.add(trendingModalClass);

                    }
                    Log.e(TAG, Integer.toString(TrendingList.size()));

                }

                @Override
                public void onSuccess2(JSONArray jsonArray) throws JSONException {

                }
            };

            StringRequest trendingStringRequest = trendingStringRequest();
            Log.e(TAG, "url");
            // executing the request (adding to queue)
            queue.add(trendingStringRequest);

            // Calling the Meal plan function
            MealTypes.add("Breakfast");
            MealTypes.add("Lunch");
            MealTypes.add("Dinner");
            volleyMealCallback = new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) throws JSONException {

                }

                @Override
                public void onSuccess2(JSONArray jsonArray) throws JSONException {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject recipe = jsonArray.getJSONObject(i);

                        Uri uri = Uri.parse("https://spoonacular.com/recipeImages/" + recipe.get("id").toString() + "-556x370.jpg");
                        MealPlanModalClass mealPlanModalClass = new MealPlanModalClass(recipe.get("title").toString(), recipe.getString("readyInMinutes"), uri, recipe.getString("id"), MealTypes.get(i));
                        PlanList.add(mealPlanModalClass);

                    }
                    Log.e(TAG, Integer.toString(PlanList.size()));
                }
            };

            StringRequest mealStringRequest = planStringRequest();
            Log.e(TAG, "url");

            queue.add(mealStringRequest);
        }else {
               relativeLayout.setVisibility(View.VISIBLE);
        }
        return root;
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
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

    public void initTrendingRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.TrendingDish);
        TrendingAdapter mAdapter = new TrendingAdapter(TrendingList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListenerTrending);
        Log.d(TAG,Integer.toString(TrendingList.size()));
    }

    public void initMealRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.MealPlanDish);
        MealPlanAdapter mAdapter = new MealPlanAdapter(PlanList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListenerPlan);
        Log.d(TAG,Integer.toString(PlanList.size()));
    }




    private StringRequest jokeStringRequest() {
        // url = url + id + "/information?includeNutrition=false&apiKey=db9a577a695640528bb7a67487f8a907";
        //https://spoonacular.com/recipeImages/579247-556x370.jpg
                //7992174586184c75bc46188438e23e9b
        final String API = "?apiKey=db9a577a695640528bb7a67487f8a907";
        final String URL_PREFIX = "https://api.spoonacular.com/food/jokes/random";
        final String url;
        url = URL_PREFIX + API;
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

                            TextView joke = viewGroup.findViewById(R.id.joke);
                            Recipes = viewGroup.findViewById(R.id.HomeTitle);
                            Trending = viewGroup.findViewById(R.id.HomeTrending);
                            MealPlan = viewGroup.findViewById(R.id.HomeMealPlan);
                            MaterialCardView jokeCard = viewGroup.findViewById(R.id.jokeCard);
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
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }

    public interface VolleyCallback{

        void onSuccess(JSONArray jsonArray) throws JSONException;
        void onSuccess2(JSONArray jsonArray)throws JSONException;
    }


    private StringRequest trendingStringRequest() {
        // url = url + id + "/information?includeNutrition=false&apiKey=db9a577a695640528bb7a67487f8a907";
        //https://spoonacular.com/recipeImages/579247-556x370.jpg
        final String API = "&apiKey=db9a577a695640528bb7a67487f8a907";
        final String number = "?number=5";
        final String URL_PREFIX = "https://api.spoonacular.com/recipes/random";
        final String url;
        url = URL_PREFIX + number + API;
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

                            initTrendingRecyclerview();

                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }



    private StringRequest planStringRequest() {
        // url = url + id + "/information?includeNutrition=false&apiKey=db9a577a695640528bb7a67487f8a907";
        //https://spoonacular.com/recipeImages/579247-556x370.jpg
        final String API = "&apiKey=db9a577a695640528bb7a67487f8a907";
        final String URL_PREFIX = "https://api.spoonacular.com/mealplanner/generate?timeFrame=day";
        final String url;
        url = URL_PREFIX + API;
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

                            initMealRecyclerview();

                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                        //Log.e(TAG,error.getMessage());

                    }
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        mshimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        mshimmerFrameLayout.stopShimmer();
        super.onPause();
    }
}
