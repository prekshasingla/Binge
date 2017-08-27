package com.example.prakharagarwal.fingerlickingawesome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class DineoutFragment extends Fragment {
    RecyclerView mRecyclerView;
    VideoAdapter mFeedsAdapter;
    List<Restaurant> restaurants;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurants=new ArrayList<Restaurant>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dineout, container, false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());

        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.dineout_fragment_recycler_view);
        mFeedsAdapter= new VideoAdapter(getContext(),mRecyclerView,getChildFragmentManager(),restaurants,linearLayoutManager);
      try{
          mRecyclerView.setAdapter(mFeedsAdapter);
      }catch (NoClassDefFoundError e){

      }
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Log.e("position",""+linearLayoutManager.findLastCompletelyVisibleItemPosition());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();

        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    if (child.getKey().equals("table")) {
                        for (DataSnapshot child1 : child.getChildren()) {
                            Restaurant restaurant=new Restaurant();

                            for (DataSnapshot child2 : child1.getChildren()) {

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
                            }
                            restaurants.add(restaurant);

                        }
                    }
                }
                mFeedsAdapter.addAll(restaurants);
                mFeedsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return rootView;
    }
}
