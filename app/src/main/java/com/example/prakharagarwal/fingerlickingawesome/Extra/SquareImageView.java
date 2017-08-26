package com.example.prakharagarwal.fingerlickingawesome.Extra;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by prekshasingla on 05/07/17.
 */
public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
        int height=width* 9/16;
        setMeasuredDimension(width, height);
    }
}

