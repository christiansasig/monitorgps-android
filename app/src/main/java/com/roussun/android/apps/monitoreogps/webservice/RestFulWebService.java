package com.roussun.android.apps.monitoreogps.webservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class RestFulWebService {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public RestFulWebService() {

    }

    public Bitmap downloadBitmap(String url) throws IOException {
        Bitmap retVal = null;
        try
        {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setRequestMethod("GET");
            return BitmapFactory.decodeStream(conn.getInputStream());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return retVal;
    }

    public String getData(String urlString) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            is = conn.getInputStream();

            String contentAsString = convertStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String postDataJson(String urlString, String dataJson) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(30000 /* milliseconds */);
            conn.setConnectTimeout(40000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("accept", "application/json");
            // Starts the query
            //conn.connect();
            Writer writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
            writer.write(dataJson);
            writer.close();


           /* DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(URLEncoder.encode(dataJson.toString(), "UTF-8"));
            wr.flush();
            wr.close();*/
            is = conn.getInputStream();

            String contentAsString = convertStreamToString(is);
            return contentAsString;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}