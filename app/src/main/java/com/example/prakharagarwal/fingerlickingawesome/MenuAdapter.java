package com.example.prakharagarwal.fingerlickingawesome;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/**
 * Created by prekshasingla on 26/07/17.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder> {
        private List<Menu> menus;
        final private Context mContext;
        MenuAdapterViewHolder holder;

    private void bindActivity() {
    }


    public MenuAdapter(Context context, List<Menu> menus) {
        mContext = context;
        this.menus=menus;
         }


    @Override
    public MenuAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_adapter_item, parent, false);
        holder = new MenuAdapterViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final MenuAdapterViewHolder holder, final int position) {


        final String description = menus.get(position).description;
        final String name = menus.get(position).name;
        final String price= menus.get(position).price;


        holder.textViewName.setText(name);
        holder.textViewPrice.setText(price);
        holder.textViewDescription.setText(description);

        if(menus.get(position).veg){
            holder.imageViewVeg.setImageResource(R.mipmap.veg);

        }
        else {
            holder.imageViewVeg.setImageResource(R.mipmap.nonveg);
        }



    }



    @Override
    public int getItemCount() {
        return menus.size();
    }


    public class MenuAdapterViewHolder extends RecyclerView.ViewHolder {

        public WebView webView;
        public TextView textViewName;
        public TextView textViewPrice;
        public TextView textViewDescription;
        public ImageView imageViewVeg;


        public MenuAdapterViewHolder(View view) {
            super(view);

            webView = (WebView) view.findViewById(R.id.item_webview);
            textViewName = (TextView) view.findViewById(R.id.menu_item_name);
            textViewPrice = (TextView) view.findViewById(R.id.menu_item_price);
            textViewDescription = (TextView) view.findViewById(R.id.menu_item_description);
            imageViewVeg = (ImageView) view.findViewById(R.id.menu_item_veg);

        }
    }

    public void addAll(List<Menu> menus) {
        MenuAdapter.this.menus=menus;
    }



}
