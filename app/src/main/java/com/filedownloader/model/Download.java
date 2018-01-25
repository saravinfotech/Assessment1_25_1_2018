package com.filedownloader.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model Class for holding the progress information
 * size of the file received
 * total file size.
 * <p>
 * This class also implements parcelable since it is passed from
 */

public class Download implements Parcelable {


    public static final Creator<Download> CREATOR = new Creator<Download>() {
        @Override
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        @Override
        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
    private int progress;
    private int currentFileSize;
    private int totalFileSize;

    public Download() {

    }

    protected Download(Parcel in) {
        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCurrentFileSize() {
        return currentFileSize;
    }

    public void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public int getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(progress);
        parcel.writeInt(currentFileSize);
        parcel.writeInt(totalFileSize);
    }
}
