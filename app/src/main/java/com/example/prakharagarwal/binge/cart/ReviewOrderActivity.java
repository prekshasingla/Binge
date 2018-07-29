package com.example.prakharagarwal.binge.cart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.prakharagarwal.binge.R;

public class ReviewOrderActivity extends AppCompatActivity {

    TextView pay_amount, error_text;
    TextView restaurant_name;
    Toolbar toolbar;
    EditText email, name, phone;
    Button payButton;
    float paymentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_order);
        pay_amount = findViewById(R.id.text_pay_amount);
        restaurant_name = findViewById(R.id.text_restaurant_name);
        email = findViewById(R.id.user_email_review_order);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
        email.setClickable(false);
        email.setCursorVisible(false);
        name = findViewById(R.id.user_name_review_order);
        phone = findViewById(R.id.user_mobile_review_order);
        payButton = findViewById(R.id.pay_now_btn_review_order);
        error_text = findViewById(R.id.error_text_review_order);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // perform whatever you want on back arrow click
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        pay_amount.setText("Payment Amount  =" + intent.getFloatExtra("payamount", 0) + "");
        restaurant_name.setText("Restaurant Name - " + intent.getStringExtra("restaurant"));
        paymentAmount = intent.getFloatExtra("payamount", 0);


        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        String uID = prefs.getString("username", null);
        final String UName = prefs.getString("testname", null);
        final String uPhone = prefs.getString("userphone", null);

        if (uID != null) {
            email.setText(uID);
        }
        if (UName != null) {
            name.setText(UName);
        }
        if (uPhone != null) {
            phone.setText(uPhone);
        }


        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(name.getText()) && phone.getText().length() < 10) {
                    error_text.setVisibility(View.VISIBLE);
                    error_text.setText("Name cannot be empty or mobile no must be 10 digit");
                    Toast.makeText(ReviewOrderActivity.this, "Please check the username or mobile no ", Toast.LENGTH_SHORT).show();
                } else {
                    if (paymentAmount == 0) {
                        Toast.makeText(ReviewOrderActivity.this, "Please add items to cart", Toast.LENGTH_SHORT).show();

                    } else {
                        if (UName == null) {
                            editor.putString("testname", name.getText().toString().trim()).commit();
                        }
                        if (uPhone == null) {
                            editor.putString("userphone", phone.getText().toString()).commit();
                        }

                        Toast.makeText(ReviewOrderActivity.this, "Opening the Payment GateWay...", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}
