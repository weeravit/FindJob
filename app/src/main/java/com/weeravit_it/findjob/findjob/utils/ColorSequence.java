package com.weeravit_it.findjob.findjob.utils;

import android.content.Context;

import com.weeravit_it.findjob.findjob.R;

/**
 * Created by Weeravit on 22/10/2558.
 */
public class ColorSequence {

    private Context context;
    private int sequence;
    private int[] colorRes;

    public ColorSequence(Context context) {
        this.context = context;
        this.sequence = 0;
        this.colorRes = initDefaultColors();
    }

    private int[] initDefaultColors() {
        int[] colorRes = {
          R.color.deep_purple_50,
                R.color.indigo_50,
                R.color.teal_50,
                R.color.lime_50
        };
        return colorRes;
    }

    public int getColor() {
        int color = 0;

        try {
            color = colorRes[sequence];
            sequence += 1;
        } catch (Exception e) {
            sequence = 0;
            color = colorRes[sequence];
        }

        return context.getResources().getColor(color);
    }

}
