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
    //    GridLayoutManager mLayoutManager;
    Food_MainScreen a  = new Food_MainScreen();
    Food_MainScreen b = new Food_MainScreen();
    Food_MainScreen c = new Food_MainScreen();
    Food_MainScreen a1  = new Food_MainScreen();
    Food_MainScreen b1 = new Food_MainScreen();
    Food_MainScreen c1 = new Food_MainScreen();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFood = new ArrayList<Food_MainScreen>();

        a.setDish_id("Hello");
        a.setPoster_url("https://firebasestorage.googleapis.com/v0/b/bingetesting.appspot.com/o/asd.jpg?alt=media&token=75761a78-25cf-4467-a42c-d469e1baf324");
        a.setRest_id("hi");
        a.setRest_name("yo yo ");
        b.setDish_id("Hello");
        b.setPoster_url("https://pbs.twimg.com/profile_images/852028772878503937/JH5x4wUL.jpg");
        b.setRest_id("hi");
        b.setRest_name("yo yo ");
        c.setDish_id("Hello");
        c.setPoster_url("https://pbs.twimg.com/profile_images/852028772878503937/JH5x4wUL.jpg");
        c.setRest_id("hi");
        c.setRest_name("yo yo ");
        a1.setDish_id("Hello");
        a1.setPoster_url("https://pbs.twimg.com/profile_images/852028772878503937/JH5x4wUL.jpg");
        a1.setRest_id("hi");
        a1.setRest_name("yo yo ");
        b1.setDish_id("Hello");
        b1.setPoster_url("https://pbs.twimg.com/profile_images/852028772878503937/JH5x4wUL.jpg");
        b1.setRest_id("hi");
        b1.setRest_name("yo yo ");
        c1.setDish_id("Hello");
        c1.setPoster_url("https://pbs.twimg.com/profile_images/852028772878503937/JH5x4wUL.jpg");
        c1.setRest_id("hi");
        c1.setRest_name("yo yo ");




    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_main_fragment, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.fragment_main_recycler_view);

        mFood.add(a);
        mFood.add(b);
        mFood.add(c);
        mFood.add(a1);
        mFood.add(b1);
        mFood.add(c1);

//        mFoodAdapter.addAll(mFood);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("mainscreen");
        ref.orderByChild("category").equalTo("fine dining").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                Log.d("data",dataSnapshot+"");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });



//        update();
        mFoodAdapter = new FoodMainScreenAdapter(mFood, getContext(), mRecyclerView, getFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mFoodAdapter);

        return  rootView;
    }


    public void getData(DataSnapshot dataSnapshot) {
        // mFeedsAdapter.removeAll();
        //mRecommendAdapter.removeAll();
        for (DataSnapshot child : dataSnapshot.getChildren()) {

            Log.d("data",child+"");

//            if (child.getKey().equals("mainscreen")) {
//
//                for (DataSnapshot child1 : child.getChildren()) {
//                    Restaurant restaurant = new Restaurant();
//
//                    for (DataSnapshot child2 : child1.getChildren()) {
//
//
//                    }
//                    //restaurants.add(restaurant);
//
//                }
//            }


            //mFeedsAdapter.addAll(restaurants);
            //mFeedsAdapter.notifyDataSetChanged();

        }
    }


    void update(){

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

