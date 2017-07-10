package com.aboelyan.sha.mshaweerak.RecyclerViewClients;

/**
 * Created by Administrator on 08/07/2017.
 */

public class ClientsModel {

    String NAME;
    String PHONE;
    String MODEL_CAR;
    String latitude,longitude,distance;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    /* public ClientsModel(String NAME, String PHONE, String MODEL_CAR, double latitude, double longitude, double distance) {

            this.NAME = NAME;
            this.PHONE = PHONE;
            this.MODEL_CAR = MODEL_CAR;
            this.latitude = latitude;
            this.longitude = longitude;
            this.distance = distance;
        }
    */
    public String getMODEL_CAR() {
        return MODEL_CAR;
    }

    public void setMODEL_CAR(String MODEL_CAR) {
        this.MODEL_CAR = MODEL_CAR;
    }


    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
    }
}
