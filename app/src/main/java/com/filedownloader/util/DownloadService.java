package com.filedownloader.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.filedownloader.R;
import com.filedownloader.retrofit.servicegateway.DownloadServiceGateway;

/**
 * Custom Intent Service class that does all the heavy work of downloading the file
 */

public class DownloadService extends IntentService {

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public DownloadService() {
        super(Constants.CUSTOM_SERVICE_NAME);
    }

    public DownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        setupNotificationManager();
        DownloadServiceGateway objDownloadServiceGateway = new DownloadServiceGateway();
        objDownloadServiceGateway.downloadFile(DownloadService.this, notificationBuilder,
                notificationManager);
    }

    private void setupNotificationManager() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Constants.DOWNLOAD_NOTIFICATION_TITLE)
                .setContentText(Constants.DOWNLOAD_TEXT_VIEW_MESSAGE)
                .setAutoCancel(true);
        notificationManager.notify(Constants.NOTIFICATOIN_STRING_TAG, 25, notificationBuilder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DownloadServiceGateway objDownloadServiceGateway = new DownloadServiceGateway();
        objDownloadServiceGateway.cancelDownload();
        if (notificationManager != null) {
            notificationManager.cancel(Constants.NOTIFICATOIN_STRING_TAG, 25);
            notificationManager = null;
        }


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(Constants.NOTIFICATOIN_STRING_TAG, 25);
    }

}
