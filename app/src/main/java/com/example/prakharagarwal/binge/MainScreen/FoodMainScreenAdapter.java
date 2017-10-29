package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sarthak on 28-10-2017.
 */

public class FoodMainScreenAdapter extends RecyclerView.Adapter<FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder> {

    private List<Food_MainScreen> mFood;
    final private Context mContext;
    String dish_id;
    String rest_id;
    String poster_url;
    String rest_name;

//    GridLayoutManager container;
    FoodMainScreenAdapterViewHolder holder;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;

    public FoodMainScreenAdapter(List<Food_MainScreen> mFood, Context mContext, RecyclerView recyclerView, FragmentManager fragmentManager){
        this.mFood = mFood;
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;

    }
    @Override
    public FoodMainScreenAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_main_screen, parent, false);
        holder = new FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FoodMainScreenAdapterViewHolder holder, int position) {
        dish_id = mFood.get(position).getDish_id();
        rest_id = mFood.get(position).getRest_id();
        rest_name = mFood.get(position).getRest_name();
        poster_url = mFood.get(position).getPoster_url();

        holder.dishName.setText(dish_id);
        holder.restaurantName.setText(rest_name);

        Picasso.with(mContext)
                .load(poster_url)
                .into(holder.imageview);



    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }

    public class FoodMainScreenAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView restaurantName;
        TextView dishName;
        ImageView imageview;
        ImageView imageViewRating;

        public FoodMainScreenAdapterViewHolder(View itemView) {
            super(itemView);

            restaurantName = (TextView) itemView.findViewById(R.id.name_rest_mainScreen);
            dishName = (TextView) itemView.findViewById(R.id.dish_name_mainscreen);
            imageViewRating = (ImageView) itemView.findViewById(R.id.rating_mainScreen);
            imageview = (ImageView) itemView.findViewById(R.id.imageview_food_mainScreen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeAll() {
        mFood.clear();
    }

    public void addAll(List<Food_MainScreen> food) {
        mFood = food;
    }
}
