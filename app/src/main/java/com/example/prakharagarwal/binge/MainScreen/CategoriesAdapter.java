package com.example.prakharagarwal.binge.MainScreen;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prakharagarwal on 25/05/18.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder> {

    public List<Integer> category_number;
    public List<String> category_string;
    private List<Category1> categories;
    final private Activity mContext;


    CategoriesAdapter.CategoriesAdapterViewHolder holder;
    private boolean headerEnabled;

    int color[] = new int[]{Color.parseColor("#FF4500"), Color.parseColor("#00cccc"), Color.parseColor("#ff70a6"),
            Color.parseColor("#16b886"), Color.parseColor("#8A2BE2"), Color.parseColor("#DC143C"),
            Color.parseColor("#DA70D6"), Color.parseColor("#FF6347"), Color.parseColor("#DAA520"),
            Color.parseColor("#1E90FF"),Color.parseColor("#D2691E"),Color.parseColor("#FF4500"), Color.parseColor("#00cccc"), Color.parseColor("#ff70a6"),
            Color.parseColor("#16b886"), Color.parseColor("#8A2BE2"), Color.parseColor("#DC143C"),
            Color.parseColor("#DA70D6"), Color.parseColor("#FF6347"), Color.parseColor("#DAA520"),
            Color.parseColor("#1E90FF"),Color.parseColor("#D2691E")};

    public CategoriesAdapter(List<Category1> categories, Activity mContext,List<String> category_string,List<Integer> category_number) {
        this.categories = categories;
        this.mContext = mContext;
        this.category_string=category_string;
        this.category_number=category_number;

    }


    @Override
    public CategoriesAdapter.CategoriesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false);
        holder = new CategoriesAdapter.CategoriesAdapterViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(CategoriesAdapter.CategoriesAdapterViewHolder holder, int position) {

        holder.carditem.setBackgroundColor(color[position]);

     //  holder.totalItem.setText(String.valueOf(category1.getItem()));

            if(categories.get(position).getItem()!=0)
            {
                holder.name.setText(categories.get(position).getCategory_name());
                Picasso.with(mContext).load(categories.get(position).getLogo_url()).into(holder.image);
                holder.totalItem.setText(categories.get(position).getItem()+"");
            }


    }


    @Override
    public int getItemCount() {

        return categories.size();
    }

    public class CategoriesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView name,totalItem;
        public CardView cardView;
        public ConstraintLayout carditem;

        public CategoriesAdapterViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.category_image);
            name = (TextView) itemView.findViewById(R.id.category_name);
            totalItem=itemView.findViewById(R.id.category_total_item);
            carditem = itemView.findViewById(R.id.carditem_layout);
            cardView = itemView.findViewById(R.id.category_cardview);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Category1 category1 = categories.get(getAdapterPosition());
            Intent intent = new Intent(mContext, RestaurantActivity.class);
            intent.putExtra("category", categories.get(getAdapterPosition()).getCategory_id());
            // intent.putExtra("mfood",mfood);
            mContext.startActivity(intent);
        }
    }


    public void addAll(List<Category1> categories) {
        this.categories = categories;

    }
}
