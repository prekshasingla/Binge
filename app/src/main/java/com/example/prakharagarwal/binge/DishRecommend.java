package com.example.prakharagarwal.binge;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DishRecommend extends AppCompatActivity {

    EditText dishName;
    EditText restName;
    EditText place;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_recommend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dishName= (EditText) findViewById(R.id.dish_name);
        restName=(EditText)findViewById(R.id.rest_name);
        place= (EditText)findViewById(R.id.place);
        mDatabase= FirebaseDatabase.getInstance();
        final TextView error=(TextView)findViewById(R.id.submit_error);
        Button recommendSubmit= (Button)findViewById(R.id.recommend_submit_button);
        recommendSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dishName.getText().toString().trim().equals("")
                        && !place.getText().toString().trim().equals("")
                        && !restName.getText().toString().trim().equals("")){
                    error.setVisibility(View.INVISIBLE);
                    saveRecommendation();
                }else{
                    error.setVisibility(View.VISIBLE);
                error.setText("Fields cannot be blank");
                }
            }
        });
    }

    private void saveRecommendation() {
        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String uID = prefs.getString("username", null);


        if (uID != null) {
            DatabaseReference ref1 = mDatabase.getReference().child("recommendations").child(encodeEmail(uID));
            Recommendation recommendation=new Recommendation();
            recommendation.dishName=dishName.getText().toString();
            recommendation.place=place.getText().toString();
            recommendation.rest=restName.getText().toString();
            ref1.setValue(recommendation);
            Toast.makeText(this, "Thank You for yor recommendation", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }
    public String encodeEmail(String email){
        return email.replace(".",getString(R.string.encode_period))
                .replace("@",getString(R.string.encode_attherate))
                .replace("$",getString(R.string.encode_dollar))
                .replace("[",getString(R.string.encode_left_square_bracket))
                .replace("]",getString(R.string.encode_right_square_bracket));
    }
    public static class Recommendation{
        String dishName;
        String place;
        String rest;
    }
}
