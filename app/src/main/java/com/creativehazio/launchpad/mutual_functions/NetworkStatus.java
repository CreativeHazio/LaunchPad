package com.creativehazio.launchpad.mutual_functions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

    private Context context;

    public NetworkStatus(Context context) {
        this.context = context;
    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }
}
