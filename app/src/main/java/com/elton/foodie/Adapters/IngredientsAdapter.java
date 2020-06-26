package com.elton.foodie.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elton.foodie.Modals.IngredientsModalClass;
import com.elton.foodie.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    private List<IngredientsModalClass> ingredientList;

    public IngredientsAdapter(List<IngredientsModalClass> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_row,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        IngredientsModalClass ingredientsModalClass = ingredientList.get(position);
        Picasso.get().load(ingredientsModalClass.getIngredientImage()).into(holder.ingImg);
        holder.ingName.setText(ingredientsModalClass.getIngredientName());
        holder.ingQuantity.setText(ingredientsModalClass.getIngredientQuantity());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ingName, ingQuantity;
        public ImageView ingImg;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingName =  itemView.findViewById(R.id.ingName);
            ingQuantity =  itemView.findViewById(R.id.ingQuantity);
            ingImg =  itemView.findViewById(R.id.ingImg);

        }
    }
}
