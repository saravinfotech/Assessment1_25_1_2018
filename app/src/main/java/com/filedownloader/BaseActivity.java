package com.filedownloader;

import android.Manifest;
import android.app.Dialog;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.filedownloader.retrofit.serviceadapter.ServiceAdapter;
import com.filedownloader.retrofit.servicegateway.DownloadServiceGateway;
import com.filedownloader.util.NetworkReceiver;

import static com.filedownloader.util.Constants.PERMISSION_REQUEST_CODE;

/**
 * Base Activity defining the common methods required across the application
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog networkDialog = null;
    private ServiceAdapter objServiceAdapter;
    private NetworkReceiver networkReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        objServiceAdapter = new ServiceAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerNetworkListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterNetworkListener();
    }

    /**
     * Register the Listener to for Network Change events
     */
    private void registerNetworkListener() {
        // Registers BroadcastReceiver to track network connection changes.
        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkReceiver();
        this.registerReceiver(networkReceiver, filter);
    }


    /**
     * Un-Register the Network listener
     */
    private void unRegisterNetworkListener() {
        if (networkReceiver != null) {
            this.unregisterReceiver(networkReceiver);
        }
    }

    /**
     * Method for checking the runtime permission availability information
     *
     * @return
     */
    protected boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Requesting permissions to the user
     */
    protected void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    public void onNetworkChange(boolean isNetworkAvailable) {
        if (!isNetworkAvailable) {
            stopNetworkCall();
            if (!(this instanceof NoNetworkActivity)) {
                showNetworkDialog();
            }
        } else {
            closeNetworkDialog();
        }
    }

    private void showNetworkDialog() {
        if (networkDialog == null) {
            networkDialog = new Dialog(this);
            networkDialog.setContentView(R.layout.network_dialog);
            networkDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            networkDialog.setCancelable(false);
            networkDialog.setTitle(R.string.network_dialog_title);
            networkDialog.show();
        }
    }

    protected void stopNetworkCall() {
        DownloadServiceGateway objDownloadServiceGateway = new DownloadServiceGateway();
        if (objServiceAdapter != null) {
            objDownloadServiceGateway.cancelDownload();
        }
    }

    private void closeNetworkDialog() {
        if (networkDialog != null) {
            networkDialog.dismiss();
            networkDialog = null;
        }
    }
}
