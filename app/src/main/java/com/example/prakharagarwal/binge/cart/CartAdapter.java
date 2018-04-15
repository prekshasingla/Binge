package com.example.prakharagarwal.binge.cart;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.MainScreen.RestaurantDetailsActivity;
import com.example.prakharagarwal.binge.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by prakharagarwal on 07/03/18.
 */

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartAdapterViewHolder> {
    final private Context mContext;
    public List<CartItem> cartList;

    CartAdapter.CartAdapterViewHolder holder;



    public CartAdapter(Context context,List<CartItem> cartList) {
        mContext = context;
        this.cartList =cartList;
    }


    @Override
    public CartAdapter.CartAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_layout, parent, false);
        holder = new CartAdapter.CartAdapterViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final CartAdapter.CartAdapterViewHolder holder, final int position) {

        holder.name.setText(cartList.get(position).getName());
        holder.rate.setText(cartList.get(position).getQty()+" X "+cartList.get(position).getPrice());
        holder.price.setText(Float.parseFloat(cartList.get(position).getQty())*Float.parseFloat(cartList.get(position).getPrice())+"");
        if (cartList.get(position).getVeg() != null) {
            if (cartList.get(position).getVeg() == 0) {
                holder.veg.setImageResource(R.mipmap.veg);
            } else {
                holder.veg.setImageResource(R.mipmap.nonveg);
            }
        }
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class CartAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView rate;
        public TextView price;
        public ImageView veg;



        public CartAdapterViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.cart_item_name);
            rate = (TextView) view.findViewById(R.id.cart_item_rate);
            price = (TextView) view.findViewById(R.id.cart_item_price);
            veg=(ImageView)view.findViewById(R.id.menu_item_veg);

        }
    }



}