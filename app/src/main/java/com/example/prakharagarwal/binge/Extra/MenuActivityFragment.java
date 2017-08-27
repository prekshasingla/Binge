package com.example.prakharagarwal.binge.Extra;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.prakharagarwal.binge.R;

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

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuActivityFragment extends Fragment {

    MenuImageAdapter menuImageAdapter;
    ArrayList<Menu> menu;
    private String sortType = "sort_by=popularity.desc";
    private String sort = "popular";


    public MenuActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        menu = new ArrayList<Menu>();

        menuImageAdapter = new MenuImageAdapter(getActivity(), menu);
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        GridView gridView=(GridView)rootView.findViewById(R.id.menu_grid_view);
//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.menu_recycler_view);
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //new FetchMenuTask().execute();
        new FetchMenu().execute();

//        recyclerView.setAdapter(menuImageAdapter);
        gridView.setAdapter(menuImageAdapter);

        //return inflater.inflate(R.layout.fragment_menu, container, false);
        return rootView;
    }

    public class FetchMenuTask extends AsyncTask<Void, Void, ArrayList<Menu>> {

        ArrayList<Menu> menu;
        public FetchMenuTask(){

            menu=new ArrayList<Menu>();
            Menu menu1=new Menu("Smjo2-2-opw");
            menu.add(menu1);
            Menu menu2=new Menu("Smjo2-2-opw");
            menu.add(menu2);
            Menu menu3=new Menu("Smjo2-2-opw");
            menu.add(menu3);
            Menu menu4=new Menu("Smjo2-2-opw");
            menu.add(menu4);


        }
        @Override
        protected ArrayList<Menu> doInBackground(Void... params) {

            return menu;
        }

        @Override
        protected void onPostExecute(ArrayList<Menu> result) {
            if (result != null) {
                menuImageAdapter.clear();
                menuImageAdapter.addAll(result);
                menuImageAdapter.notifyDataSetChanged();
            }
        }
    }

    public class FetchMenu extends AsyncTask<Void, Void, ArrayList<Menu>> {
        private final String LOG_TAG = FetchMenu.class.getName();

        private ArrayList<Menu> getMenuDatafromJson(String menuJsonString)
                throws JSONException {
            final String OWM_RESULTS = "results";
            final String OWM_POSTER = "poster_path";

            JSONObject menuJson = new JSONObject(menuJsonString);
            JSONArray menuArray = menuJson.getJSONArray(OWM_RESULTS);
            ArrayList<Menu> menus=new ArrayList<Menu>();

            for (int i = 0; i < menuArray.length(); i++) {
                JSONObject movieObject = menuArray.getJSONObject(i);
                Menu menu = new Menu("http://image.tmdb.org/t/p/w300/" + movieObject.getString(OWM_POSTER));
                menus.add(menu);
            }
            return menus;
        }

        @Override
        protected ArrayList<Menu> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonString = null;

            try {

                URL url = new URL("http://api.themoviedb.org/3/movie/" + sort + "?" + sortType + "&api_key=95ac456c4c36f55632a02725a7519821");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMenuDatafromJson(moviesJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Menu> result) {
            if (result != null) {
                menuImageAdapter.clear();
                menuImageAdapter.addAll(result);
                menuImageAdapter.notifyDataSetChanged();
            }
        }
    }


}
