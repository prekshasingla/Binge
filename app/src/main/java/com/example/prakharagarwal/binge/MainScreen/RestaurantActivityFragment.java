package com.example.prakharagarwal.binge.MainScreen;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantActivityFragment extends Fragment {


    RecyclerView mRecyclerView;
    FoodMainScreenAdapter mFoodAdapter;
    List<Food_MainScreen> mFood;
    List<Food_MainScreen> mFoodData;

    TextView emptyView;
    ProgressBar progressBar;

    public RestaurantActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFood = new ArrayList<>();
        mFoodData = new ArrayList<>();


    }

    public static RestaurantActivityFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        RestaurantActivityFragment fragment = new RestaurantActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //return inflater.inflate(R.layout.fragment_restaurant, container, false);

        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_main_recycler_view);
        emptyView = (TextView) rootView.findViewById(R.id.text_menu_empty);
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_activity_progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("mainscreen");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                getData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        update();
        mFoodAdapter = new FoodMainScreenAdapter(mFood, getContext(), mRecyclerView, getFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mFoodAdapter);

        return rootView;
    }

    public void getData(DataSnapshot dataSnapshot) {
        mFood.clear();
        int flag=0;
        progressBar.setVisibility(View.GONE);
        for (DataSnapshot child : dataSnapshot.getChildren()) {
//            if (child.getKey().equals(getArguments().getString("id"))) {
            for (DataSnapshot child1 : child.getChildren()) {
                flag = 0;
                    Food_MainScreen food = child1.getValue(Food_MainScreen.class);
                    mFoodData.add(food);
//                    }

//            }
            }
        }

        for(int i=0; i<mFoodData.size();i++){
                    if(mFoodData.get(i).getRestaurant_id().equals(getArguments().getString("id"))){
                        mFood.add(mFoodData.get(i));
                    }
        }
        if (mFood.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
        mFoodAdapter.addAll(mFood);
        mFoodAdapter.notifyDataSetChanged();
    }

}
