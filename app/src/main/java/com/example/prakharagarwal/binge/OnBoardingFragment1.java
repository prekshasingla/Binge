package com.example.prakharagarwal.binge;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prakharagarwal.binge.MainScreen.MainActivity;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OnBoardingFragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OnBoardingFragment1 extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private String mParam1;


    public OnBoardingFragment1() {
        // Required empty public constructor
    }


    public static OnBoardingFragment1 newInstance(String param1) {
        OnBoardingFragment1 fragment = new OnBoardingFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView= inflater.inflate(R.layout.fragment_on_boarding_fragment1, container, false);
        TextView text1= (TextView) rootView.findViewById(R.id.onboarding_text1);
        TextView text2= (TextView) rootView.findViewById(R.id.onboarding_text2);
        TextView text3= (TextView) rootView.findViewById(R.id.onboarding_text3);
//        ImageView image1= (ImageView) rootView.findViewById(R.id.onboarding_image1);
        if(getArguments().get(ARG_PARAM1).equals("screen1")){
            text1.setText("DISCOVER");
            text2.setText("Find out what's unique and new at restaurants near you.");
            text3.setText("SKIP");
//            image1.setBackgroundResource(R.drawable.onboarding1);
        }else{
            text1.setText("VIDEO MENUS");
            text2.setText("No more guessing, watch a short video to see what you order.");
            text3.setText("CONTINUE");
//            image1.setBackgroundResource(R.drawable.onboarding2);
        }
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("onboarding", MODE_PRIVATE).edit();
                editor.putString("status","done");
                editor.apply();
                Intent intent=new Intent(getActivity(), MainActivity.class);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
       return rootView;
    }

}
