package com.example.prakharagarwal.binge.MainScreen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by prakharagarwal on 10/05/18.
 */

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.BrandsAdapterViewHolder> {

    private static final int ITEM_VIEW_TYPE_HEADER = 1;
    private static final int ITEM_VIEW_TYPE_ITEM = 2;
    private List<Brand> brands;
    final private Activity mContext;

    BrandsAdapter.BrandsAdapterViewHolder holder;
    private boolean headerEnabled;

    public BrandsAdapter(List<Brand> brands, Activity mContext) {
        this.brands = brands;
        this.mContext = mContext;

    }



    @Override
    public BrandsAdapter.BrandsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.brands_item, parent, false);
        holder = new BrandsAdapter.BrandsAdapterViewHolder(view);
        return holder;

    }


    @Override
    public void onBindViewHolder(BrandsAdapter.BrandsAdapterViewHolder holder, int position) {
        Brand brand= brands.get(position);
        Picasso.with(mContext)
                .load(brand.getLogo_url())
                .into(holder.image);
      //  holder.name.setText(brand.getRestaurant_name());

    }



    @Override
    public int getItemCount() {

        return brands.size();
    }


    public class BrandsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView name;

        public BrandsAdapterViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.brand_image);
            name = (TextView) itemView.findViewById(R.id.brand_name);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext,DishInfoActivity.class);
            intent.putExtra("rest",brands.get(getAdapterPosition()).getRestaurant_id());
            mContext.startActivity(intent);
        }
    }


    public void addAll(List<Brand> brands) {
        this.brands = brands;
    }
}
