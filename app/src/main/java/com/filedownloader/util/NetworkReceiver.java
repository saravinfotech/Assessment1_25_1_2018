package com.filedownloader.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.filedownloader.BaseActivity;

/**
 * Receiver to listen for Network change notifications
 */

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNetworkAvailable;
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null
                && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            isNetworkAvailable = true;
        } else if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            isNetworkAvailable = true;
        } else
            isNetworkAvailable = networkInfo != null;
        ((BaseActivity) context).onNetworkChange(isNetworkAvailable);
    }
}