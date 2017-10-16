package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.StoriesMenu.StoriesActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Sarthak on 06-10-2017.
 */

public class RecommendAdapter extends RecyclerView.Adapter<RecommendAdapter.RecommendAdapterViewHolder> {

    private List<Recommend> mRecommend;

    final private Context mContext;
    String dish;
    String restaurant;
    String image_url;
    RecommendAdapterViewHolder holder;
    LinearLayout container;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private int pos;

    public RecommendAdapter(List<Recommend> mRecommend, Context mContext, RecyclerView recyclerView, FragmentManager fragmentManager, LinearLayoutManager linearLayoutManager) {
        this.mRecommend = mRecommend;
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public RecommendAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recommended, parent, false);
        container = (LinearLayout) view.findViewById(R.id.container);
        holder = new RecommendAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecommendAdapterViewHolder holder, int position) {
        Log.i("TAG", "In Recommend On bind View Holder");
         dish = mRecommend.get(position).getDishName();
         restaurant = mRecommend.get(position).getRestaurantName();
         image_url  = mRecommend.get(position).getImage();
        pos=position;
//
//            if (mRecommend.get(position).getVeg() == 0) {
//                holder.imageViewVeg.setImageResource(R.mipmap.veg);
//            } else {
//                holder.imageViewVeg.setImageResource(R.mipmap.nonveg);
//            }


       holder.restaurantName.setText(restaurant);
       holder.dishName.setText(dish);

       Picasso.with(mContext)
                .load(image_url)
                .into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        return mRecommend.size();
    }

    public class RecommendAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView restaurantName;
        TextView dishName;
        ImageView imageview;
        ImageView imageViewVeg;

        public RecommendAdapterViewHolder(View itemView) {
             super(itemView);

            restaurantName = (TextView)itemView.findViewById(R.id.restaurant_name_recommended);
            dishName = (TextView)itemView.findViewById(R.id.name_dish_recommended);
            imageViewVeg = (ImageView)itemView.findViewById(R.id.veg_image_recommended);
            imageview = (ImageView)itemView.findViewById(R.id.imageview_recommended);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Intent intent = new Intent(mContext, StoriesActivity.class);
            intent.putExtra("restaurantID", mRecommend.get(getAdapterPosition()).getRestaurantID());
            intent.putExtra("restaurantName", mRecommend.get(getAdapterPosition()).getRestaurantName());
            intent.putExtra("dishName",mRecommend.get(getAdapterPosition()).getDishName());
            mContext.startActivity(intent);
            // Can play the video of the food.
        }
    }

    public void removeAll() {
        mRecommend.clear();
    }

    public void addAll(List<Recommend> recommends) {
        mRecommend = recommends;
    }

}
