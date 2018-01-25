package com.filedownloader.util;

/**
 * Common Util class to be used across the application
 */

public class CommonUtils {

    /**
     * Method to get the base url from the input url for retrofit
     * @param url
     * @return
     */
    public String getBaseURL(String url){
        return url.substring(0,url.lastIndexOf("/")+1);
    }

    /**
     * Method to get the file name use for saving the file and for retrofit
     * @param url
     * @return
     */
    public String getFileName(String url){
        return url.substring(url.lastIndexOf('/')+1, url.length() );
    }
}
