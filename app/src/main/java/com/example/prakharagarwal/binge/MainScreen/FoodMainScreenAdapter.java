package com.example.prakharagarwal.binge.MainScreen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.prakharagarwal.binge.Menu.Menu;
import com.example.prakharagarwal.binge.R;
import com.example.prakharagarwal.binge.model_class.PassingData;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodMainScreenAdapter extends RecyclerView.Adapter<FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder> {

    private List<Menu> mFood;
    final private Activity mContext;

    FoodMainScreenAdapterViewHolder holder;
    RecyclerView recyclerView;
    private boolean headerEnabled;

    public FoodMainScreenAdapter(List<Menu> mFood, Activity mContext, RecyclerView recyclerView) {
        this.mFood = mFood;
        this.mContext = mContext;
        this.recyclerView = recyclerView;

    }



    @Override
    public FoodMainScreenAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_main_screen, parent, false);
        holder = new FoodMainScreenAdapter.FoodMainScreenAdapterViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(FoodMainScreenAdapterViewHolder holder, int position) {
        holder.dish_name_rest.setText(mFood.get(position).getName());
      //  holder.dish_price_rest.setText("₹ "+mFood.get(position).getPrice());
      //  holder.dish_time_rest.setText("10 min");
        holder.resturant_name_rest.setText(mFood.get(position).getRestaurantName());

        Picasso.with(mContext)
                .load(mFood.get(position).getPoster_url())
                .into(holder.dish_image_rest);
    }



    @Override
    public int getItemCount() {

        return mFood.size();
    }


    public class FoodMainScreenAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView dish_name_rest;
        public TextView resturant_name_rest;
   //     public TextView dish_price_rest;
  //      public TextView dish_time_rest;
        public ImageView dish_image_rest;

        //public FrameLayout videoContainer;
        public  ImageView imageview_food_mainScreen;
        public CardView cardView;
        public FoodMainScreenAdapterViewHolder(View itemView) {
            super(itemView);
            //image = (SimpleDraweeView) itemView.findViewById(R.id.image);
            dish_name_rest = (TextView) itemView.findViewById(R.id.dish_name_rest);
            resturant_name_rest = (TextView) itemView.findViewById(R.id.resturant_name_rest);
           // videoContainer = (FrameLayout) itemView.findViewById(R.id.video_container);
            dish_image_rest=itemView.findViewById(R.id.dish_image_rest);
     //       dish_price_rest=itemView.findViewById(R.id.dish_price_rest);
     //       dish_time_rest=itemView.findViewById(R.id.dish_time_rest);

            cardView=itemView.findViewById(R.id.cardview_dish_click);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(mContext,DishInfoActivity.class);
            PassingData.setMenu(mFood.get(getAdapterPosition()));
            intent.putExtra("rest",mFood.get(getAdapterPosition()).getRestaurant_id());
            intent.putExtra("dish",mFood.get(getAdapterPosition()).getName());
            intent.putExtra("flag", "preOrder");
            Log.d("RISHABH","FOOD MAIN SCREEN ADAPTER "+mFood.get(getAdapterPosition()).getRestaurant_id());
            PassingData.setResturant_Id(mFood.get(getAdapterPosition()).getRestaurant_id());
            mContext.startActivity(intent);
        }
    }


    public void addAll(List<Menu> food) {
        mFood = food;
    }
}
