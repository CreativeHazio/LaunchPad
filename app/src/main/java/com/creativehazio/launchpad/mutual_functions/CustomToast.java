package com.creativehazio.launchpad.mutual_functions;

import android.content.Context;
import android.widget.Toast;

import com.creativehazio.launchpad.R;

public class CustomToast {

    public static void showShortToast(Context context, String message) {
        Toast toast = Toast.makeText(context, "\t\t"+message+"\t\t", Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(context.getResources().getColor(R.color.desert_sunset));
        toast.show();
    }

    public static void showLongToast(Context context, String message) {
        Toast toast = Toast.makeText(context, "\t\t"+message+"\t\t", Toast.LENGTH_LONG);
        toast.getView().setBackgroundColor(context.getResources().getColor(R.color.desert_sunset));
        toast.show();
    }
}
