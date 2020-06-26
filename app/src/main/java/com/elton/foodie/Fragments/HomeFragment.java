package com.elton.foodie.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elton.foodie.MainActivity;
import com.elton.foodie.Adapters.MealPlanAdapter;
import com.elton.foodie.Modals.MealPlanModalClass;
import com.elton.foodie.Networking.SpoonacularRequests;
import com.elton.foodie.R;
import com.elton.foodie.Adapters.TrendingAdapter;
import com.elton.foodie.Modals.TrendingModalClass;
import com.elton.foodie.Utils.VolleyCallback;
import com.facebook.shimmer.ShimmerFrameLayout;
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
    private List<TrendingModalClass> TrendingList = new ArrayList<>();
    private List<MealPlanModalClass> PlanList = new ArrayList<>();
    private List<String> MealTypes = new ArrayList<>();


    private View view;
    private static final String EXTRA_DISH_KEY = "MY KEY FOR DISH";
    private ShimmerFrameLayout mshimmerFrameLayout;

    private static final String TAG = "FragmentHome";

    private View.OnClickListener onItemClickListenerTrending = view -> {
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
    };
    private View.OnClickListener onItemClickListenerPlan = view -> {
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        view = root;
        SpoonacularRequests spoonacularRequests = new SpoonacularRequests(root);

        mshimmerFrameLayout = root.findViewById(R.id.shimmer_view_container);

        RelativeLayout relativeLayout= root.findViewById(R.id.connectionHome);
        if (spoonacularRequests.Connected()) {
            relativeLayout.setVisibility(View.INVISIBLE);


            NestedScrollView nestedScrollView = root.findViewById(R.id.homeContent);
            nestedScrollView.setVisibility(View.VISIBLE);
            StringRequest stringRequest = spoonacularRequests.jokeStringRequest();
            // executing the request (adding to queue)
            queue.add(stringRequest);


            //Calling the trending meals function
            VolleyCallback volleyCallback = new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) throws JSONException {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject recipe = jsonArray.getJSONObject(i);

                        Uri uri = Uri.parse("https://spoonacular.com/recipeImages/" + recipe.get("id").toString() + "-556x370.jpg");
                        TrendingModalClass trendingModalClass = new TrendingModalClass(recipe.get("title").toString(), recipe.getString("readyInMinutes"), uri, recipe.getString("id"));
                        TrendingList.add(trendingModalClass);

                    }
                    initTrendingRecyclerview();
                    Log.e(TAG, Integer.toString(TrendingList.size()));

                }

                @Override
                public void onSuccess(JSONObject jsonObject) {

                }

                @Override
                public void onSuccess2(JSONArray jsonArray) {

                }
            };

            StringRequest trendingStringRequest = spoonacularRequests.trendingStringRequest(volleyCallback);

            Log.e(TAG, "url");
            // executing the request (adding to queue)
            queue.add(trendingStringRequest);

            // Calling the Meal plan function
            MealTypes.add("Breakfast");
            MealTypes.add("Lunch");
            MealTypes.add("Dinner");
            VolleyCallback volleyMealCallback = new VolleyCallback() {
                @Override
                public void onSuccess(JSONArray jsonArray) {

                }

                @Override
                public void onSuccess(JSONObject jsonObject) {

                }

                @Override
                public void onSuccess2(JSONArray jsonArray) throws JSONException {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject recipe = jsonArray.getJSONObject(i);

                        Uri uri = Uri.parse("https://spoonacular.com/recipeImages/" + recipe.get("id").toString() + "-556x370.jpg");
                        MealPlanModalClass mealPlanModalClass = new MealPlanModalClass(recipe.get("title").toString(), recipe.getString("readyInMinutes"), uri, recipe.getString("id"), MealTypes.get(i));
                        PlanList.add(mealPlanModalClass);

                    }
                    initMealRecyclerview();
                    Log.e(TAG, Integer.toString(PlanList.size()));
                }
            };

            StringRequest mealStringRequest = spoonacularRequests.planStringRequest(volleyMealCallback);
            Log.e(TAG, "url");

            queue.add(mealStringRequest);
        }else {
               relativeLayout.setVisibility(View.VISIBLE);
        }
        return root;
    }


    private void initTrendingRecyclerview(){
        RecyclerView recyclerView = view.findViewById(R.id.TrendingDish);
        TrendingAdapter mAdapter = new TrendingAdapter(TrendingList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListenerTrending);
        Log.d(TAG,Integer.toString(TrendingList.size()));
    }

    private void initMealRecyclerview(){

        RecyclerView recyclerView = view.findViewById(R.id.MealPlanDish);
        MealPlanAdapter mAdapter = new MealPlanAdapter(PlanList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListenerPlan);
        Log.d(TAG,Integer.toString(PlanList.size()));
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
