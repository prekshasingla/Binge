package com.example.prakharagarwal.binge.StoriesMenu;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.prakharagarwal.binge.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    RecyclerView mRecyclerView;
    MenuAdapter menuAdapter;
    List<Menu> menus;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menus=new ArrayList<Menu>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
    LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getActivity());

    mRecyclerView=(RecyclerView)rootView.findViewById(R.id.menu_fragment_recycler_view);
    menuAdapter= new MenuAdapter(getContext(),menus);
    try{
        mRecyclerView.setAdapter(menuAdapter);
    }catch (NoClassDefFoundError e){

    }
    mRecyclerView.setLayoutManager(linearLayoutManager);



    menuAdapter.addAll(menus);
    menuAdapter.notifyDataSetChanged();

    return rootView;
}

}
