package com.elton.foodie.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elton.foodie.Modals.DishResultModalClass;
import com.elton.foodie.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.DishViewHolder> {

    private List<DishResultModalClass> dishList;
    private View.OnClickListener mOnItemClickListener;
    public SearchResultAdapter(List<DishResultModalClass> dishtList) {
        this.dishList = dishtList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_row,parent,false);

        return new DishViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        DishResultModalClass dishResultModalClass = dishList.get(position);
        Picasso.get().load(dishResultModalClass.getDishImage()).into(holder.dishImg);
        holder.title.setText(dishResultModalClass.getDishName());
        holder.time.setText(dishResultModalClass.getDishTime());
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class DishViewHolder extends RecyclerView.ViewHolder {
        public TextView title,time;
        public ImageView dishImg;
        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dishName);
            time = itemView.findViewById(R.id.dishTime);
            dishImg = itemView.findViewById(R.id.DishImage);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
}




