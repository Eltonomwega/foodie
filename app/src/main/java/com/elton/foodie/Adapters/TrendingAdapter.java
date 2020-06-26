package com.elton.foodie.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elton.foodie.R;
import com.elton.foodie.Modals.TrendingModalClass;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.MealViewHolder> {

    private List<TrendingModalClass> MealList;

    public TrendingAdapter(List<TrendingModalClass> mealList) {
        MealList = mealList;
    }

    private View.OnClickListener mOnItemClickListener;

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trending_dish_row,parent,false);

        return new MealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
        TrendingModalClass trendingModalClass = MealList.get(position);
        Picasso.get().load(trendingModalClass.getTrendingImage()).into(holder.mealImg);
        holder.mealName.setText(trendingModalClass.getTrendingName());
        holder.mealTime.setText(trendingModalClass.getTrendingTime());
    }

    @Override
    public int getItemCount() {
        return MealList.size();
    }
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class MealViewHolder extends RecyclerView.ViewHolder{
        public TextView mealName, mealTime;
        public ImageView mealImg;
        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealName =  itemView.findViewById(R.id.trendingName);
            mealTime =  itemView.findViewById(R.id.trendingTime);
            mealImg =  itemView.findViewById(R.id.trendingImg);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
}
