package com.elton.foodie.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elton.foodie.Modals.MealPlanModalClass;
import com.elton.foodie.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealPlanAdapter extends RecyclerView.Adapter<MealPlanAdapter.PlanHolder> {

    private List<MealPlanModalClass> PlanList;

    public MealPlanAdapter(List<MealPlanModalClass> planList) {
        PlanList = planList;
    }

    private View.OnClickListener mOnItemClickListener;


    @NonNull
    @Override
    public PlanHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_plan_row,parent,false);

        return new PlanHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanHolder holder, int position) {
        MealPlanModalClass mealPlanModalClass = PlanList.get(position);
        Picasso.get().load(mealPlanModalClass.getDishImage()).into(holder.mealImg);
        holder.mealName.setText(mealPlanModalClass.getDishName());
        holder.mealTime.setText(mealPlanModalClass.getDishTime());
        holder.mealType.setText(mealPlanModalClass.getDishType());
    }

    @Override
    public int getItemCount() {
        return PlanList.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }


    public class PlanHolder extends RecyclerView.ViewHolder {
        public TextView mealName, mealTime,mealType;
        public ImageView mealImg;

        public PlanHolder(@NonNull View itemView) {
            super(itemView);
            mealName = itemView.findViewById(R.id.dishName2);
            mealTime = itemView.findViewById(R.id.dishTime2);
            mealImg = itemView.findViewById(R.id.DishImage2);
            mealType = itemView.findViewById(R.id.dishType);

            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
}
