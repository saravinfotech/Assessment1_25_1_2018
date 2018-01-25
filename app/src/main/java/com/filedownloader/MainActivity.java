package com.filedownloader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.filedownloader.model.Download;
import com.filedownloader.util.CommonUtils;
import com.filedownloader.util.Constants;
import com.filedownloader.util.DownloadService;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.filedownloader.util.Constants.MESSAGE_PROGRESS;
import static com.filedownloader.util.Constants.PERMISSION_REQUEST_CODE;

/**
 * Class with autosuggestion input box, just type h and you would get auto suggestions on download
 * urls. This Activity uses butterknife for UI binding & has local notification manager.
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.downloadProgressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.progress_text)
    TextView mProgressText;
    @BindView(R.id.downloadURL)
    AutoCompleteTextView mURLText;
    LocalBroadcastManager bManager;
    /**
     * Receiver code to pass on the progress information to the UI
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MESSAGE_PROGRESS)) {
                Download download = intent.getParcelableExtra(Constants.PARCELABLE_KEYWORD);
                mProgressBar.setProgress(download.getProgress());
                mProgressText.setVisibility(View.VISIBLE);
                if (download.getProgress() == 100) {
                    mProgressText.setText(R.string.download_complete);
                } else {
                    mProgressText.setText(String.format(Constants.DOWNLOAD_PROGRESS_MESSAGE
                            , download.getCurrentFileSize(), download.getTotalFileSize()));
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //Adapter for populating the autosuggestions
        ArrayAdapter adapter = new
                ArrayAdapter(this, android.R.layout.simple_list_item_1, getResources()
                .getStringArray(R.array.auto_suggestion_url));

        mURLText.setAdapter(adapter);
    }

    /**
     * Checks for available permissions & downloads file based on that
     */
    @OnClick(R.id.downloadBtn)
    public void downloadFile() {
        if (!TextUtils.isEmpty(mURLText.getText())) {
            String downloadURL = mURLText.getText().toString();
            CommonUtils objCommonUtils = new CommonUtils();
            try {
                URL url = new URL(downloadURL);
                Constants.FILE_DOWNLOAD_BASE_URL = objCommonUtils.getBaseURL(downloadURL);
                Constants.END_POINT_URL = objCommonUtils.getFileName(downloadURL);
                if (checkPermission()) {
                    startDownload();
                } else {
                    requestPermission();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.invalid_url_error_message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Usage of local notification manager to display the progress on the screen
     */
    private void registerPorgressNotificationReceiver() {
        bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * Method in which the Intent Service gets started and the application
     * registers for the notification receiver
     */
    private void startDownload() {
        registerPorgressNotificationReceiver();
        Intent downloadIntent = new Intent(this, DownloadService.class);
        startService(downloadIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Runtime permissions
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                } else {
                    Toast.makeText(this, R.string.permission_denied_message, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
