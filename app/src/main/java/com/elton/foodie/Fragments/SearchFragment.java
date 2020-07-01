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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.elton.foodie.Modals.DishResultModalClass;
import com.elton.foodie.Activities.MainActivity;
import com.elton.foodie.Networking.SpoonacularRequests;
import com.elton.foodie.R;
import com.elton.foodie.Adapters.SearchResultAdapter;
import com.elton.foodie.Utils.VolleyCallback;

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
    private List<DishResultModalClass> dishList = new ArrayList<>();
    private View view;
    private static final String TAG = "FragmentSearch";
    private VolleyCallback volleyCallback;
    private RequestQueue queue;
    private TextView textView;
    public static final String EXTRA_DISH_KEY = "MY KEY FOR DISH";

    private View.OnClickListener onItemClickListener = view -> {
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        queue = Volley.newRequestQueue(requireContext());
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        view = root;
        SpoonacularRequests spoonacularRequests = new SpoonacularRequests(root);
        textView = view.findViewById(R.id.notFound);
        final EditText SearchBar = root.findViewById(R.id.searchText);

        SearchBar.setOnKeyListener((view, keyCode, keyevent) -> {
            if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                textView.setVisibility(View.GONE);
                volleyCallback = new VolleyCallback() {
                    final String IMG_PREFIX = "https://spoonacular.com/";
                    final String size = "-556x370.jpg";

                    @Override
                    public void onSuccess(JSONObject jsonObject) {

                    }

                    @Override
                    public void onSuccess2(JSONArray jsonArray) {

                    }

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
                        initRecyclerview();
                        Log.d(TAG,Integer.toString(dishList.size()));
                    }
                };

                StringRequest stringRequest = spoonacularRequests.searchNameStringRequest(SearchBar.getText().toString(),volleyCallback);
                Log.e(TAG,"url");
                // executing the request (adding to queue)
                queue.add(stringRequest);
                Toast.makeText(getContext(), "pressed", Toast.LENGTH_LONG).show();

                return true;
            }
            return false;
        });
        return root;


    }

    private void initRecyclerview(){
        RecyclerView recyclerView = view.findViewById(R.id.searchResults);
        SearchResultAdapter mAdapter = new SearchResultAdapter(dishList);
        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(onItemClickListener);
        Log.d(TAG,Integer.toString(dishList.size()));
    }


}
