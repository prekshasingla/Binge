package com.example.prakharagarwal.binge.MainScreen;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.VolleySingleton;
import com.example.prakharagarwal.binge.cart.CartSuccess;
import com.example.prakharagarwal.binge.cart.NewCartActivity;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {

    private TabLayout tabLayout;

   // private ViewPager viewPager;
    public FrameLayout fragment_container;

    TextView textViewLocation;

    String latitude = null;
    String longitude = null;
    private Menu menu;
    private FusedLocationProviderClient mFusedLocationClient;
    SearchActivity searchActivity;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    CardView cv;
    SharedPreferences sharedpreferences;
    //private ImageView searchIcon;
    MainActivityFragment mainActivityFragment=new MainActivityFragment();

    private EditText search_edittext;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String uID = prefs.getString("username", null);
        MenuItem menuItemLogin = menu.findItem(R.id.menu_login_status);
        MenuItem menuItemUser = menu.findItem(R.id.menu_username);


        if (uID != null) {
            menuItemUser.setTitle(uID);
            menuItemLogin.setTitle("Logout");

        } else {
            menuItemUser.setTitle("");
            menuItemLogin.setTitle("Login");
            //Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        }

        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_login_status) {
            if (item.getTitle().equals("Logout")) {
                SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String uID = prefs.getString("username", null);
                if (uID != null) {
                    SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();
                    editor.remove("username").commit();
                    updateMenuTitles();
                }
            } else {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateMenuTitles() {
        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String uID = prefs.getString("username", null);
        if (menu != null) {
            MenuItem menuItemLogin = menu.findItem(R.id.menu_login_status);
            MenuItem menuItemUser = menu.findItem(R.id.menu_username);

            if (uID != null) {
                menuItemUser.setVisible(true);
                menuItemUser.setTitle(uID);
                menuItemLogin.setTitle("Logout");

            } else {
                menuItemUser.setVisible(false);
                menuItemLogin.setTitle("Login");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        setContentView(R.layout.activity_main);
        textViewLocation = (TextView) findViewById(R.id.user_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing the tablayout


        //hide the soft keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


//        Log.v("RishabhSharedPreference","Starting shared prefernce");
//        //get data from the shared preference because if user already placed the preorder then we directly switch to the map activity
//        MySharedPreference sharedPreference=new MySharedPreference(MainActivity.this);
//        if(sharedPreference.savedmapactivity_get_flag()==true)
//        {
//            Log.v("RishabhSharedPreference","Inside shared prefernce "+sharedPreference.savedmapactivity_get_flag());
//            Intent intent = new Intent(MainActivity.this, CartSuccess.class);
//            intent.putExtra("orderId", sharedPreference.savedmapactivity_get_orderID());
//            intent.putExtra("latitude", sharedPreference.savedmapactivity_get_latitude());
//            intent.putExtra("longitude", sharedPreference.savedmapactivity_get_longitude());
//            intent.putExtra("resturant_id",sharedPreference.savedmapactivity_restaurantID());
//            startActivity(intent);
//        }
//        Log.v("RishabhSharedPreference","OutSide shared prefernce "+sharedPreference.savedmapactivity_get_flag());

        fragment_container=findViewById(R.id.fragment_container);
        search_edittext=findViewById(R.id.search_edit_text);
        search_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
               // search_edittext.setFocusable(true);
               // search_edittext.setFocusableInTouchMode(true);
                searchActivity=new SearchActivity();
                SearchActivity.activity=MainActivity.this;
                android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_container,searchActivity);
                transaction.addToBackStack("search");
                transaction.commit();
            }
        });


//        search_edittext.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.setFocusable(true);
//                v.setFocusableInTouchMode(true);
//                searchActivity=new SearchActivity();
//                android.support.v4.app.FragmentManager manager=getSupportFragmentManager();
//                android.support.v4.app.FragmentTransaction transaction=manager.beginTransaction();
//                transaction.replace(R.id.fragment_container,searchActivity);
//                transaction.addToBackStack("search");
//                transaction.commit();
//                return false;
//
//
//            }
//        });





        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (getIntent().getStringExtra("callingActivity") != null) {
            latitude=getIntent().getStringExtra("latitude");
            longitude=getIntent().getStringExtra("longitude");
            setAddress();
        } else {
            checkLocationPermission();
        }
//        latitude=""+LocationConstants.LAT_CP;
//        longitude=""+LocationConstants.LON_CP;
        createUI();

        LinearLayout locationLayout = (LinearLayout) findViewById(R.id.location_layout);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        //  update();


    }

    public EditText getSearch_edittext() {
        return search_edittext;
    }

    private void createUI() {
      //  MainActivityFragment mainActivityFragment=new MainActivityFragment();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        if(!mainActivityFragment.isAdded()) {
            transaction.add(R.id.fragment_container, mainActivityFragment);
            transaction.commit();
        }
        else
        {
            transaction.remove(mainActivityFragment);
            transaction.add(R.id.fragment_container, new MainActivityFragment()).commit();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                latitude=data.getStringExtra("latitude");
                longitude=data.getStringExtra("longitude");
                Log.d("RISHABH LATITUDE 123",latitude+"");
                Log.d("RISHABH LONGITUDE 123",longitude+"");
                setAddress();
                createUI();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Required")
                        .setMessage("Please give permission to access your location")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = "" + location.getLatitude();
                                longitude = "" + location.getLongitude();
                                setAddress();
                            } else {
                                textViewLocation.setText("Unavailable");
                            }

                        }
                    });
            return true;
        }
    }

    public void setAddress() {
        if (latitude != null && longitude != null) {
            try {

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                if (!addresses.isEmpty()) {
                    Address add = addresses.get(0);
                    String locality = add.getLocality();
                    String sublocality = add.getSubLocality();
                    if (locality != null && sublocality != null)
                    {
                        textViewLocation.setText(sublocality + " " + locality);
//
                    }
                    else
                        Toast.makeText(this, "No Location Available", Toast.LENGTH_LONG).show();
//

                } else {
                    Toast.makeText(this, "No Location", Toast.LENGTH_LONG).show();
//
                }

            } catch (IOException e) {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        mFusedLocationClient.getLastLocation()
                                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {
                                            // Logic to handle location object
                                            latitude = "" + location.getLatitude();
                                            longitude = "" + location.getLongitude();
                                        }
                                    }
                                });
                        setAddress();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //update();
        updateMenuTitles();
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        finishAffinity();
    }
}
