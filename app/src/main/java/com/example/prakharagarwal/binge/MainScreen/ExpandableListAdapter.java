package com.example.prakharagarwal.binge.MainScreen;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.rishabhcutomview.CartNumberButton;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Menu>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Menu>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        //     final String childText = (String) getChild(groupPosition, childPosition);
        final Menu menu = (Menu) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_child, null);
        }
//
//        TextView txtListChild = (TextView) convertView
//                .findViewById(R.id.lblListItem);
//        txtListChild.setText(childText);

        ImageView veg_nonveg = convertView.findViewById(R.id.veg_nonveg);
        TextView dishnametext = convertView.findViewById(R.id.dish_name);
        TextView dish_price = convertView.findViewById(R.id.dish_price);
        TextView dish_discount=convertView.findViewById(R.id.dish_discount);
        CartNumberButton numberButton = convertView.findViewById(R.id.cart_number_btn);

        if (menu.getVeg() == 0) {
            veg_nonveg.setBackgroundResource(R.mipmap.veg);
        } else {
            veg_nonveg.setBackgroundResource(R.mipmap.nonveg);
        }
        dishnametext.setText(menu.getName());
        dish_price.setText("₹ " + menu.getPrice());
        if(menu.getDiscount()!=0){
            dish_discount.setVisibility(View.VISIBLE);
            dish_discount.setText(menu.getDiscount()+"%");
        }
        else {
            dish_discount.setVisibility(View.GONE);
        }

        if(menu.getDish_switch()==0){
            numberButton.updateColors(_context.getResources().getColor(R.color.grey), Color.WHITE);
            numberButton.disable();
        }else{
            numberButton.updateColors(_context.getResources().getColor(R.color.lime_green), Color.WHITE);
            numberButton.enable();
        }
        numberButton.setNumber(menu.getTotalcartItem() + "");

        numberButton.setOnValueChangeListener(new CartNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(CartNumberButton view, int oldValue, int newValue) {
                DishInfoActivity infoActivity = new DishInfoActivity();
                infoActivity.add_item_to_cart(menu, newValue);
            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
//        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}