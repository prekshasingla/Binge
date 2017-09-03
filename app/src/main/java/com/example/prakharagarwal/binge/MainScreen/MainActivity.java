package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ViewPagerAdapter mviewPagerAdapter;
    ViewPager mviewPager;
    String latitude=null;
    String longitude=null;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //TextView textViewLocation = (TextView) findViewById(R.id.user_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        // getSupportActionBar().setDisplayShowTitleEnabled(true);

//        sharedpreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//
//        editor.putString("userid","123");
//        editor.commit();

//        SharedPreferences mySPrefs = this.getSharedPreferences("User", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySPrefs.edit();
//        editor.remove("userid");
//        editor.apply();

//        SharedPreferences prefs = this.getSharedPreferences("User", Context.MODE_PRIVATE);
//        prefs.edit().clear();
//        String uID = prefs.getString("userid","Null");
//        Log.e("Uid",uID);


        mviewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mviewPagerAdapter.addFragment(new DineoutFragment(), "Dineout");
        mviewPager = (ViewPager) findViewById(R.id.viewpager);

        mviewPager.setAdapter(mviewPagerAdapter);
        // Log.e("adapter","set");



        if (checkUserPermission()) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gps);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
            } else if (locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
                // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gps);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
            }
        }

        if (latitude != null && longitude != null) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                if (!addresses.isEmpty()) {
                    Address add = addresses.get(0);
                    String country = add.getCountryName();
                    String city = (add.getAddressLine(2)).split(",")[0];
//                    if (city != null && country != null)
                    //    textViewLocation.setText(city + ", " + country);
//                    else if (country != null)
//                      //  textViewLocation.setText(country);
//                    else if (city != null)
//                        //textViewLocation.setText(city);
//                    else
//                        Toast.makeText(this, "No Location Available", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "No Location", Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
            }
        }


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
           // startActivity(new Intent(this, SettingsActivity.class));
            SharedPreferences prefs =getSharedPreferences("Login", Context.MODE_PRIVATE);
            String uID = prefs.getString("username", null);
            if(uID!=null)
            {
                SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();
                editor.remove("username").commit();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean checkUserPermission()
    {
        int statusintfine = getPackageManager().checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,getPackageName());
        int statusintcoarse = getPackageManager().checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION,getPackageName());
        boolean flag=false;

        if (statusintfine != PackageManager.PERMISSION_GRANTED && statusintcoarse != PackageManager.PERMISSION_GRANTED) {
            flag = false;
        }
        else { flag=true; }
        return flag;
    }

}
