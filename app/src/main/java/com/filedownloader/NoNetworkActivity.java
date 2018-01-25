package com.filedownloader;

import android.os.Bundle;

/**
 * UI for no notification
 */

public class NoNetworkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network_);

    }

    public void onNetworkChange(boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            finish();
        }
    }

}
