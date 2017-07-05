package com.example.prakharagarwal.fingerlickingawesome;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

/**
 * Created by prekshasingla on 02/07/17.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {

    private List<String> mVideos;
    final private Context mContext;
    VideoAdapterViewHolder holder;
    LinearLayout container;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;
    int currId = -1;


    public VideoAdapter(Context context, RecyclerView recyclerView, List<String> videos, FragmentManager fragmentManager) {
        mContext = context;
        mVideos = videos;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter_card_item, parent, false);
        //view.setFocusable(true);
        container=(LinearLayout)view.findViewById(R.id.container);
        holder = new VideoAdapterViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(final VideoAdapterViewHolder holder, final int position) {




        LinearLayoutManager linearLayoutManager=(LinearLayoutManager)recyclerView.getLayoutManager();

        final String video = mVideos.get(position);
        //video="ZZS4dd5VeXY";
        final  String url="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+video+"?rel=0?ecver=1&modestbranding=1&autoplay=1\" frameborder=\"0\" allowfullscreen></iframe>\n";
        holder.webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setDomStorageEnabled(true);
        holder.webView.getSettings().setAllowFileAccess(true);
        holder.webView.setWebChromeClient(new WebChromeClient());

        holder.webView.callOnClick();

        holder.webView.setWebViewClient(new WebViewClient());

//        holder.webView.setWebViewClient(new WebViewClient() {
//            // autoplay when finished loading via javascript injection
//            public void onPageFinished(WebView view, String url) {
//                holder.webView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()"); }
//        });



//        int SDK_INT = android.os.Build.VERSION.SDK_INT;
//        if (SDK_INT > 16) {
//            holder.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        }
//
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
                //holder.webView.loadUrl(url);
                holder.webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");

            }
        }, position+5);
       // holder.webView.loadDataWithBaseURL("SomeStringForBaseURL", url, "text/html", "UTF-8", "");

        holder.buttonAmbience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ambienceURL="<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+video+"?rel=0?ecver=1&start=3&autoplay=1&end=10\" frameborder=\"0\" allowfullscreen></iframe >\n";
                holder.webView.loadDataWithBaseURL("", ambienceURL, "text/html", "UTF-8", "");

                holder.webView.requestFocus();
                //holder.webView.performClick();

            }
        });

        holder.buttonSignatureDishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String signatureDishesURL="<iframe name=\"video\"  width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/"+video+"?rel=0?ecver=1&start=3&autoplay=1&end=10\" frameborder=\"0\" allowfullscreen></iframe>\n";
                holder.webView.loadDataWithBaseURL("SomeStringForBaseURL", signatureDishesURL, "text/html", "UTF-8", "");
                holder.webView.requestFocus();

            }
        });



    }

    @Override
    public int getItemCount() {
        return mVideos.size();
    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public WebView webView;
        public Button buttonAmbience;
        public Button buttonSignatureDishes;



        public VideoAdapterViewHolder(View view) {
            super(view);

            webView = (WebView) view.findViewById(R.id.item_webview);
            buttonAmbience=(Button)view.findViewById(R.id.button_ambience);
            buttonSignatureDishes=(Button)view.findViewById(R.id.button_signature_dishes);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            mContext.startActivity(new Intent(mContext,DetailActivity.class));
        }
    }

    public void addAll(List<String> videos) {
        mVideos = videos;

    }

}
