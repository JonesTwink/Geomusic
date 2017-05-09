package com.azhukovski.geomusic;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.RoundingMode;
import java.text.DecimalFormat;


public class User implements Parcelable, Comparable<User>{
    public String id;
    public String login;
    public String about;
    public String song;
    public String album;
    public String latitude;
    public String longitude;
    public User currentUser;

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

    public Double countDistanceToUser(User remoteUser){
        double distance = calculateDistanceBetweenTwoEarthPoints(Double.parseDouble(this.latitude),
                                                                 Double.parseDouble(this.longitude),
                                                                 Double.parseDouble(remoteUser.latitude),
                                                                 Double.parseDouble(remoteUser.longitude));
        return distance;
    }

    private double calculateDistanceBetweenTwoEarthPoints(double lat1, double lon1, double lat2, double lon2){
        final double RADIANS_IN_DEGREE = 0.0174533;
        final double EARTH_RADIUS = 6372795;

        lat1 = lat1 * RADIANS_IN_DEGREE;
        lon1 = lon1 * RADIANS_IN_DEGREE;
        lat2 = lat2 * RADIANS_IN_DEGREE;
        lon2 = lon2 * RADIANS_IN_DEGREE;

        double deltaLon = Math.abs(lon1 - lon2);

        double numerator = Math.pow((Math.cos(lat2)*Math.sin(deltaLon)), 2) + Math.pow(( Math.cos(lat1)*Math.sin(lat2) - Math.sin(lat1)*Math.cos(lat2)*Math.cos(deltaLon) ), 2);
        double denominator = Math.sin(lat1)*Math.sin(lat2) + Math.cos(lat1)*Math.cos(lat2)*Math.cos(deltaLon);

        double distance = Math.atan2(Math.sqrt(numerator), denominator);
        return distance * EARTH_RADIUS;
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

    @Override
    public int compareTo(User user) {
        return countDistanceToUser(currentUser).compareTo(user.countDistanceToUser(user.currentUser));
    }

    @Override
    public String toString(){
        return this.login + " (" + getFormattedDistanceToCurrentUser()  + " м)";
    }

    public String getFormattedDistanceToCurrentUser(){
        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.CEILING);
        return  df.format(countDistanceToUser(currentUser));
    }
}
