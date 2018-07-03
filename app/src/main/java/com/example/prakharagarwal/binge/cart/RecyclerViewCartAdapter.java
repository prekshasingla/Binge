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
import com.example.prakharagarwal.binge.model_class.PassingCartItem;
import com.example.prakharagarwal.binge.rishabhcutomview.CartNumberButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecyclerViewCartAdapter extends RecyclerView.Adapter<RecyclerViewCartAdapter.MyViewHolder> {

  static   List<com.example.prakharagarwal.binge.Menu.Menu> menuList;
    List<Integer> integerList;
    Context context;

    RecyclerViewCartAdapter(List<com.example.prakharagarwal.binge.Menu.Menu> menus, List<Integer> integers, Context context)
    {
       this.menuList=menus;
       this.integerList=integers;
       this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recyclerview_cart_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.dish_name.setText(menuList.get(position).getName());
        holder.item_price.setText("₹ "+Integer.parseInt(menuList.get(position).getPrice())*integerList.get(position));
        holder.NumberButton.setNumber(integerList.get(position).toString());
        holder.NumberButton.setOnValueChangeListener(new CartNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(CartNumberButton view, int oldValue, int newValue) {
              //  menuList.get(position).setTotalcartItem(Byte.);
                if(newValue==0)
                    holder.NumberButton.setbuttonBackgroundColor(Color.GRAY,Color.GRAY,Color.GRAY);
                else
                    holder.NumberButton.setbuttonBackgroundColor(context.getResources().getColor(R.color.lime_green),context.getResources().getColor(R.color.lime_green),context.getResources().getColor(R.color.lime_green));

                holder.item_price.setText("₹ "+Integer.parseInt(menuList.get(position).getPrice())*newValue);
                menuList.get(position).setTotalcartItem(newValue);
                PassingCartItem.addmenu(menuList.get(position),newValue);
                menuList.get(position).setTotalcartItem(newValue);
            }
        });


        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.item_price.setText("₹ "+Integer.parseInt(menuList.get(position).getPrice())*0);
                holder.NumberButton.setNumber("0");
                holder.NumberButton.setbuttonBackgroundColor(Color.GRAY,Color.GRAY,Color.GRAY);
                menuList.get(position).setTotalcartItem(0);
                PassingCartItem.addmenu(menuList.get(position),0);
                menuList.get(position).setTotalcartItem(0);
            }
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CartNumberButton NumberButton;
        TextView item_price,dish_name;
        ImageView remove_item;

        public MyViewHolder(View itemView) {
            super(itemView);

            NumberButton=itemView.findViewById(R.id.elegantNumberButton_cart_recyclerview);
            item_price=itemView.findViewById(R.id.price_cart_item);
            dish_name=itemView.findViewById(R.id.dish_name_cart_recyclerview);
            remove_item=itemView.findViewById(R.id.close_cart_item);


        }
    }
}
