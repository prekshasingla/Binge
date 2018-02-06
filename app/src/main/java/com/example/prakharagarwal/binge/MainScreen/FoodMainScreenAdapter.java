package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.DishRecommend;
import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodMainScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    private List<Food_MainScreen> mFood;
    final private Context mContext;
    String dishName;
    String restaurantID;
    String posterUrl;
    String restaurantName;

    RecyclerView.ViewHolder holder;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;
    private boolean headerEnabled;

    public FoodMainScreenAdapter(List<Food_MainScreen> mFood, Context mContext, RecyclerView recyclerView, FragmentManager fragmentManager) {
        this.mFood = mFood;
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public int getItemViewType(int position) {
        return  isHeader(position)?
                ITEM_VIEW_TYPE_HEADER:ITEM_VIEW_TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        if(isHeaderEnabled())
        return position == 0;
        else
            return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_screen_recommend_header, parent, false);
            if (!headerEnabled)
                view.setVisibility(View.GONE);
            holder = new FoodMainScreenAdapter.MainScreenHeaderViewHolder(view);
            return holder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_main_screen, parent, false);
            holder = new FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeader(position)) {
            if (isHeaderEnabled()) {
                MainScreenHeaderViewHolder holder1 = (MainScreenHeaderViewHolder) holder;
                holder1.nearby_empty_layout.setVisibility(View.VISIBLE);
            } else {
                MainScreenHeaderViewHolder holder1 = (MainScreenHeaderViewHolder) holder;
                holder1.nearby_empty_layout.setVisibility(View.GONE);
            }

        }
        if (holder instanceof FoodMainScreenAdapterViewHolder) {
            FoodMainScreenAdapterViewHolder holder1 = (FoodMainScreenAdapterViewHolder) holder;
            if(isHeaderEnabled())
            position -= 1;
            dishName = mFood.get(position).getDish_id();
            restaurantID = mFood.get(position).getRestaurant_id();
            restaurantName = mFood.get(position).getRestaurant_name();
            posterUrl = mFood.get(position).getPoster_url();

            holder1.dishName.setText(dishName);
            holder1.restaurantName.setText(restaurantName);

            Picasso.with(mContext)
                    .load(posterUrl)
                    .into(holder1.imageView);

        }

    }

    @Override
    public int getItemCount() {
        if(isHeaderEnabled())
        return mFood.size() + 1;
        else
            return mFood.size();
    }

    public void setHeaderEnabled(boolean headerEnabled) {
        this.headerEnabled = headerEnabled;
    }

    public boolean isHeaderEnabled() {
        return headerEnabled;
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

            Intent intent = new Intent(mContext, RestaurantDetailsActivity.class);
            intent.putExtra("restaurantID", mFood.get(getAdapterPosition() - 1).getRestaurant_id());
            intent.putExtra("restaurantName", mFood.get(getAdapterPosition() - 1).getRestaurant_name());
            intent.putExtra("dishName", mFood.get(getAdapterPosition() - 1).getDish_id());
            // Log.i("TAG", mFood.get(getAdapterPosition()).getDishName());
            intent.putExtra("posi", 1);
            mContext.startActivity(intent);
        }
    }

    public class MainScreenHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout nearby_empty_layout;
        Button recommend;

        public MainScreenHeaderViewHolder(View itemView) {
            super(itemView);

            nearby_empty_layout = (RelativeLayout) itemView.findViewById(R.id.nearby_empty_layout);
            recommend = (Button) itemView.findViewById(R.id.recommend_btn);
            recommend.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            SharedPreferences prefs = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
//            prefs.edit().clear();
            String uID = prefs.getString("username", null);
            if (uID == null) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            } else {
                Intent intent = new Intent(mContext, DishRecommend.class);
                mContext.startActivity(intent);
            }
        }
    }


    public void addAll(List<Food_MainScreen> food) {
        mFood = food;
    }
}
