package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;

import java.util.List;

/**
 * Created by prakhar.agarwal on 12/9/2017.
 */

public class SearchResultAdapter extends BaseAdapter {

  private final Context context;
  List<String> names;
  public SearchResultAdapter(Context context, List<String> names){
    this.names=names;
    this.context =context;
  }
  @Override
  public int getCount() {
    return names.size();
  }

  @Override
  public Object getItem(int position) {
    return names.get(position);
  }

  @Override
  public long getItemId(int position) {
    return names.get(position).hashCode();
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    SearchResultAdapter.ViewHolder holder;
    final String name =(String) getItem(position);

    if (convertView == null) {
      holder = new SearchResultAdapter.ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(context);
      convertView = inflater.inflate(R.layout.item_search_text, null);
      holder.searchHint = (TextView) convertView.findViewById(R.id.search_item_text);
      convertView.setTag(holder);
    } else {
      holder = (SearchResultAdapter.ViewHolder) convertView.getTag();
    }
    holder.searchHint.setText(name);
    return convertView;
  }

  class ViewHolder {
    TextView searchHint;
  }
}
