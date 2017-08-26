package com.roussun.android.apps.monitoreogps.model.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by christiansasig on 6/3/17.
 */

public class Position implements Serializable {
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String created_at;

    public Position() {
    }

    public Position(Integer id, Double latitude, Double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getCreatedAt() {
        String sDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date createDate = formatter.parse(created_at);
            sDate = dateFormat.format(createDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sDate;
    }

    public void setCreatedAt(String created_at) {
        this.created_at = created_at;
    }
}
