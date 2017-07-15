package com.example.prakharagarwal.fingerlickingawesome;

import android.app.Fragment;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    Restaurant restaurant;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        restaurant=new Restaurant();

        Intent i = getActivity().getIntent();
        restaurant.setName(i.getStringExtra("name"));
        restaurant.setVideo(i.getStringExtra("video"));
        restaurant.setAddress(i.getStringExtra("address"));
        restaurant.setLattitude(i.getStringExtra("lattitude"));
        restaurant.setLongitude(i.getStringExtra("longitude"));
        restaurant.setTypeOfRestaurant(i.getStringExtra("type_of_restaurant"));
        restaurant.setAmbienceEndTime(i.getIntExtra("ambience_end_time",0));
        restaurant.setAmbienceStartTime(i.getIntExtra("ambience_start_time",0));
        restaurant.setCuisineType(i.getStringExtra("cuiseine_type"));
        restaurant.setClosingTime(i.getStringExtra("closing_time"));
        restaurant.setOpeningTime(i.getStringExtra("opening_time"));
        restaurant.setSignatureEndTime(i.getIntExtra("signature_end_time",0));
        restaurant.setSignatureStartTime(i.getIntExtra("signature_start_time",0));


        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)rootView.findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setTitle(restaurant.name);
        SquareImageView squareImageView=(SquareImageView)rootView.findViewById(R.id.detail_image) ;

        String img_url="http://img.youtube.com/vi/"+restaurant.video+"/0.jpg";


        Picasso.with(getActivity())
                .load(img_url)
                .into(squareImageView);

        squareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" +restaurant.video));
//                startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + restaurant.video));
                startActivity(intent);
            }
        });


        TextView textViewAddress=(TextView)rootView.findViewById(R.id.detail_address);
        textViewAddress.setText(restaurant.address);

        TextView textViewViewOnMaps=(TextView)rootView.findViewById(R.id.detail_view_on_maps);
        textViewViewOnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                String uri = "http://maps.google.com/maps?daddr=" + restaurant.lattitude + "," + restaurant.longitude;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        TextView textViewMenuSeeMore=(TextView)rootView.findViewById(R.id.detail_menu_see_more);
        textViewMenuSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),MenuActivity.class);
                getActivity().startActivity(intent);


            }
        });


        LinearLayout layoutMenu = (LinearLayout) rootView.findViewById(R.id.details_menu_images);
        for (int j = 0; j < 10; j++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setId(j+100);
            imageView.setPadding(4,4,4,4);

            String img_url1="http://img.youtube.com/vi/"+restaurant.video+"/0.jpg";


//            if(j==1)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==2)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==3)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==4)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";
//            if(j==5)
//                img_url1="http://img.youtube.com/vi/LyJ9-m2M_NM/0.jpg";
//            if(j==6)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==7)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==8)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==9)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";

            Picasso.with(getActivity())
                    .load(img_url1)
                    .into(imageView);
            layoutMenu.addView(imageView);
        }

//        LinearLayout layoutMainCourse = (LinearLayout) rootView.findViewById(R.id.details_main_course_images);
//        for (int j = 0; j < 10; j++) {
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setId(j+100);
//            imageView.setPadding(4,4,4,4);
//
//            String img_url1="http://img.youtube.com/vi/"+restaurant.video+"/0.jpg";
//
//
//            if(j==1)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==2)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==3)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==4)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";
//            if(j==5)
//                img_url1="http://img.youtube.com/vi/LyJ9-m2M_NM/0.jpg";
//            if(j==6)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==7)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==8)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==9)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";
//
//            Picasso.with(getActivity())
//                    .load(img_url1)
//                    .into(imageView);
//            layoutMainCourse.addView(imageView);
//        }
//
//        LinearLayout layoutStarters = (LinearLayout) rootView.findViewById(R.id.details_starters_images);
//        for (int j = 0; j < 10; j++) {
//            ImageView imageView = new ImageView(getActivity());
//            imageView.setId(j+100);
//            imageView.setPadding(4,4,4,4);
//
//            String img_url1="http://img.youtube.com/vi/"+restaurant.video+"/0.jpg";
//
//
//            if(j==1)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==2)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==3)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==4)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";
//            if(j==5)
//                img_url1="http://img.youtube.com/vi/LyJ9-m2M_NM/0.jpg";
//            if(j==6)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==7)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==8)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==9)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";
//
//            Picasso.with(getActivity())
//                    .load(img_url1)
//                    .into(imageView);
//            layoutStarters.addView(imageView);
//        }
//
//

        LinearLayout layoutReview = (LinearLayout) rootView.findViewById(R.id.details_review_images);
        for (int j = 0; j < 10; j++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setId(j+100);
            imageView.setPadding(4,4,4,4);

            String img_url1="http://img.youtube.com/vi/"+restaurant.video+"/0.jpg";


//            if(j==1)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==2)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==3)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==4)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";
//            if(j==5)
//                img_url1="http://img.youtube.com/vi/LyJ9-m2M_NM/0.jpg";
//            if(j==6)
//                img_url1="http://img.youtube.com/vi/Smjo2-2-opw/0.jpg";
//            if(j==7)
//                img_url1="http://img.youtube.com/vi/Y0aP0Rd6j1M/0.jpg";
//            if(j==8)
//                img_url1="http://img.youtube.com/vi/twl072sffjE/0.jpg";
//            if(j==9)
//                img_url1="http://img.youtube.com/vi/UwVqBY3c_80/0.jpg";

            Picasso.with(getActivity())
                    .load(img_url1)
                    .into(imageView);
            layoutReview.addView(imageView);
        }



        return rootView;

    }
}
