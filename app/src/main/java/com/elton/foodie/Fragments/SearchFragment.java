package com.elton.foodie.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.elton.foodie.Modals.DishResultModalClass;
import com.elton.foodie.MainActivity;
import com.elton.foodie.R;
import com.elton.foodie.Adapters.SearchResultAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<DishResultModalClass> dishList = new ArrayList<>();
    View viewGroup;
    public String message;
    private static final String TAG = "FragmentSearch";
    VolleyCallback volleyCallback;
    private RequestQueue queue;
    TextView textView;
    public static final String EXTRA_DISH_KEY = "MY KEY FOR DISH";

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //TODO: Step 4 of 4: Finally call getTag() on the view.
            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            // viewHolder.getItemId();
            // viewHolder.getItemViewType();
            // viewHolder.itemView;
            DishResultModalClass thisItem = dishList.get(position);
            Toast.makeText(getContext(), "You Clicked: " + thisItem.getDishId(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra(EXTRA_DISH_KEY,thisItem.getDishId());
            startActivity(i);
        }
    };

    public SearchFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        viewGroup = root;
        textView = viewGroup.findViewById(R.id.notFound);
        final EditText SearchBar = root.findViewById(R.id.searchText);

        SearchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    textView.setVisibility(View.GONE);
                    volleyCallback = new VolleyCallback() {
                        final String IMG_PREFIX = "https://spoonacular.com/";
                        final String size = "-556x370.jpg";
                        @Override
                        public void onSuccess(JSONArray jsonArray) throws JSONException {
                            dishList.clear();
                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String ID = jsonObject.get("id").toString();

                                String urlImg = IMG_PREFIX +"recipeImages/"+ ID + size;

                                Uri uri = Uri.parse(urlImg);
                                DishResultModalClass dishResultModalClass = new DishResultModalClass(jsonObject.getString("title"), jsonObject.getString("readyInMinutes")+" Min",uri,jsonObject.getString("id"));
                                dishList.add(dishResultModalClass);

                                Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();
                                Log.d(TAG,urlImg);
                            }
                            Log.d(TAG,Integer.toString(dishList.size()));
                        }
                    };

                    StringRequest stringRequest = searchNameStringRequest(SearchBar.getText().toString());
                    Log.e(TAG,"url");
                    // executing the request (adding to queue)
                    queue.add(stringRequest);
                    Toast.makeText(getContext(), "pressed", Toast.LENGTH_LONG).show();

                    return true;
                }
                return false;
            }
        });
        return root;


    }



    public void initRecyclerview(){
        RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.searchResults);
        SearchResultAdapter mAdapter = new SearchResultAdapter(dishList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);
        Log.d(TAG,Integer.toString(dishList.size()));
    }
    public interface VolleyCallback{

        void onSuccess(JSONArray jsonArray) throws JSONException;
    }

    private StringRequest searchNameStringRequest(String nameSearch) {
        // url = url + id + "/information?includeNutrition=false&apiKey=db9a577a695640528bb7a67487f8a907";
        //https://spoonacular.com/recipeImages/579247-556x370.jpg
        final String API = "&apiKey=db9a577a695640528bb7a67487f8a907";
        final String NAME_SEARCH = "/search?query=";
        final String number = "&number=10";
        final String URL_PREFIX = "https://api.spoonacular.com/recipes";
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


                            volleyCallback.onSuccess(resultArry);

                            initRecyclerview();

                            //gets each JSON object within the JSON array

                            // catch for the JSON parsing error
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            textView.setVisibility(View.VISIBLE);
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
}
