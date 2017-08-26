package com.roussun.android.apps.monitoreogps.model.entity;

import java.io.Serializable;

/**
 * Created by christiansasig on 6/3/17.
 */

public class Device implements Serializable{
    private Integer id;
    private String name;
    private String ip;
    private String mac;
    private String description;
    private Polygon polygon;

    public Device() {
    }

    public Device(Integer id, String name, String ip, String mac, String description, Polygon polygon) {
        this.id = id;
        this.name = name;
        this.ip = ip;
        this.mac = mac;
        this.description = description;
        this.polygon = polygon;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
