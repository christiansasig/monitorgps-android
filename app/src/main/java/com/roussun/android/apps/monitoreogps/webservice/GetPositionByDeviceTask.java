package com.roussun.android.apps.monitoreogps.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roussun.android.apps.monitoreogps.model.entity.Device;
import com.roussun.android.apps.monitoreogps.model.entity.Position;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GetPositionByDeviceTask extends AsyncTask<String, Void, Position> {
    private  RestFulWebService restFulWebService;
    private Gson gson;
    private Context context;
    private Device device;
    private ConfirmCallback mConfirmCallback;
    private ProgressDialog progressDialog;

    public GetPositionByDeviceTask(Context context, Device device, ConfirmCallback mConfirmCallback) {
        this.context = context;
        this.device = device;
        this.mConfirmCallback = mConfirmCallback;
        this.gson = new GsonBuilder().serializeNulls().create();
        this.restFulWebService = new RestFulWebService();
    }

    public interface ConfirmCallback {
        public void onConfirmReceived(Position position);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Buscando ubicación actual", "Por favor espere...", false, false);
    }

    @Override
    protected Position doInBackground(String... urls) {
        Position positionResponse = null;
        try {
            String _respuesta = restFulWebService.postDataJson(urls[0], gson.toJson(device));
            Log.e("respuesta", "_respuesta device: " + _respuesta);
            if (!_respuesta.equals("") && !_respuesta.equals(null)) {
                try {
                    JSONObject obj = new JSONObject(_respuesta);
                    positionResponse = gson.fromJson(obj.getString("position"), Position.class);
                    Log.e("respuesta", "gson.toJson(positionResponse): " + positionResponse);
                } catch (com.google.gson.JsonSyntaxException e) {

                }
                catch (JSONException e) {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return positionResponse;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(Position position) {
        progressDialog.dismiss();
        mConfirmCallback.onConfirmReceived(position);

    }
}
