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

import com.example.prakharagarwal.binge.CheckNetwork;
import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends Fragment {


    RecyclerView mRecyclerView;
    FoodMainScreenAdapter mFoodAdapter;
    List<Food_MainScreen> mFood;

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

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_main_recycler_view);


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

        for (DataSnapshot child : dataSnapshot.getChildren()) {


            if (child.getKey().equals(getArguments().getString("id"))) {
                mFood.clear();
                for (DataSnapshot child1 : child.getChildren()) {
                    Food_MainScreen food = child1.getValue(Food_MainScreen.class);
                    mFood.add(food);
                }
            }
        }
        mFoodAdapter.notifyDataSetChanged();
    }


    void update() {

//        if(CheckNetwork.isInternetAvailable(getActivity())) {

//            textViewEmpty.setVisibility(View.GONE);
//            progress.setVisibility(View.VISIBLE);
//            progress.setIndeterminate(true);
//            mRecyclerView.setVisibility(View.VISIBLE);
//            nRecyclerView.setVisibility(View.VISIBLE);


//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    getData(dataSnapshot);
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });


//        }
//        else{

//            textViewEmpty.setVisibility(View.VISIBLE);
//            progress.setVisibility(View.GONE);
//            mRecyclerView.setVisibility(View.INVISIBLE);
//            nRecyclerView.setVisibility(View.INVISIBLE);
//            new AlertDialog.Builder(getActivity())
//                    .setTitle("No Internet Connection")
//                    .setMessage("No Internet connection is available, Please check or try again.")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                            dialogInterface.dismiss();
//                            dialogInterface.cancel();
//                            //Prompt the user once explanation has been shown
//                        }
//                    })
//                    .create()
//                    .show();
//        }


    }


}
