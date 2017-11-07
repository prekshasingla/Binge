package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodMainScreenAdapter extends RecyclerView.Adapter<FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder> {

    private List<Food_MainScreen> mFood;
    final private Context mContext;
    String dishName;
    String restaurantID;
    String posterUrl;
    String restaurantName;

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
        dishName = mFood.get(position).getDishName();
        restaurantID = mFood.get(position).getRestaurantID();
        restaurantName = mFood.get(position).getRestaurantName();
        posterUrl = mFood.get(position).getPosterUrl();

        holder.dishName.setText(dishName);
        holder.restaurantName.setText(restaurantName);

        Picasso.with(mContext)
                .load(posterUrl)
                .into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return mFood.size();
    }

    public class FoodMainScreenAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView restaurantName;
        TextView dishName;
        ImageView imageView;

        public FoodMainScreenAdapterViewHolder(View itemView) {
            super(itemView);

            restaurantName = (TextView) itemView.findViewById(R.id.name_rest_mainScreen);
            dishName = (TextView) itemView.findViewById(R.id.dish_name_mainscreen);
            imageView = (ImageView) itemView.findViewById(R.id.imageview_food_mainScreen);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(mContext, RestaurantActivity.class);
            intent.putExtra("restaurantID", mFood.get(getAdapterPosition()).getRestaurantID());
            intent.putExtra("restaurantName", mFood.get(getAdapterPosition()).getRestaurantName());
            intent.putExtra("dishName",mFood.get(getAdapterPosition()).getDishName());
           // Log.i("TAG", mFood.get(getAdapterPosition()).getDishName());
            intent.putExtra("posi",1);
            mContext.startActivity(intent);
        }
    }

    public void removeAll() {
        mFood.clear();
    }

    public void addAll(List<Food_MainScreen> food) {
        mFood = food;
    }
}
