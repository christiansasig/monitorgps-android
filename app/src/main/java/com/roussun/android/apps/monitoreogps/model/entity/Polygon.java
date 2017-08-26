package com.roussun.android.apps.monitoreogps.model.entity;

import java.io.Serializable;

/**
 * Created by christiansasig on 6/3/17.
 */

public class Polygon implements Serializable {
    private Integer id;
    private String name;
    private String path;

    public Polygon() {
    }

    public Polygon(Integer id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
