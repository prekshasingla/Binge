package com.example.prakharagarwal.binge.MainScreen;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.CheckNetwork;
import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DineoutFragment extends Fragment {
    RecyclerView nRecyclerView;
    RecommendAdapter mRecommendAdapter;
    List<Recommend> recommends;
    RecyclerView mRecyclerView;
    VideoAdapter mFeedsAdapter;
    List<Restaurant> restaurants;
    private SwipeRefreshLayout swipeContainer;

    TextView textViewEmpty;
    private ProgressBar progress;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurants=new ArrayList<Restaurant>();
        recommends = new ArrayList<Recommend>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dineout, container, false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        textViewEmpty=(TextView)rootView.findViewById(R.id.main_activity_empty);
        textViewEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
        progress=(ProgressBar)rootView.findViewById(R.id.main_activity_progress);
        progress.setVisibility(View.VISIBLE);
        progress.setIndeterminate(true);
        Log.i("TAG", "Before NRecycler");
        nRecyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_recommended);
        Log.i("TAG", "After NRecycler");
        mRecommendAdapter = new RecommendAdapter(recommends,getContext(),nRecyclerView,getChildFragmentManager(),layoutManager);
        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.dineout_fragment_recycler_view);
        mFeedsAdapter= new VideoAdapter(getContext(),mRecyclerView,getChildFragmentManager(),restaurants,linearLayoutManager);
        nRecyclerView.setHasFixedSize(true);
       nRecyclerView.setAdapter(mRecommendAdapter);
       mRecyclerView.setAdapter(mFeedsAdapter);
       nRecyclerView.setLayoutManager(layoutManager);
       mRecyclerView.setLayoutManager(linearLayoutManager);

        //update();

        swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
                swipeContainer.setRefreshing(false);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return rootView;
    }


    public void getData(DataSnapshot dataSnapshot) {
        mFeedsAdapter.removeAll();

        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals("table")) {
                for (DataSnapshot child1 : child.getChildren()) {
                    Restaurant restaurant=new Restaurant();

                    for (DataSnapshot child2 : child1.getChildren()) {

                        //Restaurant restaurant=child2.getValue(Restaurant.class);
                        //restaurants.add(restaurant);

                        if (child2.getKey().equals("hname")){
                            restaurant.setName(""+child2.getValue());

                        }
                        if (child2.getKey().equals("hvideo")){
                            restaurant.setVideo((""+child2.getValue()).split("=")[1]);
                        }
                        if (child2.getKey().equals("h_address")){
                            restaurant.setAddress(""+child2.getValue());
                        }
                        if (child2.getKey().equals("h_lat")){
                            restaurant.setLattitude(""+child2.getValue());
                        }

                        if (child2.getKey().equals("h_lng")){
                            restaurant.setLongitude(""+child2.getValue());
                        }

                        if (child2.getKey().equals("h_type_of_restaurant")){
                            restaurant.setTypeOfRestaurant(""+child2.getValue());
                        }

                        if (child2.getKey().equals("hambience_etime")){
                            restaurant.setAmbienceEndTime(Integer.parseInt(""+child2.getValue()));
                        }
                        if (child2.getKey().equals("hambience_stime")){
                            restaurant.setAmbienceStartTime(Integer.parseInt(""+child2.getValue()));
                        }
                        if (child2.getKey().equals("hclosing_time")){
                            restaurant.setClosingTime(""+child2.getValue());
                        }
                        if (child2.getKey().equals("hopening_time")){
                            restaurant.setOpeningTime(""+child2.getValue());
                        }
                        if (child2.getKey().equals("hcuisine_type")){
                            restaurant.setCuisineType(""+child2.getValue());
                        }
                        if (child2.getKey().equals("hsignature_etime")){
                            restaurant.setSignatureEndTime(Integer.parseInt(""+child2.getValue()));
                        }
                        if (child2.getKey().equals("hsignature_stime")){
                            restaurant.setSignatureStartTime(Integer.parseInt(""+child2.getValue()));
                        }
                        if (child2.getKey().equals("hid")){
                            restaurant.setId(""+child2.getValue());
                        }
                        if (child2.getKey().equals("h_cost_for_two")){
                            restaurant.setCostForTwo(Integer.parseInt(""+child2.getValue()));
                        }
                        if (child2.getKey().equals("h_phone")){
                            restaurant.setPhone(""+child2.getValue());
                        }
                    }
                    restaurants.add(restaurant);

                }
            }
        }
        mFeedsAdapter.addAll(restaurants);
        mFeedsAdapter.notifyDataSetChanged();
        progress.setVisibility(View.GONE);
    }


    void update(){

        if(CheckNetwork.isInternetAvailable(getActivity())) {

            textViewEmpty.setVisibility(View.GONE);
            progress.setVisibility(View.VISIBLE);
            progress.setIndeterminate(true);
            mRecyclerView.setVisibility(View.VISIBLE);
            nRecyclerView.setVisibility(View.VISIBLE);
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    getData(dataSnapshot);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
        else{

            textViewEmpty.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            nRecyclerView.setVisibility(View.INVISIBLE);
            new AlertDialog.Builder(getActivity())
                    .setTitle("No Internet Connection")
                    .setMessage("No Internet connection is available, Please check or try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.dismiss();
                            dialogInterface.cancel();
                            //Prompt the user once explanation has been shown
                        }
                    })
                    .create()
                    .show();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        update();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        mFeedsAdapter.removeAll();
//    }

    //    public interface OnGetDataListener {
//        //make new interface for call back
//        void onSuccess(DataSnapshot dataSnapshot);
//        void onStart();
//        void onFailure();
//    }

}
