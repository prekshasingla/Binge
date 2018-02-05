package com.example.prakharagarwal.binge.MainScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Food_MainScreen> mFood;
    private List<Restaurant> mRestaurants;

    private ListView cuisineListView;
    private SearchResultAdapter cuisineResultAdapter;
    private ListView restaurantListView;
    private SearchResultAdapter restaurantResultAdapter;
    private List<RestCuisineListItem> cuisineList;
    private List<RestCuisineListItem> restaurantList;
    private EditText searchField;

    private TextView cuisinesHeader;
    private TextView restrHeader;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        cuisineList=new ArrayList<>();
        restaurantList=new ArrayList<>();
        cuisinesHeader = (TextView) findViewById(R.id.cuisinesHeader);
        restrHeader = (TextView) findViewById(R.id.restaurantsHeader);
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
                if (mFood.size() > 0)
                    setCuisineList(searchTerm.trim());
                if (mRestaurants.size() > 0)
                    setRestaurantList(searchTerm.trim());
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
                setRestaurantList(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        cuisineListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, RestaurantDetailsActivity.class);
                intent.putExtra("restaurantID", cuisineList.get(position).getRestaurantID());
                intent.putExtra("restaurantName", cuisineList.get(position).getRestaurantName());
                intent.putExtra("dishName", cuisineList.get(position).getDisplayName());
                // Log.i("TAG", mFood.get(getAdapterPosition()).getDishName());
                intent.putExtra("posi", 1);
                startActivity(intent);
                finish();
            }
        });

        restaurantListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, RestaurantActivity.class);
                intent.putExtra("restaurantID", restaurantList.get(position).getRestaurantID());
                intent.putExtra("restaurantName", restaurantList.get(position).getRestaurantName());
                intent.putExtra("posi", 1);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setCuisineList(String searchTerm) {
        int i = 0;
        int j = 0;
        if(cuisineList!=null)
        cuisineList.clear();
        cuisineResultAdapter.notifyDataSetChanged();
        while (j < 10) {
            if (searchTerm == null) {
                RestCuisineListItem cuisine=new RestCuisineListItem();
                cuisine.setDisplayName(mFood.get(i).getDish_id());
                cuisine.setRestaurantID(mFood.get(i).getRestaurant_id());
                cuisine.setRestaurantName(mFood.get(i).getRestaurant_name());
                cuisineList.add(cuisine);
                j++;
            } else if (mFood.get(i).getDish_id().toLowerCase().contains(searchTerm.toLowerCase())) {
                RestCuisineListItem cuisine=new RestCuisineListItem();
                cuisine.setDisplayName(mFood.get(i).getDish_id());
                cuisine.setRestaurantID(mFood.get(i).getRestaurant_id());
                cuisine.setRestaurantName(mFood.get(i).getRestaurant_name());
                cuisineList.add(cuisine);
                j++;
            }
            i++;
            if (i == mFood.size())
                break;
        }
        if (cuisineList.size() == 0) {
            cuisineListView.setVisibility(View.GONE);
            cuisinesHeader.setVisibility(View.GONE);
        } else {
            cuisineListView.setVisibility(View.VISIBLE);
            cuisinesHeader.setVisibility(View.VISIBLE);
            cuisineResultAdapter.notifyDataSetChanged();
            cuisineListView.setAdapter(cuisineResultAdapter);
            setListViewHeightBasedOnChildren(cuisineListView, cuisineList.size());
        }
    }

    private void setRestaurantList(String searchTerm) {
        int i = 0;
        int j = 0;
        if(restaurantList!=null)
        restaurantList.clear();
        restaurantResultAdapter.notifyDataSetChanged();
        while (j < 10) {
            if (searchTerm == null) {
                RestCuisineListItem rest=new RestCuisineListItem();
                rest.setDisplayName(mRestaurants.get(i).getRestaurantName());
                rest.setRestaurantID(mRestaurants.get(i).getRestaurantID());
                rest.setRestaurantName(mRestaurants.get(i).getRestaurantName());
                restaurantList.add(rest);
                j++;
            } else
                if ((mRestaurants.get(i).getRestaurantName()).toLowerCase().contains(searchTerm.toLowerCase())) {
                    RestCuisineListItem rest=new RestCuisineListItem();
                    rest.setDisplayName(mRestaurants.get(i).getRestaurantName());
                    rest.setRestaurantID(mRestaurants.get(i).getRestaurantID());
                    rest.setRestaurantName(mRestaurants.get(i).getRestaurantName());
                    restaurantList.add(rest);
                    j++;
            }
            i++;
            if (i == mRestaurants.size())
                break;
        }
        if (restaurantList.size() == 0) {
            restaurantListView.setVisibility(View.GONE);
            restrHeader.setVisibility(View.GONE);
        } else {
            restaurantListView.setVisibility(View.VISIBLE);
            restrHeader.setVisibility(View.VISIBLE);
            restaurantResultAdapter.notifyDataSetChanged();
            restaurantListView.setAdapter(restaurantResultAdapter);
            setListViewHeightBasedOnChildren(restaurantListView, restaurantList.size());
        }
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
            Restaurant restaurant = new Restaurant();

            for (DataSnapshot child1 : child.getChildren()) {

                String id = null;
                String name = null;
                if (child1.getKey().equals("hid")) {
                    restaurant.setRestaurantID("" + child1.getValue());
                }
                if (child1.getKey().equals("hname")) {
                    restaurant.setRestaurantName("" + child1.getValue());

                }

            }
            mRestaurants.add(restaurant);

        }
    }

    public void getCuisineData(DataSnapshot dataSnapshot) {
//    progressBar.setVisibility(View.GONE);
        for (DataSnapshot child : dataSnapshot.getChildren()) {
//      if (child.getKey().equals(getArguments().getString("id"))) {
            for (DataSnapshot child1 : child.getChildren()) {
                Food_MainScreen food = child1.getValue(Food_MainScreen.class);
                mFood.add(food);

            }
//      }
        }
    }

    class Restaurant {
        String restaurantID;
        String restaurantName;


        public String getRestaurantID() {
            return restaurantID;
        }

        public void setRestaurantID(String restaurantID) {
            this.restaurantID = restaurantID;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }
    }
    class RestCuisineListItem{
        Integer position;
        String displayName;
        String restaurantID;
        String restaurantName;
        String dishName;

        public Integer getPosition() {
            return position;
        }

        public void setPosition(Integer position) {
            this.position = position;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getRestaurantID() {
            return restaurantID;
        }

        public void setRestaurantID(String restaurantID) {
            this.restaurantID = restaurantID;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public String getDishName() {
            return dishName;
        }

        public void setDishName(String dishName) {
            this.dishName = dishName;
        }
    }
}