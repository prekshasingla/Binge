package com.example.prakharagarwal.binge.StoriesMenu;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.R;

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

        if (menus.get(position).getHas_video() == 0) {
            holder.play.setVisibility(View.VISIBLE);
        }else{
            holder.play.setVisibility(View.INVISIBLE);
        }

        holder.textViewName.setText(name);
        holder.textViewPrice.setText(price);
        holder.textViewDescription.setText(description);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menus.get(position).getHas_video() == 0) {
                    ((Callback )mContext).showStory(mContext,menus.get(position));
                }else{
                    Toast.makeText(mContext, "Video requested. Will be available soon", Toast.LENGTH_SHORT).show();
                }
            }
        });


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
        public ImageView play;


        public MenuAdapterViewHolder(View view) {
            super(view);

            webView = (WebView) view.findViewById(R.id.item_webview);
            textViewName = (TextView) view.findViewById(R.id.menu_item_name);
            textViewPrice = (TextView) view.findViewById(R.id.menu_item_price);
            textViewDescription = (TextView) view.findViewById(R.id.menu_item_description);
            imageViewVeg = (ImageView) view.findViewById(R.id.menu_item_veg);
            linearLayout = (LinearLayout) view.findViewById(R.id.menu_adapter_lin_lay);
        play=(ImageView)view.findViewById(R.id.menu_adapter_play);
        }
    }

    public void addAll(List<Menu> menus) {
        MenuAdapter.this.menus = menus;
    }

}
