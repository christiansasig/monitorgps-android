package com.roussun.android.apps.monitoreogps.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.roussun.android.apps.monitoreogps.model.entity.Device;
import com.roussun.android.apps.monitoreogps.model.entity.Position;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RetriveHistorialPositionByDateTask extends AsyncTask<String, Void, List<Position>> {
    private  RestFulWebService restFulWebService;
    private Gson gson;
    private Context context;
    private Device device;
    private String startDate;
    private String endDate;
    private ConfirmCallback mConfirmCallback;
    private ProgressDialog progressDialog;

    public RetriveHistorialPositionByDateTask(Context context, Device device, String startDate, String endDate, ConfirmCallback mConfirmCallback) {
        this.context = context;
        this.device = device;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mConfirmCallback = mConfirmCallback;
        this.gson = new GsonBuilder().serializeNulls().create();
        this.restFulWebService = new RestFulWebService();
    }

    public interface ConfirmCallback {
        public void onConfirmReceived(List<Position> positions);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(context, "Buscando historial", "Por favor espere...", false, false);
    }

    @Override
    protected List<Position> doInBackground(String... urls) {
        List<Position> positionsResponse = null;
        Type listType = new TypeToken<ArrayList<Position>>() {}.getType();

        try {
            JSONObject json = new JSONObject();
            json.accumulate("device", device.getId());
            json.accumulate("startDate", startDate);
            json.accumulate("endDate", endDate);

            Log.e("datos", " gson.toJson(json).toString(): " +  json.toString());

            String _respuesta = restFulWebService.postDataJson(urls[0], json.toString());
            Log.e("respuesta", "_respuesta device: " + _respuesta);
            if (!_respuesta.equals("") && !_respuesta.equals(null)) {
                try {
                    JSONObject obj = new JSONObject(_respuesta);
                    positionsResponse = gson.fromJson(obj.getString("positions"), listType);
                } catch (com.google.gson.JsonSyntaxException e) {

                }
                catch (JSONException e) {

                }
            }
        }  catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return positionsResponse;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<Position> positions) {
        progressDialog.dismiss();
        mConfirmCallback.onConfirmReceived(positions);

    }
}
