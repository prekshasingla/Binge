package com.example.prakharagarwal.binge.MainScreen;

import android.app.Activity;
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
 * Created by prakharagarwal on 25/05/18.
 */

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesAdapterViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    private List<Category1> categories;
    final private Activity mContext;

    CategoriesAdapter.CategoriesAdapterViewHolder holder;
    private boolean headerEnabled;

    public CategoriesAdapter(List<Category1> categories, Activity mContext) {
        this.categories = categories;
        this.mContext = mContext;

    }



    @Override
    public CategoriesAdapter.CategoriesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false);
        holder = new CategoriesAdapter.CategoriesAdapterViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(CategoriesAdapter.CategoriesAdapterViewHolder holder, int position) {
        Category1 category1= categories.get(position);
        Picasso.with(mContext)
                .load(category1.getLogo_url())
                .into(holder.image);
        holder.name.setText(category1.getCategory_name());
    }



    @Override
    public int getItemCount() {

        return categories.size();
    }


    public class CategoriesAdapterViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;

        public CategoriesAdapterViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.category_image);
            name = (TextView) itemView.findViewById(R.id.category_name);
        }
    }


    public void addAll(List<Category1> categories) {
        this.categories = categories;
    }
}
