package com.example.prakharagarwal.binge.MainScreen;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prakharagarwal.binge.LoginActivity;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.WalletActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    // private ViewPager viewPager;
    public FrameLayout fragment_container;

    TextView textViewLocation;

    String latitude = null;
    String longitude = null;
    private Menu menu;
    private FusedLocationProviderClient mFusedLocationClient;
    SearchFragment searchActivity;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    CardView cv;
    SharedPreferences sharedpreferences;
    //private ImageView searchIcon;
    MainActivityFragment mainActivityFragment = new MainActivityFragment();

    private EditText search_edittext;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    ProgressDialog dialog;
    private boolean couponFlag = false;
    private boolean couponFlag2 = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_detail, menu);
//        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
//        String uID = prefs.getString("username", null);
//        MenuItem menuItemLogin = menu.findItem(R.id.menu_login_status);
//        MenuItem menuItemUser = menu.findItem(R.id.menu_username);
//
//
//        if (uID != null) {
//            menuItemUser.setTitle(uID);
//            menuItemLogin.setTitle("Logout");
//
//        } else {
//            menuItemUser.setTitle("");
//            menuItemLogin.setTitle("Login");
//            //Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//        }
//
//        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);
        setContentView(R.layout.activity_main);
        textViewLocation = (TextView) findViewById(R.id.user_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        //Initializing the tablayout

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait");
        dialog.setCancelable(false);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        //hide the soft keyboard
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        fragment_container = findViewById(R.id.fragment_container);
        search_edittext = findViewById(R.id.search_edit_text);
        search_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchActivity = new SearchFragment();
                SearchFragment.activity = MainActivity.this;
                android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragment_container, searchActivity);
                transaction.addToBackStack("search");
                transaction.commit();
            }
        });


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (getIntent().getStringExtra("callingActivity") != null) {
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            setAddress();
        } else {
            checkLocationPermission();
        }

        createUI();

        LinearLayout locationLayout = (LinearLayout) findViewById(R.id.location_layout);
        locationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LocationSearchActivity.class);
                startActivityForResult(intent, 1);
            }
        });


        mNavigationView = findViewById(R.id.nav_view);
        checkLogin();
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        String uID = prefs.getString("username", null);
                        // set item as selected to persist highlight
                        menuItem.setChecked(false);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        switch (menuItem.getItemId()) {
                            case R.id.nav_redeem:
                                if (uID != null) {
                                    mDrawerLayout.closeDrawers();
                                    showRedeemDialog();

                                } else {
                                    mDrawerLayout.closeDrawers();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                                break;
                            case R.id.nav_wallet:
                                if (uID != null) {
                                    mDrawerLayout.closeDrawers();
                                    startActivity(new Intent(MainActivity.this, WalletActivity.class));


                                } else {
                                    mDrawerLayout.closeDrawers();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                                break;
                            case R.id.nav_logout:
                                mDrawerLayout.closeDrawers();
                                if (uID != null) {
                                    SharedPreferences.Editor editor = getSharedPreferences("Login", MODE_PRIVATE).edit();
                                    editor.remove("username").commit();
                                    Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                                    checkLogin();
                                }
                                break;
                        }
                        return true;
                    }
                });


    }

    private void checkLogin() {
        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String uID = prefs.getString("username", null);
        Menu nav_Menu = mNavigationView.getMenu();
        View header_view=mNavigationView.getHeaderView(0);
        TextView hiText=header_view.findViewById(R.id.hi_text);
        TextView nameText=header_view.findViewById(R.id.name_text);

        if (uID != null) {
            nav_Menu.findItem(R.id.nav_order).setVisible(true);
            nav_Menu.findItem(R.id.nav_logout).setVisible(true);
            hiText.setVisibility(View.VISIBLE);
            nameText.setText(prefs.getString("display_name",""));

        } else {
            nav_Menu.findItem(R.id.nav_order).setVisible(false);
            nav_Menu.findItem(R.id.nav_logout).setVisible(false);
            hiText.setVisibility(View.INVISIBLE);
            nameText.setText("Login");
            nameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    mDrawerLayout.closeDrawers();
                }
            });

        }
    }

    private void showRedeemDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        final Button redeem = layout.findViewById(R.id.redeem);
        final EditText code = layout.findViewById(R.id.code);

        builder.setView(layout)
                .setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        builder.show();
        redeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "Enter a coupon", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    checkCoupon(code.getText().toString().trim());
                }
            }
        });
    }

    private void checkCoupon(final String code) {

        dialog.show();
        couponFlag = false;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("coupons");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Coupon coupon = child.getValue(Coupon.class);
                    if (coupon.coupon_code.equalsIgnoreCase(code) && coupon.status == 1) {
                        redeemCoupon(code, coupon.amount);
                        couponFlag = true;
                        break;
                    }
                }
                if (!couponFlag) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Invalid or Expired Coupon", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }

    private void redeemCoupon(final String code, final double amount) {

        SharedPreferences prefs = getSharedPreferences("Login", Context.MODE_PRIVATE);
        final String uID = prefs.getString("username", null);

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        couponFlag2 = false;
        firebaseFirestore.collection("users_app/" + encodeEmail(uID) + "/coupons")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.exists()) {
                                    Map<String, Object> data = document.getData();
                                    if (data.get("coupon_code").toString().equalsIgnoreCase(code)) {
                                        Toast.makeText(MainActivity.this, "Coupon already redeemed", Toast.LENGTH_SHORT).show();
                                        couponFlag2 = true;
                                        dialog.dismiss();
                                        break;

                                    }
                                } else {

                                }
                            }
                            if (!couponFlag2) {
                                final String[] walletBalance = {"0"};
                                final long timestamp = Calendar.getInstance().getTimeInMillis();
                                Map<String, Object> newCoupon = new HashMap<>();
                                newCoupon.put("coupon_code", code.toUpperCase());
                                newCoupon.put("timestamp", timestamp);
                                newCoupon.put("amount", amount);
                                firebaseFirestore.collection("users_app/" + encodeEmail(uID) + "/coupons")
                                        .document().set(newCoupon);
                                firebaseFirestore.collection("users_app").document(encodeEmail(uID)).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Map<String, Object> data = document.getData();
                                                        if (data.containsKey("wallet_balance")) {
                                                            walletBalance[0] = ""+data.get("wallet_balance");
                                                        }
//                                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                                    } else {
//                                                Log.d(TAG, "No such document");
                                                    }
                                                } else {
//                                            Log.d(TAG, "get failed with ", task.getException());
                                                }
                                                if(walletBalance[0].equals(0)){
//                                                    Map<String>
                                                }
                                                Map<String,Double> wal=new HashMap<>();
                                                wal.put("wallet_balance",Double.parseDouble(walletBalance[0]) + amount);
                                                firebaseFirestore.collection("users_app").document(encodeEmail(uID)).set(wal,SetOptions.merge());
//                                                firebaseFirestore.collection("users_app")
//                                                        .document(encodeEmail(uID)).update("wallet_balance", Float.parseFloat(walletBalance[0]) + amount);
                                                Map<String, Object> trans = new HashMap<>();
                                                trans.put("credit_amount", amount);
                                                trans.put("credit_debit", 1);
                                                trans.put("debit_amount", 0);
                                                trans.put("purpose", "coupon:" + code.toUpperCase());
                                                trans.put("second_party", "Binge Team");
                                                trans.put("timestamp", timestamp);
                                                trans.put("wallet_balance_after", walletBalance[0] + amount);
                                                trans.put("wallet_balance_before", walletBalance[0]);
                                                firebaseFirestore.collection("users_app/" + encodeEmail(uID) + "/wallet_transactions")
                                                        .document().set(trans);

                                            }
                                        });
                                dialog.dismiss();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                                alertDialog.setCancelable(false)
                                        .setMessage("Congratulations! You have added " + amount + " rupees to your Binge wallet.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                alertDialog.create();
                                alertDialog.show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Some error occurred. Try again later", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

//
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
////                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
//                    } else {
////                        Log.d(TAG, "No such document");
//                    }
//                } else {
//                    Toast.makeText(MainActivity.this, "Could not Connect. Try again later", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        firebaseFirestore.collection("users_app").document(uID).update("payment", "done");

    }

    public EditText getSearch_edittext() {
        return search_edittext;
    }

    private void createUI() {
        //  MainActivityFragment mainActivityFragment=new MainActivityFragment();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        if (!mainActivityFragment.isAdded()) {
            transaction.add(R.id.fragment_container, mainActivityFragment);
            transaction.commit();
        } else {
            transaction.replace(R.id.fragment_container, new MainActivityFragment()).commit();
//            transaction.remove(mainActivityFragment);
//            transaction.add(R.id.fragment_container, new MainActivityFragment()).commit();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                latitude = data.getStringExtra("latitude");
                longitude = data.getStringExtra("longitude");
                Log.d("RISHABH LATITUDE 123", latitude + "");
                Log.d("RISHABH LONGITUDE 123", longitude + "");
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
                    if (locality != null && sublocality != null) {
                        textViewLocation.setText(sublocality + " " + locality);
//
                    } else
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
        checkLogin();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
//        finishAffinity();
    }

    public String encodeEmail(String email) {
        return email.replace(".", getString(R.string.encode_period))
                .replace("@", getString(R.string.encode_attherate))
                .replace("$", getString(R.string.encode_dollar))
                .replace("[", getString(R.string.encode_left_square_bracket))
                .replace("]", getString(R.string.encode_right_square_bracket));
    }

    public static class Coupon {
        String coupon_code;
        double amount;
        double status;

        public Coupon() {

        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public double getStatus() {
            return status;
        }

        public void setStatus(double status) {
            this.status = status;
        }
    }
}
