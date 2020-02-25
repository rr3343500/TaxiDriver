package com.iwish.taxidriver.extended;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.iwish.taxidriver.R;


public class TexiFonts extends TextView {
    public TexiFonts(Context context) {
        super(context);
        changeFont(null);


    }

    public TexiFonts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeFont(attrs);
    }

    public TexiFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeFont(attrs);
    }







    public void changeFont(AttributeSet attributeSet){

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/Cabin-Regular-TTF.ttf");
        if (attributeSet != null) {
            TypedArray aa = getContext().obtainStyledAttributes(attributeSet, R.styleable.ProximaFonts);
            String fontvalue = aa.getString(R.styleable.ProximaFonts_fonttype);
            try {
                if (fontvalue != null) {
                    switch (fontvalue) {
                        case "1":
                            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/arial_10.ttf");
                            break;
                        case "2":
                            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ARIALBD_A.ttf");
                            break;
                        case "3":
                            typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/TCB_7.ttf");
                            break;
                        case "4":
                            typeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/TCM_8.ttf");
                            break;


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setTypeface(typeface);

            aa.recycle();


        }
        }
    }

