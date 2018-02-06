package com.example.prakharagarwal.binge.MainScreen;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.prakharagarwal.binge.CheckNetwork;
import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {


    RecyclerView mRecyclerView;
    FoodMainScreenAdapter mFoodAdapter;
    List<Food_MainScreen> mFood;
    List<Food_MainScreen> mFood2;
    TextView emptyView;
    ProgressBar progressBar;
    boolean flag;

    public static MainActivityFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        MainActivityFragment fragment = new MainActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFood = new ArrayList<>();
        mFood2= new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);
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

                if(((MainActivity)getActivity()).getLatitude()!=null && ((MainActivity)getActivity()).getLongitude()!=null)
                    flag=true;
                else
                    flag=false;
                getData(dataSnapshot);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        update();
        mFoodAdapter = new FoodMainScreenAdapter(mFood, getContext(), mRecyclerView, getFragmentManager());
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mFoodAdapter.isHeader(position) ? mLayoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setAdapter(mFoodAdapter);

        return rootView;
    }


    public void getData(DataSnapshot dataSnapshot) {
        mFood.clear();
        mFood2.clear();
        progressBar.setVisibility(View.GONE);
        for (DataSnapshot child : dataSnapshot.getChildren()) {
            if (child.getKey().equals(getArguments().getString("id"))) {
                for (DataSnapshot child1 : child.getChildren()) {
                    Food_MainScreen food = child1.getValue(Food_MainScreen.class);
                    mFood2.add(food);
                    if(!flag)
                        mFood.add(food);
                    else
                    if( flag && calRadius(food.getLatitude(),food.getLongitude()))
                    mFood.add(food);
                }
            }
        }
        Collections.shuffle(mFood);
        Collections.shuffle(mFood2);
        if(mFood.size()==0 && mFood2.size()==0)
        {
            emptyView.setVisibility(View.VISIBLE);
        }else
        if (mFood.size() == 0) {
            emptyView.setVisibility(View.GONE);
            mFoodAdapter.addAll(mFood2);
            mFoodAdapter.setHeaderEnabled(true);
            mFoodAdapter.notifyDataSetChanged();
        } else {
            emptyView.setVisibility(View.GONE);
            mFoodAdapter.setHeaderEnabled(false);
            mFoodAdapter.addAll(mFood);
            mFoodAdapter.notifyDataSetChanged();
        }

    }

    private boolean calRadius(double lat2,double lon2) {
        double lat1=Double.parseDouble(((MainActivity)getActivity()).getLatitude());
        double lon1=Double.parseDouble(((MainActivity)getActivity()).getLongitude());
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if(dist<=6)
        return true;
        else
            return false;
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

}

