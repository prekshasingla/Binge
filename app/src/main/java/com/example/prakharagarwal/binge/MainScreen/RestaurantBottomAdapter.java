package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
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

/**
 * Created by prakharagarwal on 05/11/17.
 */

public class RestaurantBottomAdapter extends RecyclerView.Adapter<RestaurantBottomAdapter.RestaurantBottomAdapterViewHolder> {

    private List<String> categoryList;
    final private Context mContext;


    //    GridLayoutManager container;
    RestaurantBottomAdapter.RestaurantBottomAdapterViewHolder holder;


    public RestaurantBottomAdapter(List<String> categoryList, Context mContext){
        this.categoryList = categoryList;
        this.mContext = mContext;


    }
    @Override
    public RestaurantBottomAdapter.RestaurantBottomAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_restaurant, parent, false);
        holder = new RestaurantBottomAdapter.RestaurantBottomAdapterViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RestaurantBottomAdapterViewHolder holder, int position) {
        holder.category.setText("Starters");
    }



    @Override
    public int getItemCount() {
//        return categoryList.size();
   return 5;
    }

    public class RestaurantBottomAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView category;


        public RestaurantBottomAdapterViewHolder(View itemView) {
            super(itemView);

            category=(TextView)itemView.findViewById(R.id.bottom_category_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeAll() {
        categoryList.clear();
    }

    public void addAll(List<String> food) {
        categoryList = food;
    }
}

