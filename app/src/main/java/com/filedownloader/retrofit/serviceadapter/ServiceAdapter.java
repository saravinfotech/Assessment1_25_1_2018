package com.filedownloader.retrofit.serviceadapter;

import com.filedownloader.retrofit.api.APIServices;
import com.filedownloader.util.Constants;

import retrofit2.Retrofit;

/**
 * Class to return the retrofit object. When multiple API calls are defined there
 * would be no need to define the retrofit object repeatedly
 */

public class ServiceAdapter {

    public static APIServices getRetrofitBuilder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.FILE_DOWNLOAD_BASE_URL)
                .build();
        APIServices apiServices = retrofit.create(APIServices.class);
        return apiServices;
    }

}
