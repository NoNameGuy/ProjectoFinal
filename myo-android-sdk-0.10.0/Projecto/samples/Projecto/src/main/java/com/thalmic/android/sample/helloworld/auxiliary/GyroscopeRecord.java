package com.thalmic.android.sample.helloworld.auxiliary;

/**
 * Created by UTIL on 15/07/2016.
 */
public class GyroscopeRecord {

    long timestamp;
    double x;
    double y;
    double z;

    public GyroscopeRecord(long timestamp, double x, double y, double z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String toString() {
        return ""+timestamp+"; "+x+";"+y+";"+z;
    }

}

