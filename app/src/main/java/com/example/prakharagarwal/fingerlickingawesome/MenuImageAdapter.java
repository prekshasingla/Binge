package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by prekshasingla on 11/07/17.
 */
public class MenuImageAdapter extends ArrayAdapter<Menu>
{
    public ArrayList<Menu> menuList;

    public MenuImageAdapter(Activity context, ArrayList<Menu> menuList) {
        super(context, 0, menuList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Menu menu = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_view, parent, false);
        }


        ImageView posterView = (ImageView) convertView.findViewById(R.id.image_view);
        //String img_url="http://img.youtube.com/vi/"+menu.image+"/0.jpg";

        Picasso.with(getContext()).load(menu.image).into(posterView);

        TextView titleView = (TextView) convertView.findViewById(R.id.article_title);
        //TextView subtitleView = (TextView) convertView.findViewById(R.id.article_subtitle);
        titleView.setText("Title");
        //subtitleView.setText("subtitle");


        return convertView;
    }


}


//        extends RecyclerView.Adapter<MenuImageAdapter.MenuImageAdapterViewHolder> {
//
//    public ArrayList<Menu> menuList;
//    final private Context mContext;
//    MenuImageAdapterViewHolder holder;
//
//
//    public MenuImageAdapter(Context context, ArrayList<Menu> menuList) {
//        mContext = context;
//        this.menuList=menuList;
//    }
//
//
//    @Override
//    public MenuImageAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view, parent, false);
//        holder = new MenuImageAdapterViewHolder(view);
//        return holder;
//
//    }
//
//    @Override
//    public void onBindViewHolder(final MenuImageAdapterViewHolder holder, final int position) {
//
//        Menu menu = menuList.get(position);
//
//        holder.titleView.setText("Title");
//        holder.subtitleView.setText("SubTitle");
//
//        String img_url="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//
//        Picasso.with(mContext).load(img_url).into(holder.thumbnailView);
////        holder.thumbnailView.setImageUrl(
////                img_url,
////                ImageLoaderHelper.getInstance(mContext).getImageLoader());
////        holder.thumbnailView.setAspectRatio(6);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return menuList.size();
//    }
//
//
//    public class MenuImageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        public ImageView thumbnailView;
//        public TextView titleView;
//        public TextView subtitleView;
//
//
//        public MenuImageAdapterViewHolder(View view) {
//            super(view);
//            thumbnailView = (ImageView) view.findViewById(R.id.image_view);
//            titleView = (TextView) view.findViewById(R.id.article_title);
//            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
//            view.setOnClickListener(this);
//
//        }
//
//        @Override
//        public void onClick(View view) {
//
//        }
//    }
//
//
//}
//



