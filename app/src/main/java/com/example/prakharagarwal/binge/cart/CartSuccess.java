package com.example.prakharagarwal.binge.cart;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.prakharagarwal.binge.MainScreen.LocationSearchActivity;
import com.example.prakharagarwal.binge.MainScreen.MainActivity;
import com.example.prakharagarwal.binge.MainScreen.MySharedPreference;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.VolleySingleton;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.internal.IPolylineDelegate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.LongToIntFunction;

public class CartSuccess extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener, OnMapReadyCallback {

    private Toolbar toolbar;
    private GoogleApiClient mGoogleApiClient;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 98;
    private LocationRequest mLocationRequest;
    private int REQUEST_CHECK_SETTINGS = 88;
    private String LOG_TAG = "CArt Success Activity";

    private String orderID;
    private FirebaseFirestore db;
    private double latitude = 0;
    private double longitude = 0;
    private GoogleMap googleMap = null;
    private Marker movemarker;
    private Button received, cooking, served;

    TextView eta_time;
    Polyline polyline = null;
    Double rest_latitude;
    Double rest_longitude;
    String resturant_id;
    Boolean dialogflag=false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_success);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        received = findViewById(R.id.recived_status);
        cooking = findViewById(R.id.cooking_status);
        served = findViewById(R.id.served_status);



        db = FirebaseFirestore.getInstance();

        eta_time = (TextView) findViewById(R.id.eta_time);
        orderID = getIntent().getStringExtra("orderId");
        rest_latitude=getIntent().getDoubleExtra("latitude",0.0);
        rest_longitude=getIntent().getDoubleExtra("longitude",0.0);
        resturant_id=getIntent().getStringExtra("resturant_id");

        //save the data to shared preference
        final MySharedPreference sharedPreference=new MySharedPreference(CartSuccess.this);
        sharedPreference.savedmapactivity_set_latitude_longitude_flag(rest_latitude,rest_longitude);
        sharedPreference.savedmapactivity_set_orderID_restaurant_id(orderID,resturant_id);
        sharedPreference.savedmapactivity_set_flag(true);

        Log.d("RISHABH", "ORDER ID IS THE " + orderID);
        Log.d("RISHABH", "LATITUDE IS THE " + String.valueOf(getIntent().getDoubleExtra("latitude",0.0)) + " LONGITUDE IS THE " + String.valueOf(getIntent().getDoubleExtra("longitude",0.0)));

        mGoogleApiClient = new GoogleApiClient.Builder(CartSuccess.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(CartSuccess.this)
                .addOnConnectionFailedListener(CartSuccess.this)
                .build();
        checkLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final DocumentReference docRef = db.collection("orders/" + resturant_id + "/PreOrder").document(orderID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("RISHABH", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d("RISHABH", "Current data: " + snapshot.getData());
                    String order_status = snapshot.getString("status");
                    Log.d("RISHABH", "STRING DATA IS THE " + order_status);
                    if (order_status != null) {
                        if(order_status.equals("Recieved")) {
                            Log.d("RISHABH","Status 1");
                            received.setBackgroundColor(Color.GREEN);
                            cooking.setBackgroundColor(Color.GRAY);
                            served.setBackgroundColor(Color.GRAY);
                        }
                            else if(order_status.equals("MealPrepared")) {
                            Log.d("RISHABH","Status 2");
                            received.setBackgroundColor(Color.GREEN);
                            cooking.setBackgroundColor(Color.GREEN);
                            served.setBackgroundColor(Color.GRAY);
                        }
                            else if(order_status.equals("Delivered")) {
                            Log.d("RISHABH","Status 3");
//                            received.setBackgroundColor(Color.GREEN);
//                            cooking.setBackgroundColor(Color.GREEN);
//                            served.setBackgroundColor(Color.GREEN);
                            if(!dialogflag) {
                                dialogflag=true;
                                final Dialog dialog = new Dialog(CartSuccess.this);
                                dialog.setContentView(R.layout.thankyou_layout);
                                dialog.setCancelable(false);
                                dialog.show();
                                Button button = dialog.findViewById(R.id.thankyou_btn);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.cancel();
                                        startActivity(new Intent(CartSuccess.this, MainActivity.class));
                                        //MySharedPreference sharedPreference1=new MySharedPreference(CartSuccess.this);
                                        sharedPreference.savedmapactivity_set_flag(false);
                                        finish();


                                    }
                                });
                            }
                        }
                        else
                        {
                            Log.d("RISHABH","Status 4");
                            received.setBackgroundColor(Color.GRAY);
                            cooking.setBackgroundColor(Color.GRAY);
                            served.setBackgroundColor(Color.GRAY);
                        }
                    }
                } else {
                    Log.d("RISHABH", "Current data: null");
                }
            }
        });



    }

    public void checkLocationPermission() {

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
                                ActivityCompat.requestPermissions(CartSuccess.this,
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
        } else {
            Log.d("RISHABH","CREATE LOCATION REQUEST 1");
            createLocationRequest();
//            mGoogleApiClient.connect();
        }
    }

    protected void createLocationRequest() {
        Log.d("RISHABH","CREATE LOCATION REQUEST 2");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                mGoogleApiClient.connect();
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(CartSuccess.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            mGoogleApiClient.connect();
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
                        createLocationRequest();
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
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10 * 1000);
        try {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } catch (SecurityException e) {
            Log.e(LOG_TAG, e.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "google api client connection suspended");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "google api client connection failed");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(LOG_TAG, "location" + location.toString());
        Map<String, Object> locationMap = new HashMap<>();
        locationMap.put("location_long", location.getLongitude());
        locationMap.put("location_lat", location.getLatitude());
        db.collection("orders/" + resturant_id + "/PreOrder").document(orderID)
                .set(locationMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(LOG_TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(LOG_TAG, "Error writing document", e);
                    }
                });
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        changePositionOnMap();
        calculateTime();
        LatLng origin = new LatLng(latitude, longitude);
        LatLng dest = new LatLng(rest_latitude, rest_longitude);

        Log.d("RISHABH", "LATITUDE IS THE " + rest_latitude + " LONGITUDE IS THE " + rest_longitude);
// Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);
        DownloadTask downloadTask = new DownloadTask();

// Start downloading json data from Google Directions API
        downloadTask.execute(url);

    }



    private void calculateTime() {
        // Get a RequestQueue
        RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + String.valueOf(latitude) + "," + String.valueOf(longitude) + "&destinations=" + String.valueOf(rest_latitude) + "," + String.valueOf(rest_longitude) + "&key=AIzaSyD3mElGj7e1rE8sOdEamKJs-2S4JuIuSGA";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Do something with the response
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            if (responseJson.get("status").equals("OK")) {
                                Log.d("RISHABH", "RESPONSE IS THE " + response);
                                JSONArray rows = responseJson.getJSONArray("rows");
                                JSONArray elements = ((JSONObject) rows.get(0)).optJSONArray("elements");
                                JSONObject firstElement = (JSONObject) elements.get(0);
                                if (firstElement.get("status").equals("OK")) {
                                    JSONObject distance = firstElement.getJSONObject("distance");
                                    String km = distance.get("text") + "";
                                    JSONObject duration = firstElement.getJSONObject("duration");
                                    eta_time.setText(duration.get("text") + " " + km);

                                    Map<String, Object> durationMap = new HashMap<>();
                                    durationMap.put("time_to_reach", duration.get("text"));
                                    db.collection("orders/" + resturant_id + "/preOrder").document(orderID)
                                            .set(durationMap, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(LOG_TAG, "DocumentSnapshot successfully written!");
                                                    Toast.makeText(CartSuccess.this, "success response", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(LOG_TAG, "Error writing document", e);
                                                }
                                            });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("RISHABH", "INSIDE THE CATCH EXEPTION");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.d("RISHABH", "API ERROR" + error.toString());
                        Toast.makeText(CartSuccess.this, "Error in response", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(stringRequest);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {


        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("RISHABH", "DATA OF THE LOCATION" + data);

            br.close();

        } catch (Exception e) {
            Log.d("Exception while", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Rishabh Background", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            List<Polyline> polylineList = new ArrayList<>();
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                if (polyline == null) {
                    Log.d("RISHABH MAP", "PolylineList is null");
                    polyline = googleMap.addPolyline(lineOptions);

                } else {
                    //      polylineList.clear();
                    polyline.remove();
                    Log.d("RISHABH MAP", "PolylineList is clear");
                    polyline = googleMap.addPolyline(lineOptions);

                }
            }
        }
    }

    private void changePositionOnMap() {
        Log.d("RISHABH","CHANGE POSITION ON MAP");
        LatLng current = null;
        if (latitude != 0 && longitude != 0) {
            current = new LatLng(latitude, longitude);

            if (movemarker == null) {
                movemarker = googleMap.addMarker(new MarkerOptions().position(current)
                        .title("Your Current Location"));
                googleMap.addMarker(new MarkerOptions().position(new LatLng(rest_latitude, rest_longitude)).title(resturant_id + " Restaurant"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 12.0f));
                CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15);
                googleMap.animateCamera(cameraUpdate);
            } else {
                movemarker.remove();
                movemarker = googleMap.addMarker(new MarkerOptions().position(current)
                        .title("Your Current Location").snippet("Your are here").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)));
                CameraUpdate cameraUpdate= CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude),15);
                googleMap.animateCamera(cameraUpdate);

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.reconnect();

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        Toast.makeText(this, "Sorry you cannot back until your food will be served in the restaurant", Toast.LENGTH_SHORT).show();
    }
}
//recived(1) cooking(2) served(3)