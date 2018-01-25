package com.filedownloader.retrofit.servicegateway;

import android.app.IntentService;
import android.app.NotificationManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.filedownloader.retrofit.serviceadapter.ServiceAdapter;
import com.filedownloader.util.Constants;
import com.filedownloader.util.LoadDownloadParameters;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Service Gateway for file download operation
 */

public class DownloadServiceGateway {

    Call<ResponseBody> request;

    public DownloadServiceGateway() {
        request = ServiceAdapter.getRetrofitBuilder().downloadFile(Constants.END_POINT_URL);
    }

    /**
     * Sychronour API call for download file
     *
     * @param intentService
     * @param notificationBuilder
     * @param notificationManager
     */
    public void downloadFile(final IntentService intentService, final NotificationCompat.Builder
            notificationBuilder, final NotificationManager notificationManager) {
        final LoadDownloadParameters objLoadDownloadParameters = new LoadDownloadParameters();
        try {
            objLoadDownloadParameters.loadFileDownloadParameters(request.execute().body(),
                    intentService, notificationBuilder, notificationManager);
        } catch (IOException e) {
            Log.e(Constants.RETROFIT_ERROR, e.getLocalizedMessage());
        }
    }

    public void cancelDownload() {
        if (!request.isCanceled()) {
            request.cancel();
        }
    }
}
