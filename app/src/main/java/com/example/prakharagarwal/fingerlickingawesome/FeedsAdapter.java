package com.example.prakharagarwal.fingerlickingawesome;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by prakharagarwal on 25/06/17.
 */
public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.FeedsAdapterViewHolder> {

    private Cursor mCursor;
    final private Context mContext;
    FeedsAdapterViewHolder holder;


    public FeedsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public interface Callback {
        void onItemSelected(Cursor cursor, int position, FeedsAdapterViewHolder holder);
    }

    @Override
    public FeedsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent instanceof RecyclerView) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_adapter_card_item, parent, false);
            view.setFocusable(true);
            holder = new FeedsAdapterViewHolder(view);
            return holder;
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(FeedsAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
       


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public class FeedsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //public final ImageView I1;
        //public final TextView t2;

        public FeedsAdapterViewHolder(View view) {
            super(view);
           // I1 = (ImageView) view.findViewById(R.id.articleThumbnail);
            //t2 = (TextView) view.findViewById(R.id.article_text);
            view.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            ((Callback) mContext).onItemSelected(mCursor, adapterPosition, holder);


        }
    }


}
