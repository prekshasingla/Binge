package com.example.prakharagarwal.fingerlickingawesome.Extra;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.fingerlickingawesome.R;
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



