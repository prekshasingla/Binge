package com.example.prakharagarwal.binge.StoriesMenu;

import android.content.Context;
import android.graphics.Typeface;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.MainScreen.RestaurantDetailsActivity;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.cart.CartItem;

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

    public interface Callback {
        public void showStory(Context context, Menu menu);

    }

    public MenuAdapter(Context context, List<Menu> menus) {
        mContext = context;
        this.menus = menus;
    }


    @Override
    public MenuAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_adapter_item, parent, false);
        holder = new MenuAdapterViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final MenuAdapterViewHolder holder, final int position) {


        final String description = menus.get(position).desc;
        final String name = menus.get(position).name;
        final String price = menus.get(position).price;

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nunito-SemiBold.ttf");
        Typeface typeface1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nunito-Light.ttf");
        Typeface typeface2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/Nunito-Regular.ttf");


        if (menus.get(position).getHas_video() == 0) {
//            holder.play.setVisibility(View.VISIBLE);
        } else {
//            holder.play.setVisibility(View.INVISIBLE);
        }

        if (menus.get(position).getVeg() != null) {
            if (menus.get(position).getVeg() == 0) {
                holder.imageViewVeg.setImageResource(R.mipmap.veg);
            } else {
                holder.imageViewVeg.setImageResource(R.mipmap.nonveg);
            }
        }
        holder.textViewName.setText(name);
        holder.textViewName.setTypeface(typeface);
        holder.textViewPrice.setText(price);
        holder.textViewPrice.setTypeface(typeface);
        holder.textViewDescription.setText(description);
        holder.textViewDescription.setTypeface(typeface);
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (menus.get(position).getHas_video() == 0) {
//                    ((Callback )mContext).showStory(mContext,menus.get(position));
//                }else{
//                    Toast.makeText(mContext, "Video requested. Will be available soon", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        if (((RestaurantDetailsActivity) mContext).isCart()) {
            holder.cartLayout.setVisibility(View.VISIBLE);
            holder.qty_text.setText(menus.get(position).getCart_quantity() + "");
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    menus.get(position).setCart_quantity(menus.get(position).getCart_quantity() + 1);
                    holder.qty_text.setText(menus.get(position).getCart_quantity() + "");
                    CartItem item = new CartItem();
                    item.setQty(menus.get(position).getCart_quantity() + "");
                    item.setName(name);
                    item.setPrice(price);
                    item.setVeg(menus.get(position).getVeg());
                    ((RestaurantDetailsActivity) mContext).cartListMap.put(name, item);
                    ((RestaurantDetailsActivity) mContext).updateCartQty();

                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menus.get(position).getCart_quantity() == 0) {
                    } else {
                        menus.get(position).setCart_quantity(menus.get(position).getCart_quantity() - 1);
                        holder.qty_text.setText(menus.get(position).getCart_quantity() + "");
                        if (menus.get(position).getCart_quantity() == 0)
                            ((RestaurantDetailsActivity) mContext).cartListMap.remove(name);
                        else {
                            CartItem item = new CartItem();
                            item.setQty(menus.get(position).getCart_quantity() + "");
                            item.setName(name);
                            item.setPrice(price);
                            item.setVeg(menus.get(position).getVeg());
                            ((RestaurantDetailsActivity) mContext).cartListMap.put(name, item);
                        }
                        ((RestaurantDetailsActivity) mContext).updateCartQty();
                    }
                }
            });
        } else {
            holder.cartLayout.setVisibility(View.GONE);
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
        public LinearLayout linearLayout;
        public LinearLayout cartLayout;
        public TextView qty_text;
        public Button plus;
        public Button minus;
//        public LinearLayout play;


        public MenuAdapterViewHolder(View view) {
            super(view);

            webView = (WebView) view.findViewById(R.id.item_webview);
            textViewName = (TextView) view.findViewById(R.id.menu_item_name);
            textViewPrice = (TextView) view.findViewById(R.id.menu_item_price);
            textViewDescription = (TextView) view.findViewById(R.id.menu_item_description);
            imageViewVeg = (ImageView) view.findViewById(R.id.menu_item_veg);
            linearLayout = (LinearLayout) view.findViewById(R.id.menu_adapter_lin_lay);
            cartLayout = (LinearLayout) view.findViewById(R.id.menu_adapter_cart);
            qty_text = (TextView) view.findViewById(R.id.menu_adapter_add_number);
            plus = (Button) view.findViewById(R.id.menu_adapter_plus);
            minus = (Button) view.findViewById(R.id.menu_adapter_minus);
//        play=(LinearLayout)view.findViewById(R.id.menu_adapter_play);
        }
    }

    public void addAll(List<Menu> menus) {
        MenuAdapter.this.menus = menus;
    }

}
