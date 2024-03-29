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
import java.util.Map;

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

        View myview= LayoutInflater.from(context).inflate(R.layout.recyclerview_cart_item,parent,false);
        return new MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.dish_name.setText(menuList.get(position).getName());
        holder.item_price.setText("₹ "+Integer.parseInt(menuList.get(position).getPrice())*integerList.get(position));
        holder.NumberButton.setNumber(integerList.get(position).toString());
        if(menuList.get(position).getVeg()==0)
            holder.veg_nonveg.setImageResource(R.mipmap.veg);
        else
            holder.veg_nonveg.setBackgroundResource(R.mipmap.nonveg);

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

                addingToBill();
            }
        });


        holder.remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                holder.item_price.setText("₹ "+Integer.parseInt(menuList.get(position).getPrice())*0);
//                holder.NumberButton.setNumber("0");
//                holder.NumberButton.setbuttonBackgroundColor(Color.GRAY,Color.GRAY,Color.GRAY);
//                menuList.get(position).setTotalcartItem(0);
                PassingCartItem.addmenu(menuList.get(position),0);
                menuList.get(position).setTotalcartItem(0);
                menuList.remove(position);
                integerList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, menuList.size());
                addingToBill();
            }
        });

    }

    public static void addingToBill() {
        float totalsum=0;
        float total_price=0;
        float total_priceGST5 = 0;
        float total_priceGST18 = 0;
        float gst_price = 0;
        float totalDiscount = 0f;
        float discountedPrice = 0f;
        HashMap<com.example.prakharagarwal.binge.Menu.Menu,Integer> cartItem= PassingCartItem.getMenuHashmap();
        for (Map.Entry<com.example.prakharagarwal.binge.Menu.Menu, Integer> entry : cartItem.entrySet()) {
            if (entry.getValue() != 0) {

                    if (entry.getKey().getDiscount() > 0) {
                        totalDiscount += (Float.parseFloat(entry.getKey().getPrice()) * (entry.getKey().getDiscount() / 100.0f))
                                * entry.getValue();
                        discountedPrice = Float.parseFloat(entry.getKey().getPrice()) -
                                (Float.parseFloat(entry.getKey().getPrice()) * (entry.getKey().getDiscount() / 100.0f));
                    }
                    if (discountedPrice > 0) {
                        if (entry.getKey().getGst() == 5)
                            total_priceGST5 += discountedPrice * entry.getValue();
                        if (entry.getKey().getGst() == 18)
                            total_priceGST18 += discountedPrice * entry.getValue();
                    } else {
                        if (entry.getKey().getGst() == 5)
                            total_priceGST5 += Float.parseFloat(entry.getKey().getPrice()) * entry.getValue();
                        if (entry.getKey().getGst() == 18)
                            total_priceGST18 += Float.parseFloat(entry.getKey().getPrice()) * entry.getValue();
                    }

                total_price+=entry.getValue()*Float.parseFloat(entry.getKey().getPrice());
            }
        }
        gst_price = ((total_priceGST5 * 5) / 100) + ((total_priceGST18 * 18) / 100);
        totalsum = gst_price + total_price- totalDiscount;
        NewCartActivity.payAmount=totalsum;
        if(totalDiscount>0){
            NewCartActivity.discountText.setVisibility(View.VISIBLE);
            NewCartActivity.discountValue.setVisibility(View.VISIBLE);
            NewCartActivity.discountValue.setText("₹" + totalDiscount);
        }else{
            NewCartActivity.discountText.setVisibility(View.INVISIBLE);
            NewCartActivity.discountValue.setVisibility(View.INVISIBLE);
        }
        if(NewCartActivity.gstpricetext!=null)
        {
            NewCartActivity.gstpricetext.setText("₹"+gst_price+"");
            NewCartActivity.totalpricetext.setText("₹"+total_price+"");
            NewCartActivity.paypricetext.setText("₹"+totalsum+"");
        }
        if(totalsum==0) {
            NewCartActivity.payNowbtn.setEnabled(false);
            NewCartActivity.payNowbtn.setBackgroundColor(Color.GRAY);

            NewCartActivity.placed_order_btn.setEnabled(false);
            NewCartActivity.placed_order_btn.setBackgroundColor(Color.GRAY);
        }else{
            NewCartActivity.payNowbtn.setEnabled(true);
            NewCartActivity.payNowbtn.setBackgroundColor(Color.parseColor("#32CD32"));
            NewCartActivity.placed_order_btn.setEnabled(true);
            NewCartActivity.placed_order_btn.setBackgroundColor(Color.parseColor("#ff5500"));
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CartNumberButton NumberButton;
        TextView item_price,dish_name;
        ImageView remove_item;
        ImageView veg_nonveg;

        public MyViewHolder(View itemView) {
            super(itemView);

            NumberButton=itemView.findViewById(R.id.elegantNumberButton_cart_recyclerview);
            item_price=itemView.findViewById(R.id.price_cart_item);
            dish_name=itemView.findViewById(R.id.dish_name_cart_recyclerview);
            remove_item=itemView.findViewById(R.id.close_cart_item);
            veg_nonveg=itemView.findViewById(R.id.veg_nonveg_cart);

        }
    }
}
