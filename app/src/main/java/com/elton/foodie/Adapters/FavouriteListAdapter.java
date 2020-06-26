package com.elton.foodie.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elton.foodie.DB.Favourite;
import com.elton.foodie.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavouriteListAdapter extends RecyclerView.Adapter<FavouriteListAdapter.FavouriteViewHolder> {

    private List<Favourite>FavouriteList;
    private final LayoutInflater mInflater;

    private Context mcontext;
    public FavouriteListAdapter(Context context){mInflater = LayoutInflater.from(context);
    this.mcontext=context;}
    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.dish_row,parent,false);

        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
            if (FavouriteList != null){
                Favourite current = FavouriteList.get(position);
                holder.title.setText(current.getTitle());
                holder.time.setText(current.getReadyTime());
                Picasso.get().load(current.getImgUrl()).into(holder.dishImage);
            }else {
            }
    }

    public void setFavouriteList(List<Favourite> favouriteList){
        FavouriteList = favouriteList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (FavouriteList != null)
            return  FavouriteList.size();
        else return 0;
    }

    public void removeItem(int position){
        FavouriteList.remove(position);

        notifyItemRemoved(position);
    }

    public void restoreItem(Favourite favourite,int position){
        FavouriteList.add(position,favourite);
        notifyItemInserted(position);
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder{

        private final TextView title,time;
        private final ImageView dishImage;
        public RelativeLayout viewBackground, viewForeground;
    public FavouriteViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.dishName);
        time = itemView.findViewById(R.id.dishTime);
        dishImage = itemView.findViewById(R.id.DishImage);
        viewBackground = itemView.findViewById(R.id.view_background);
        viewForeground = itemView.findViewById(R.id.view_foreground);
    }
}
}
