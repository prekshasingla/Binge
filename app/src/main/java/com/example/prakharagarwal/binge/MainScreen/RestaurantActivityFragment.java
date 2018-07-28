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

import com.example.prakharagarwal.binge.Manifest;
import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class RestaurantActivityFragment extends Fragment {


    RecyclerView mRecyclerView;
    FoodMainScreenAdapter mFoodAdapter;
    List<Menu> mFood;
    List<Menu> mFoodData;
    List<Menu> category_food;
    TextView emptyView;
    ProgressBar progressBar;

    public RestaurantActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFood = new ArrayList<>();
        mFoodData = new ArrayList<>();
        category_food=new ArrayList<>();
    }

    public static RestaurantActivityFragment newInstance(String category_id) {
        Bundle args = new Bundle();
        args.putString("category_id", category_id);
        RestaurantActivityFragment fragment = new RestaurantActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_main_recycler_view);
        emptyView = (TextView) rootView.findViewById(R.id.text_menu_empty);
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_activity_progress);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);

        String category_id=getArguments().getString("category_id");
        List<Menu> menuList=PassingData.getMenuList();

        for(int a=0;a<=menuList.size()-1;a++)
        {
            if(category_id.equals(menuList.get(a).getCategory()))
            {
              category_food.add(menuList.get(a));
            }
        }

        mFoodAdapter = new FoodMainScreenAdapter(category_food, getActivity(), mRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mFoodAdapter);

        progressBar.setVisibility(View.INVISIBLE);
        return rootView;
    }

}
