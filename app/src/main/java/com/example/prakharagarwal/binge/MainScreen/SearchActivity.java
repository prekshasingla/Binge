package com.example.prakharagarwal.binge.MainScreen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

  private List<String> mFood;
  private List<String> mRestaurants;
  private ListView cuisineListView;
  private SearchResultAdapter cuisineResultAdapter;
  private ListView restaurantListView;
  private SearchResultAdapter restaurantResultAdapter;
  private ArrayList<String> cuisineList;
  private ArrayList<String> restaurantList;
  private EditText searchField;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    cuisineList = new ArrayList<>();
    restaurantList = new ArrayList<>();

    cuisineListView = (ListView) findViewById(R.id.cuisinesList);
    restaurantListView = (ListView) findViewById(R.id.restaurantsList);
    cuisineResultAdapter = new SearchResultAdapter(this, cuisineList);
    restaurantResultAdapter = new SearchResultAdapter(this, restaurantList);

    searchField = (EditText) findViewById(R.id.search_edit_text);
    searchField.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        String searchTerm = searchField.getText().toString();
        setCuisineList(searchTerm);
        setRestaurantList(searchTerm);
      }
    });


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("mainscreen");
    mFood = new ArrayList<>();
    ref.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        getCuisineData(dataSnapshot);
        setCuisineList(null);
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });

    DatabaseReference ref1 = database.getReference("table");
    mRestaurants = new ArrayList<>();
    ref1.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(DataSnapshot dataSnapshot) {
        getRestaurantData(dataSnapshot);
        setRestaurantList("");
      }

      @Override
      public void onCancelled(DatabaseError databaseError) {
      }
    });
  }

  private void setCuisineList(String searchTerm) {
    int i = 0;
    int j = 9;
    while (j < 10) {
      if(searchTerm==null)
      {
        cuisineList.add(mFood.get(i));
        j++;
      }else
      if (mFood.get(i).toLowerCase().contains(searchTerm.toLowerCase())) {
        cuisineList.add(mFood.get(i));
        j++;
      }
      i++;
      if(i==mFood.size())
        return;
    }
    cuisineResultAdapter.notifyDataSetChanged();
    cuisineListView.setAdapter(cuisineResultAdapter);
    setListViewHeightBasedOnChildren(cuisineListView, cuisineList.size());
  }

  private void setRestaurantList(String searchTerm) {
    for (int i = 0; i < 10; i++) {
      restaurantList.add(mRestaurants.get(i));
    }
    restaurantResultAdapter.notifyDataSetChanged();
    restaurantListView.setAdapter(restaurantResultAdapter);
    setListViewHeightBasedOnChildren(restaurantListView, restaurantList.size());
  }

  public void setListViewHeightBasedOnChildren(ListView listView, int size) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null)
      return;

    int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
    int totalHeight = 0;
    View view = null;
    for (int i = 0; i < size; i++) {
      view = listAdapter.getView(i, view, listView);
      if (i == 0)
        view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

      DisplayMetrics metrics = new DisplayMetrics();
      getWindowManager().getDefaultDisplay().getMetrics(metrics);

      int px = metrics.widthPixels;
      view.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
      totalHeight += view.getMeasuredHeight();
    }
    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + listView.getPaddingTop() + listView.getPaddingBottom() + 15;
    listView.setLayoutParams(params);
    listView.setScrollContainer(false);
  }

  private void getRestaurantData(DataSnapshot dataSnapshot) {
    for (DataSnapshot child : dataSnapshot.getChildren()) {
      for (DataSnapshot child1 : child.getChildren()) {
        String restaurant = null;
        if (child1.getKey().equals("hname")) {
          restaurant = (String) child1.getValue();
          mRestaurants.add(restaurant);
        }
      }
    }
  }

  public void getCuisineData(DataSnapshot dataSnapshot) {
//    progressBar.setVisibility(View.GONE);
    for (DataSnapshot child : dataSnapshot.getChildren()) {
//      if (child.getKey().equals(getArguments().getString("id"))) {
      for (DataSnapshot child1 : child.getChildren()) {
        for (DataSnapshot child2 : child1.getChildren()) {
          String cuisine = null;
          if (child2.getKey().equals("dish_id")) {
            cuisine = (String) child2.getValue();
            mFood.add(cuisine);
          }
        }
      }
//      }
    }
  }
}
