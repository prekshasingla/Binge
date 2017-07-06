package com.example.prakharagarwal.fingerlickingawesome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by prekshasingla on 02/07/17.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private  List<Restaurant> mRestaurants;
    final private Context mContext;
    VideoAdapterViewHolder holder;
    LinearLayout container;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;

    public VideoAdapter(Context context, RecyclerView recyclerView, FragmentManager fragmentManager,List<Restaurant> restaurants) {
        mContext = context;
        mRestaurants=restaurants;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter_card_item, parent, false);
        container=(LinearLayout)view.findViewById(R.id.container);
        holder = new VideoAdapterViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final VideoAdapterViewHolder holder, final int position) {


        final String video = mRestaurants.get(position).video;
        final String name = mRestaurants.get(position).name;
        final String typeOfRestaurant=mRestaurants.get(position).typeOfRestaurant;

        holder.textViewName.setText(name);
        holder.textViewTypeOfRestaurant.setText(typeOfRestaurant);
        final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+video+"?rel=0?ecver=1&modestbranding=1&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";
        holder.webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setDomStorageEnabled(true);
        holder.webView.getSettings().setAllowFileAccess(true);
        holder.webView.setWebChromeClient(new WebChromeClient());


        holder.webView.setWebViewClient(new WebViewClient());

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            holder.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        }
        else {
            holder.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        }


//        holder.webView.setWebViewClient(new WebViewClient() {
//            // autoplay when finished loading via javascript injection
//            public void onPageFinished(WebView view, String url) {
//                holder.webView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()"); }
//        });



//        holder.webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageFinished(WebView webView, String url) {
//               super.onPageFinished(webView, url);
//                webView.loadUrl(url);
//                container.requestLayout();
//            }
//        });

        holder.webView.postDelayed(new Runnable() {

            @Override
            public void run() {
                holder.webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");

            }
        }, position+5);

        holder.buttonAmbience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ambienceURL="<iframe name=\"video\" width=\"100%\" height=\"100%\"" +
                        " src=\"https://www.youtube-nocookie.com/embed/"
                        +video+"?rel=0?ecver=1&start="+mRestaurants.get(position).ambienceStartTime+
                        "&end="+mRestaurants.get(position).ambienceEndTime+
                        "&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe >\n";
                holder.webView.loadDataWithBaseURL("", ambienceURL, "text/html", "UTF-8", "");
                holder.webView.requestFocus();

            }
        });

        holder.buttonSignatureDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signatureDishesURL="<iframe name=\"video\"  width=\"100%\" height=\"100%\"" +
                        " src=\"https://www.youtube-nocookie.com/embed/"
                        +video+"?rel=0?ecver=1&start="+mRestaurants.get(position).signatureStartTime+
                        "&end="+mRestaurants.get(position).signatureEndTime+
                        "&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";
                holder.webView.loadDataWithBaseURL("SomeStringForBaseURL", signatureDishesURL, "text/html", "UTF-8", "");
                holder.webView.requestFocus();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }


    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public WebView webView;
        public Button buttonAmbience;
        public Button buttonSignatureDishes;
        public TextView textViewName;
        public TextView textViewTypeOfRestaurant;



        public VideoAdapterViewHolder(View view) {
            super(view);

            webView = (WebView) view.findViewById(R.id.item_webview);
            buttonAmbience=(Button)view.findViewById(R.id.button_ambience);
            buttonSignatureDishes=(Button)view.findViewById(R.id.button_signature_dishes);
            textViewName=(TextView)view.findViewById(R.id.item_name);
            textViewTypeOfRestaurant=(TextView)view.findViewById(R.id.item_type_of_restaurant);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            Intent intent=new Intent(mContext,DetailActivity.class);
            intent.putExtra("name",mRestaurants.get(getAdapterPosition()).name);
            intent.putExtra("video",mRestaurants.get(getAdapterPosition()).video);
            intent.putExtra("address",mRestaurants.get(getAdapterPosition()).address);
            intent.putExtra("lattitude",mRestaurants.get(getAdapterPosition()).lattitude);
            intent.putExtra("longitude",mRestaurants.get(getAdapterPosition()).longitude);
            intent.putExtra("type_of_restaurant",mRestaurants.get(getAdapterPosition()).typeOfRestaurant);
            intent.putExtra("ambience_end_time",mRestaurants.get(getAdapterPosition()).ambienceEndTime);
            intent.putExtra("ambience_start_time",mRestaurants.get(getAdapterPosition()).ambienceStartTime);
            intent.putExtra("closing_time",mRestaurants.get(getAdapterPosition()).closingTime);
            intent.putExtra("opening_time",mRestaurants.get(getAdapterPosition()).openingTime);
            intent.putExtra("cuisine_type",mRestaurants.get(getAdapterPosition()).cuisineType);
            intent.putExtra("signature_end_time",mRestaurants.get(getAdapterPosition()).signatureEndTime);
            intent.putExtra("signature_start_time",mRestaurants.get(getAdapterPosition()).signatureStartTime);
            mContext.startActivity(intent);
        }
    }

    public void addAll(List<Restaurant> restaurants) {
        mRestaurants=restaurants;
    }

}
