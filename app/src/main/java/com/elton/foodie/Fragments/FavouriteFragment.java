package com.elton.foodie.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.elton.foodie.Adapters.FavouriteListAdapter;
import com.elton.foodie.Adapters.RecyclerItemTouchHelper;
import com.elton.foodie.DB.Favourite;
import com.elton.foodie.DB.FoodieViewModel;
import com.elton.foodie.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FoodieViewModel foodieViewModel;
    private List<Favourite> mfavourites;
    private FavouriteListAdapter adapter;
    FrameLayout frameLayout;
    public FavouriteFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View root = inflater.inflate(R.layout.fragment_favourite, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.favouriteDishes);
        frameLayout = root.findViewById(R.id.frag_fav);
                adapter = new FavouriteListAdapter(getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        foodieViewModel = new ViewModelProvider(this).get(FoodieViewModel.class);

        foodieViewModel.getAllFavourites().observe(getViewLifecycleOwner(), new Observer<List<Favourite>>() {
            @Override
            public void onChanged(List<Favourite> favourites) {
                mfavourites = favourites;
                adapter.setFavouriteList(favourites);
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        return root;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        List<Favourite> favourites;
        if (viewHolder instanceof FavouriteListAdapter.FavouriteViewHolder) {
            // get the removed item name to display it in snack bar
            String name = mfavourites.get(viewHolder.getAdapterPosition()).getTitle();

            // backup of removed item for undo purpose
            final Favourite deletedItem = mfavourites.get(viewHolder.getAdapterPosition());

            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            foodieViewModel.delete(deletedItem);
            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(frameLayout, name + " removed from favourite!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                    foodieViewModel.insert(deletedItem);
                }
            });
            snackbar.setBackgroundTint(Color.BLACK);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setActionTextColor(Color.GREEN);
            snackbar.show();
        }
    }

}
