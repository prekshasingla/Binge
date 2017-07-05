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

    List<String> videos;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        videos=new ArrayList<String>();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dineout, container, false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());

        mRecyclerView=(RecyclerView)rootView.findViewById(R.id.dineout_fragment_recycler_view);
       /*??*/ mFeedsAdapter= new VideoAdapter(getContext(),mRecyclerView,videos,getChildFragmentManager());
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
                            for (DataSnapshot child2 : child1.getChildren()) {
                                if (child2.getKey().equals("hvideo")){
                                        String video= (""+child2.getValue()).split("=")[1];
                                        videos.add(video);
                                }
                            }
                        }
                    }
                }
                mFeedsAdapter.addAll(videos);
                mFeedsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
