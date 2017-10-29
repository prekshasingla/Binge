package com.example.prakharagarwal.binge.MainScreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prakharagarwal.binge.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sarthak on 18-10-2017.
 */

public class Tab1 extends Fragment {


    RecyclerView mRecyclerView;
    FoodMainScreenAdapter mFoodAdapter;
    List<Food_MainScreen> mFood;
//    GridLayoutManager mLayoutManager;
    Food_MainScreen a  = new Food_MainScreen();
    Food_MainScreen b = new Food_MainScreen();
    Food_MainScreen c = new Food_MainScreen();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFood = new ArrayList<Food_MainScreen>();

        a.setDish_id("Hello");
        a.setPoster_url("https://pbs.twimg.com/profile_images/852028772878503937/JH5x4wUL.jpg");
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



    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1, container, false);
        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.dineout_recycler);

        mFood.add(a);
        mFood.add(b);
        mFood.add(c);
//        mFoodAdapter.addAll(mFood);

        mFoodAdapter = new FoodMainScreenAdapter(mFood, getContext(), mRecyclerView, getFragmentManager());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mFoodAdapter);

        return  rootView;
    }
}