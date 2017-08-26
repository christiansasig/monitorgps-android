package com.roussun.android.apps.monitoreogps.model.entity;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Clase Usuario
 *
 * @author Christian Sasig christiansasig@gmail.com
 * @version 1.0, 20/10/2014
 *
 * Tu tiempo es limitado, así que no lo malgastes viviendo la vida de otro.
 * No te dejes atrapar por el dogma que implica vivir según los resultados del pensamiento de otros.
 * No dejes que el ruido de las opiniones de los demás ahogue tu propia voz interior.
 * Y lo que es más importante, ten el coraje de seguir a tu corazón y tu intuición.
 * De algún modo él ya sabe lo que realmente quieres llegar a ser. Todo lo demás es secundario. Steve Jobs
 *
 */
public class User implements Serializable {

    private Integer id;
    private String name;
    private String username;
    private String password;
    private ArrayList<Device> devices;

    public User() {

    }

    public User(Integer id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }
}