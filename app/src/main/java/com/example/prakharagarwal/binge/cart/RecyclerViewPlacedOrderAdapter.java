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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        View view = LayoutInflater.from(context).inflate(R.layout.placed_order_recyclerview_item, parent, false);
        return new RecyclerViewPlacedOrderAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textViewname.setText(menuList.get(position).getName());
        holder.textViewprice.setText(integersList.get(position) + " X  ₹" + menuList.get(position).getPrice() + " = ₹" + Integer.parseInt(menuList.get(position).getPrice()) * integersList.get(position));
        if (menuList.get(position).getVeg() == 0)
            holder.imageView.setBackgroundResource(R.mipmap.veg);
        else
            holder.imageView.setBackgroundResource(R.mipmap.nonveg);

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static void addingToBill() {
        float totalsum = 0;
        float total_price = 0;
        float total_priceGST5 = 0;
        float total_priceGST18 = 0;
        float gst_price = 0;
        float totalDiscount = 0f;
        float discountedPrice = 0f;
        HashMap<com.example.prakharagarwal.binge.Menu.Menu, Integer> cartItem = PassingCartItem.placed_order_hashmap;
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


            }
            total_price += entry.getValue() * Float.parseFloat(entry.getKey().getPrice());
        }
        gst_price = ((total_priceGST5 * 5) / 100) + ((total_priceGST18 * 18) / 100);
        totalsum = gst_price + total_price- totalDiscount;
        NewCartActivity.payAmount = totalsum;
        if(totalDiscount>0){
            NewCartActivity.discountText.setVisibility(View.VISIBLE);
            NewCartActivity.discountValue.setVisibility(View.VISIBLE);
            NewCartActivity.discountValue.setText("₹" + totalDiscount);
        }else{
            NewCartActivity.discountText.setVisibility(View.INVISIBLE);
            NewCartActivity.discountValue.setVisibility(View.INVISIBLE);
        }
        if (NewCartActivity.gstpricetext != null) {
            NewCartActivity.gstpricetext.setText("₹" + gst_price + "");
            NewCartActivity.totalpricetext.setText("₹" + total_price + "");
            NewCartActivity.paypricetext.setText("₹" + totalsum + "");
        }

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewname;
        public TextView textViewprice;
        public ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.dish_name_placed_order_cart);
            textViewprice = itemView.findViewById(R.id.dish_price_order_cart);
            imageView = itemView.findViewById(R.id.veg_nonveg_placed_order_cart);
        }
    }
}
