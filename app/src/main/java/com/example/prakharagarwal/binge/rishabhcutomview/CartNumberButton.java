package com.example.prakharagarwal.binge.rishabhcutomview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prakharagarwal.binge.R;


public class CartNumberButton  extends RelativeLayout {

    private Context context;
    private AttributeSet attrs;
    private int styleAttr;
    private CartNumberButton.OnClickListener mListener;
    private int initialNumber;
    private int lastNumber;
    private int currentNumber;
    private int finalNumber;
    private TextView textView;
    private Button addBtn, subtractBtn;
    private View view;
    private CartNumberButton.OnValueChangeListener mOnValueChangeListener;
    private LinearLayout mLayout;

    public CartNumberButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CartNumberButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        initView();
    }

    public CartNumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        this.attrs = attrs;
        this.styleAttr = defStyleAttr;
        initView();
    }

    private void initView() {
        this.view = this;
        inflate(context, R.layout.cartbuttonlayout, this);
        final Resources res = getResources();
        final int defaultColor = res.getColor(com.example.prakharagarwal.binge.R.color.colorPrimary);
        final int defaultTextColor = res.getColor(com.example.prakharagarwal.binge.R.color.colorText);
        final Drawable defaultDrawable = res.getDrawable(R.drawable.cartbuttonbackground);

        TypedArray a = context.obtainStyledAttributes(attrs, com.example.prakharagarwal.binge.R.styleable.CartNumberButton, styleAttr, 0);

        initialNumber = a.getInt(com.example.prakharagarwal.binge.R.styleable.CartNumberButton_initialNumber, 0);
        finalNumber = a.getInt(com.example.prakharagarwal.binge.R.styleable.CartNumberButton_finalNumber, Integer.MAX_VALUE);
        float textSize = a.getDimension(com.example.prakharagarwal.binge.R.styleable.CartNumberButton_textSize, 13);
        int color = a.getColor(com.example.prakharagarwal.binge.R.styleable.CartNumberButton_backGroundColor, defaultColor);
        int textColor = a.getColor(com.example.prakharagarwal.binge.R.styleable.CartNumberButton_textColor, defaultTextColor);
        Drawable drawable = a.getDrawable(com.example.prakharagarwal.binge.R.styleable.CartNumberButton_backgroundDrawable);

        subtractBtn = (Button) findViewById(com.example.prakharagarwal.binge.R.id.subtract_btn);
        addBtn = (Button) findViewById(com.example.prakharagarwal.binge.R.id.add_btn);
        textView = (TextView) findViewById(com.example.prakharagarwal.binge.R.id.number_counter);
        mLayout = (LinearLayout) findViewById(com.example.prakharagarwal.binge.R.id.layout);

        subtractBtn.setTextColor(textColor);
        addBtn.setTextColor(textColor);
        textView.setTextColor(textColor);
        subtractBtn.setTextSize(textSize);
        addBtn.setTextSize(textSize);
        textView.setTextSize(textSize);

        if (drawable == null) {
            drawable = defaultDrawable;
        }
        assert drawable != null;
        drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC));
        if (Build.VERSION.SDK_INT > 16)
            mLayout.setBackground(drawable);
        else
            mLayout.setBackgroundDrawable(drawable);

        textView.setText(String.valueOf(initialNumber));

        currentNumber = initialNumber;
        lastNumber = initialNumber;

        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString());
                setNumber(String.valueOf(num - 1), true);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString());
                setNumber(String.valueOf(num + 1), true);
            }
        });
        a.recycle();
    }

    public void disable(){
        subtractBtn.setOnClickListener(null);
        addBtn.setOnClickListener(null);
    }
    public void enable(){
        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString());
                setNumber(String.valueOf(num - 1), true);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.valueOf(textView.getText().toString());
                setNumber(String.valueOf(num + 1), true);
            }
        });
    }
    private void callListener(View view) {
        if (mListener != null) {
            mListener.onClick(view);
        }

        if (mOnValueChangeListener != null) {
            if (lastNumber != currentNumber) {
                mOnValueChangeListener.onValueChange(this, lastNumber, currentNumber);
            }
        }
    }

    public String getNumber() {
        return String.valueOf(currentNumber);
    }

    public void setNumber(String number) {
        lastNumber = currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if (this.currentNumber > finalNumber) {
            this.currentNumber = finalNumber;
        }
        if (this.currentNumber < initialNumber) {
            this.currentNumber = initialNumber;
        }
        textView.setText(String.valueOf(currentNumber));
    }

    public void setNumber(String number, boolean notifyListener) {
        setNumber(number);
        if (notifyListener) {
            callListener(this);
        }
    }

    public void setOnClickListener(CartNumberButton.OnClickListener onClickListener) {
        this.mListener = onClickListener;
    }

    public void setOnValueChangeListener(CartNumberButton.OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public interface OnClickListener {

        void onClick(View view);

    }

    public interface OnValueChangeListener {
        void onValueChange(CartNumberButton view, int oldValue, int newValue);
    }

    public void setRange(Integer startingNumber, Integer endingNumber) {
        this.initialNumber = startingNumber;
        this.finalNumber = endingNumber;
    }

    public void updateColors(int backgroundColor, int textColor) {
        this.textView.setBackgroundColor(backgroundColor);
        this.addBtn.setBackgroundColor(backgroundColor);
        this.subtractBtn.setBackgroundColor(backgroundColor);

        this.textView.setTextColor(textColor);
        this.addBtn.setTextColor(textColor);
        this.subtractBtn.setTextColor(textColor);
    }

    public void updateTextSize(int unit, float newSize) {
        this.textView.setTextSize(unit, newSize);
        this.addBtn.setTextSize(unit, newSize);
        this.subtractBtn.setTextSize(unit, newSize);
    }

    public void setTextColors(int minuscolor,int numbercolor,int pluscolor)
    {
        this.textView.setTextColor(numbercolor);
        this.addBtn.setTextColor(pluscolor);
        this.subtractBtn.setTextColor(minuscolor);
    }

    public void setbuttonBackgroundColor(int minuscolor,int numbercolor,int pluscolor)
    {
        this.textView.setBackgroundColor(numbercolor);
        this.addBtn.setBackgroundColor(pluscolor);
        this.subtractBtn.setBackgroundColor(minuscolor);

    }

    public void setViewVisibility(int visibility) throws IllegalArgumentException
    {
        if(visibility==View.VISIBLE || visibility==View.INVISIBLE || visibility==View.GONE)
        {
            this.textView.setVisibility(visibility);
            this.addBtn.setVisibility(visibility);
            this.subtractBtn.setVisibility(visibility);
            this.mLayout.setVisibility(visibility);
        }
        else
            throw new IllegalArgumentException("Check the argument");
    }
}
