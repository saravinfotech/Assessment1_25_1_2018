package com.filedownloader.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;

import com.filedownloader.model.Download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

/**
 * Class to download the file and load the Download Model Class as well
 */

public class LoadDownloadParameters {

    private int totalFileSize;

    /**
     * Method to read the file and display the notifications accordingly
     *
     * @param body                Response body received from Retrofit class
     * @param intentService       Instance of intent service to send notifications
     * @param notificationBuilder Instance of local notification build
     * @param notificationManager Instance of local notification Manager to manage notifications
     * @throws IOException
     */
    public void loadFileDownloadParameters(ResponseBody body, IntentService intentService,
                                           NotificationCompat.Builder notificationBuilder,
                                           NotificationManager notificationManager) throws IOException {
        if (body != null && body.contentLength() > 0) {
            int count;
            byte data[] = new byte[1024 * 4];
            long fileSize = body.contentLength();
            InputStream bis = new BufferedInputStream(body.byteStream(), 1024 * 8);
            File outputFile = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS), Constants.END_POINT_URL);
            OutputStream output = new FileOutputStream(outputFile);
            long total = 0;
            long startTime = System.currentTimeMillis();
            int timeCount = 1;
            while ((count = bis.read(data)) != -1) {
                total += count;
                totalFileSize = (int) (fileSize / (Math.pow(1024, 2)));
                double current = Math.round(total / (Math.pow(1024, 2)));
                int progress = (int) ((total * 100) / fileSize);
                long currentTime = System.currentTimeMillis() - startTime;
                Download download = new Download();
                download.setTotalFileSize(totalFileSize);
                if (currentTime > 1000 * timeCount) {
                    download.setCurrentFileSize((int) current);
                    download.setProgress(progress);
                    sendNotification(download, intentService, notificationBuilder, notificationManager);
                    timeCount++;
                }
                output.write(data, 0, count);
            }
            onDownloadComplete(notificationBuilder, notificationManager, intentService);
            output.flush();
            output.close();
            bis.close();
        }
    }


    /**
     * Sending notification
     * @param download
     * @param intentService
     * @param notificationBuilder
     * @param notificationManager
     */
    private void sendNotification(Download download, IntentService intentService, NotificationCompat
            .Builder notificationBuilder, NotificationManager notificationManager) {
        sendIntent(download, intentService);
        notificationBuilder.setProgress(100, download.getProgress(), false);
        notificationBuilder.setContentText(Constants.DOWNLOAD_TEXT_VIEW_MESSAGE + download.
                getCurrentFileSize() + Constants.DELIMITER + totalFileSize +
                Constants.DOWNLOAD_DISPLAY_UNIT);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendIntent(Download download, IntentService intentService) {
        Intent intent = new Intent(Constants.MESSAGE_PROGRESS);
        intent.putExtra(Constants.PARCELABLE_KEYWORD, download);
        LocalBroadcastManager.getInstance(intentService).sendBroadcast(intent);
    }

    private void onDownloadComplete(NotificationCompat.Builder notificationBuilder,
                                    NotificationManager notificationManager,
                                    IntentService intentService) {
        Download download = new Download();
        download.setProgress(100);
        sendIntent(download, intentService);
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0, 0, false);
        notificationBuilder.setContentText(Constants.DOWNLOAD_TEXT_VIEW_MESSAGE);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
