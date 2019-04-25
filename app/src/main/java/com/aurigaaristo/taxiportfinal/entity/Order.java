package com.aurigaaristo.taxiportfinal.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private String id;
    private String date;
    private String name;
    private String destination;
    private String city;
    private String district;
    private String urban;
    private String passenger;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUrban() {
        return urban;
    }

    public void setUrban(String urban) {
        this.urban = urban;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.date);
        dest.writeString(this.name);
        dest.writeString(this.destination);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.urban);
        dest.writeString(this.passenger);
    }

    public Order() {
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        this.date = in.readString();
        this.name = in.readString();
        this.destination = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.urban = in.readString();
        this.passenger = in.readString();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}
