package com.example.prakharagarwal.binge.cart;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;

import java.util.List;

public class RecyclerViewPlacedOrderAdapter extends RecyclerView.Adapter<RecyclerViewPlacedOrderAdapter.MyViewHolder> {

    private Context context;
    private List<com.example.prakharagarwal.binge.Menu.Menu> menuList;
    private List<Integer> integersList;

    public RecyclerViewPlacedOrderAdapter(Context context, List<com.example.prakharagarwal.binge.Menu.Menu> menuList, List<Integer> integersList) {
        this.context = context;
        this.menuList = menuList;
        this.integersList = integersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.placed_order_recyclerview_item,parent,false);
        return new RecyclerViewPlacedOrderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewname.setText(menuList.get(position).getName());
       holder.textViewprice.setText(integersList.get(position)+" X  ₹"+menuList.get(position).getPrice()+" = ₹"+Integer.parseInt(menuList.get(position).getPrice())*integersList.get(position));
       if(menuList.get(position).getVeg()==0)
          holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.veg_active));
       else
           holder.imageView.setBackgroundColor(context.getResources().getColor(R.color.nonveg_active));

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {

        public TextView textViewname;
        public TextView textViewprice;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewname=itemView.findViewById(R.id.dish_name_placed_order_cart);
            textViewprice=itemView.findViewById(R.id.dish_price_order_cart);
            imageView=itemView.findViewById(R.id.veg_nonveg_placed_order_cart);
        }
    }
}
