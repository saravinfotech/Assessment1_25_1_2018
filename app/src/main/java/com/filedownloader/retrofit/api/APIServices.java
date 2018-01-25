package com.filedownloader.retrofit.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Class that would contain all the API call details
 */

public interface APIServices {

    @GET
    @Streaming
    Call<ResponseBody> downloadFile(@Url String url);
}
