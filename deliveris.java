package com.finalproject3.amir.testgooglemaps;

/**
 * Created by amir on 5/17/2016.
 */
public class deliveris {
    private String place;
    private String order;
    private Double lat;
    private Double lng;
    boolean Checked ;


    public deliveris(String place, String order, Double lat, Double lng) {
        this.place = place;
        this.order = order;
        this.lat = lat;
        this.lng = lng;
        this.Checked=false;

    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getPlace() {

        return place;
    }

    public String getOrder() {
        return order;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public boolean isChecked() {
        return Checked;
    }

    public void setChecked(boolean Checked) {
        this.Checked = Checked;
    }

}
