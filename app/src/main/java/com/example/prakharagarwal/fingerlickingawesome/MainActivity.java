package com.example.prakharagarwal.fingerlickingawesome;

import android.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ViewPagerAdapter mviewPagerAdapter;
    ViewPager mviewPager;
    TabLayout mtabLayout;
    String latitude=null;
    String longitude=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        TextView textViewLocation=(TextView)findViewById(R.id.user_location);

        mviewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mviewPagerAdapter.addFragment(new DineoutFragment(), "Dineout");
        mviewPager = (ViewPager) findViewById(R.id.viewpager);
        mtabLayout = (TabLayout) findViewById(R.id.tablayout);

        mviewPager.setAdapter(mviewPagerAdapter);
        mtabLayout.setupWithViewPager(mviewPager);

        if(checkUserPermission()) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gps);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
            }
            else
            {
                // locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, gps);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
            }
        }

        if(latitude!=null && longitude!=null) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                if (!addresses.isEmpty()) {
                    Address add = addresses.get(0);
                    String country=add.getCountryName();
                    String city=(add.getAddressLine(2)).split(",")[0];
                    if(city!=null && country!=null)
                     textViewLocation.setText(city+", "+country);
                    else if(country!=null)
                        textViewLocation.setText(country);
                    else if(city!=null)
                        textViewLocation.setText(city);
                    else
                        Toast.makeText(this, "No Location Available", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "No Location", Toast.LENGTH_LONG).show();
                }

            } catch (IOException e) {
            }
        }



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
