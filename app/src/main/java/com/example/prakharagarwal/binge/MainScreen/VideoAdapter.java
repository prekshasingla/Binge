package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.Review.ReviewActivity;
import com.example.prakharagarwal.binge.StoriesMenu.StoriesActivity;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by prekshasingla on 02/07/17.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {
    private List<Restaurant> mRestaurants;
    final private Context mContext;
    VideoAdapterViewHolder holder;
    LinearLayout container;
    FragmentManager fragmentManager;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    public static final String inputFormat = "HH:mm";

    private Date date;
    private Date dateCompareOne;
    private Date dateCompareTwo;

    private String compareStringOne = "9:45";
    private String compareStringTwo = "1:45";

    SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);


    private void bindActivity() {
    }


    public VideoAdapter(Context context, RecyclerView recyclerView, FragmentManager fragmentManager, List<Restaurant> restaurants, LinearLayoutManager linearLayoutManager) {
        mContext = context;
        mRestaurants = restaurants;
        this.recyclerView = recyclerView;
        this.fragmentManager = fragmentManager;
        this.linearLayoutManager = linearLayoutManager;
    }


    @Override
    public VideoAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter_card_item, parent, false);
        container = (LinearLayout) view.findViewById(R.id.container);
        holder = new VideoAdapterViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(final VideoAdapterViewHolder holder, final int position) {


        final String video = mRestaurants.get(position).video;
        final String name = mRestaurants.get(position).name;
        final String id = mRestaurants.get(position).id;
        final String typeOfRestaurant = mRestaurants.get(position).typeOfRestaurant;

        compareStringOne = mRestaurants.get(position).openingTime;
        compareStringTwo = mRestaurants.get(position).closingTime;


        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/segoeui.ttf");
//        resName.setTypeface(typeface);

        if (mRestaurants.get(position).costForTwo != -1)
            holder.textViewCostForTwo.setText("Cost for two: " + mRestaurants.get(position).getCostForTwo());
        holder.textViewName.setText(name);
        holder.textViewName.setTypeface(typeface);
//        holder.textViewTypeOfRestaurant.setText(typeOfRestaurant);
        holder.textViewTypeOfCuisine.setText(mRestaurants.get(position).cuisineType);
        holder.address.setText(mRestaurants.get(position).address);
        final String url = "  <body style=\"margin:0; padding:0\">\n" +
                "<iframe name=\"video\" width=\"100%\" height=\"100%\" src=\"https://www.youtube-nocookie.com/embed/" + video + "?rel=0?ecver=1&modestbranding=1&showinfo=0&autohide=1&controls=0&autoplay=1&playlist=" + video + "&loop=1\" frameborder=\"0\" allowfullscreen></iframe>" +
                "  </body>";
        holder.webView.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
        holder.webView.getSettings().setJavaScriptEnabled(true);
        holder.webView.getSettings().setDomStorageEnabled(true);
        holder.webView.getSettings().setAllowFileAccess(true);
        holder.webView.setWebChromeClient(new WebChromeClient());

        holder.webView.setWebViewClient(new WebViewClient());

        holder.webView.setVerticalScrollBarEnabled(false);
        holder.webView.setHorizontalScrollBarEnabled(false);
        holder.webView.setBackgroundColor(808080);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 16) {
            holder.webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        } else {
            holder.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        }

        holder.webView.loadDataWithBaseURL("", url, "text/html", "UTF-8", "");
        holder.webView.setVisibility(View.VISIBLE);
//        holder.webView.webView

        final int currentVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


            }
        });

        if(compareDates()){
            holder.textViewTimings.setText("Open"+Html.fromHtml(compareStringOne + "-" + compareStringTwo + " hrs"));

        }else{
            holder.textViewTimings.setText("Closed"+Html.fromHtml(compareStringOne + "-" + compareStringTwo + " hrs"));

        }


//        if(currentVisible==position){
//            holder.webView.loadDataWithBaseURL("", url1, "text/html", "UTF-8", "");
//
//        }
//
//
//        if(position==0)
//        {
//
//            holder.webView.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    holder.webView.performClick();
//
//                }
//            }, position+5);
//
//
//        }


        holder.textViewShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Indulge in the experience of delicacies that allure you in at Restaurant- "+mRestaurants.get(position).getName()+" on Binge. https://play.google.com/store/apps/details?id=in.binge.android ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Binge");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                mContext.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        holder.textViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = mRestaurants.get(position).getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" +number));
                mContext.startActivity(intent);
            }
        });


        holder.buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ReviewActivity.class);
                intent.putExtra("restaurantID", id);
                intent.putExtra("restaurantName", mRestaurants.get(position).name);
                mContext.startActivity(intent);


            }
        });
        holder.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, StoriesActivity.class);
                intent.putExtra("restaurantID", id);
                intent.putExtra("restaurantName", mRestaurants.get(position).name);
                mContext.startActivity(intent);


            }
        });
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?daddr=" + mRestaurants.get(position).getLattitude() + "," + mRestaurants.get(position).getLongitude();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                mContext.startActivity(intent);
            }
        });


    }

    private boolean compareDates() {

        try {
            Date time1 = new SimpleDateFormat("HH:mm").parse(compareStringOne);
            Log.i("TAG",compareStringOne);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(time1);
            calendar1.add(Calendar.DATE, 1);

            Date time2 = new SimpleDateFormat("HH:mm").parse(compareStringTwo);
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(time2);
            Log.i("TAG",compareStringTwo);
            calendar2.add(Calendar.DATE, 1);

            Calendar now = Calendar.getInstance();
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            String a =hour+":"+minute;
            Date d = new SimpleDateFormat("HH:mm").parse(a);
            now.setTime(d);
            now.add(Calendar.DATE, 1);
            Date x = now.getTime();
            Log.i("TAG",x.toString());

            if(calendar1.get(Calendar.HOUR_OF_DAY)>calendar2.get(Calendar.HOUR_OF_DAY)){

                if(now.get(Calendar.HOUR_OF_DAY)<calendar1.get(Calendar.HOUR_OF_DAY)){
                    int hr1 =now.get(Calendar.HOUR_OF_DAY)+24;
                    int min1 = now.get(Calendar.MINUTE);
                    String c = hr1+":"+min1;
                    Date d2 = new SimpleDateFormat("HH:mm").parse(c);
                    now.setTime(d2);
                    now.add(Calendar.DATE, 1);
                }
                int hr =calendar2.get(Calendar.HOUR_OF_DAY)+24;
                int min = calendar2.get(Calendar.MINUTE);
                String b = hr+":"+min;
                Date d1 = new SimpleDateFormat("HH:mm").parse(b);
                calendar2.setTime(d1);
                calendar2.add(Calendar.DATE, 1);
            }
            if (x.after(calendar1.getTime()) && x.before(calendar2.getTime())) {
                return true;
            }
            return false;

        } catch (ParseException e){
            e.printStackTrace();

        }
        return true;}

//        try {
//            Calendar now = Calendar.getInstance();
//           // now.add(Calendar.DATE,1);
//
//            int hour = now.get(Calendar.HOUR_OF_DAY);
//            int minute = now.get(Calendar.MINUTE);
//
//
//
//           // date=now.getTime();
//            date=new SimpleDateFormat("HH:mm").parse(hour+":"+minute);
//            Calendar calendar0=Calendar.getInstance();
//
//            dateCompareOne = new SimpleDateFormat("HH:mm").parse(compareStringOne);
//            Calendar calendar1 = Calendar.getInstance();
//            calendar1.setTime(dateCompareOne);
//            //calendar1.add(Calendar.DATE, 1);
//
//            dateCompareTwo = new SimpleDateFormat("HH:mm").parse(compareStringTwo);
//            Calendar calendar2 = Calendar.getInstance();
//            calendar2.setTime(dateCompareTwo);
//            int hourOpen=calendar1.HOUR_OF_DAY;
//            int minOpen=calendar1.MINUTE;
//
//            int hourClose=calendar2.HOUR_OF_DAY;
//            int minClose=calendar2.MINUTE;
//
//            int hourCurr=calendar0.HOUR_OF_DAY;
//            int minCurr=calendar0.MINUTE;
//
//            if(hourOpen>hourClose){
//                hourClose+=24;
//            }
//            if(hourCurr<hourOpen){
//                return false;
//            }
//            if(hourCurr<hourClose && hourOpen<hourClose &&
//                    hourCurr>hourOpen){
//                return true;
//            }
//            if(hourCurr>hourClose && hourOpen<hourClose
//                    && hourCurr>hourOpen){
//                return false;
//            }
//
//            if(calendar0.HOUR_OF_DAY>calendar2.HOUR_OF_DAY && calendar1.HOUR_OF_DAY>calendar2.HOUR_OF_DAY){
//               //calendar0.M
//                return true;
//            }
//            if(calendar0.HOUR_OF_DAY<calendar2.HOUR_OF_DAY && calendar1.HOUR_OF_DAY<calendar2.HOUR_OF_DAY){
//                return true;
//            }
//
//            return false;
//
//    }
//        catch (java.text.ParseException e){}
//        return true;
//    }
//

    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }


    public class VideoAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public WebView webView;
        public TextView textViewName;
//        public TextView textViewTypeOfRestaurant;
        public TextView textViewTypeOfCuisine;
        public TextView textViewTimings;
        public Button buttonReview;
        public Button buttonMenu;
        public TextView textViewCostForTwo;
        public TextView location;
        public TextView textViewCall;
        public TextView textViewShare;
        TextView address;


        public VideoAdapterViewHolder(View view) {
            super(view);

            webView = (WebView) view.findViewById(R.id.item_webview);
            textViewName = (TextView) view.findViewById(R.id.item_name);
//            textViewTypeOfRestaurant = (TextView) view.findViewById(R.id.item_type_of_restaurant);
            textViewTypeOfCuisine = (TextView) view.findViewById(R.id.item_type_of_cuisine);
            buttonReview = (Button) view.findViewById(R.id.button_review);
            buttonMenu = (Button) view.findViewById(R.id.button_menu);
            location = (TextView) view.findViewById(R.id.feeds_adapter_item_location);
            textViewCostForTwo = (TextView) view.findViewById(R.id.item_cost_for_two);
            address = (TextView) view.findViewById(R.id.feeds_item_address);
            textViewTimings=(TextView) view.findViewById(R.id.feeds_item_timings);
            textViewCall=(TextView)view.findViewById(R.id.call_button_textview);
            textViewShare=(TextView)view.findViewById(R.id.share_button_textview);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
//            Uri uri=Uri.parse("https://www.youtube.com/watch?v=22RWiVE7Wwc");
//            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
//            mContext.startActivity(intent);

//            Intent intent=new Intent(mContext,StoriesActivity.class);
//            mContext.startActivity(intent);

//            Intent intent=new Intent(mContext,DetailActivity.class);
//            intent.putExtra("name",mRestaurants.get(getAdapterPosition()).name);
//            intent.putExtra("video",mRestaurants.get(getAdapterPosition()).video);
//            intent.putExtra("address",mRestaurants.get(getAdapterPosition()).address);
//            intent.putExtra("lattitude",mRestaurants.get(getAdapterPosition()).lattitude);
//            intent.putExtra("longitude",mRestaurants.get(getAdapterPosition()).longitude);
//            intent.putExtra("type_of_restaurant",mRestaurants.get(getAdapterPosition()).typeOfRestaurant);
//            intent.putExtra("ambience_end_time",mRestaurants.get(getAdapterPosition()).ambienceEndTime);
//            intent.putExtra("ambience_start_time",mRestaurants.get(getAdapterPosition()).ambienceStartTime);
//            intent.putExtra("closing_time",mRestaurants.get(getAdapterPosition()).closingTime);
//            intent.putExtra("opening_time",mRestaurants.get(getAdapterPosition()).openingTime);
//            intent.putExtra("cuisine_type",mRestaurants.get(getAdapterPosition()).cuisineType);
//            intent.putExtra("signature_end_time",mRestaurants.get(getAdapterPosition()).signatureEndTime);
//            intent.putExtra("signature_start_time",mRestaurants.get(getAdapterPosition()).signatureStartTime);
//            mContext.startActivity(intent);
        }
    }

    public void addAll(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
    }

    public void removeAll() {
        mRestaurants.clear();
    }
}
