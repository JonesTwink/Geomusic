package com.azhukovski.geomusic;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 07.05.2017.
 */

public class User implements Parcelable {
    public String id;
    public String login;
    public String about;
    public String song;
    public String album;
    public String latitude;
    public String longitude;

    @Override
    public String toString(){
        return this.login;
    }
    public User(){
    }
    public User(String id, String login, String about, String song,String album, String latitude, String longitude){
        this.id = id;
        this.login = login;
        this.about = about;
        this.song = song;
        this.album = album;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public User(Parcel in){
        String[] data = new String[7];

        in.readStringArray(data);
        // the order needs to be the same as in writeToParcel() method
        this.id = data[0];
        this.login = data[1];
        this.about = data[2];
        this.song = data[3];
        this.album = data[4];
        this.latitude = data[5];
        this.longitude = data[6];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.id,
                this.login,
                this.about,
                this.song,
                this.album,
                this.latitude,
                this.longitude});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
