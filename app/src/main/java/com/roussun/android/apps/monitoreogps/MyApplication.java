package com.roussun.android.apps.monitoreogps;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.roussun.android.apps.monitoreogps.model.entity.User;

/**
 * Created by christiansasig on 13/2/17.
 */

public class MyApplication extends MultiDexApplication {
    //Pruebas
    public static final String URL_MAIN = "http://192.168.4.252/~christiansasig";

    //Produccion
    //public static final String URL_MAIN = "http://191.97.64.8";
    public static final String URL_ENVOIREMENT = "/proyectos-web/monitoreogps/web/app_dev.php";
    public static final String URL_AUTHENTICATE_DATA =  URL_MAIN + URL_ENVOIREMENT + "/rest/user/authentication";
    public static final String URL_GET_POSITION_BY_DEVICE = URL_MAIN + URL_ENVOIREMENT + "/rest/position/positionByDevice";
    public static final String URL_GET_HISTORIAL_POSITIONS_BY_DEVICE = URL_MAIN + URL_ENVOIREMENT + "/rest/position/historialPositionByDevice";
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
