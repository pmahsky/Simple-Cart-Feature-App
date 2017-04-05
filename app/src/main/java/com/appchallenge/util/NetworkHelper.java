package com.appchallenge.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prashant on 05/04/17.
 */

public class NetworkHelper {

    public static boolean isConnectedToInternet(Context context) {

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            return networkInfo.isConnected();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
