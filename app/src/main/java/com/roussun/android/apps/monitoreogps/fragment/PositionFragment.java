package com.roussun.android.apps.monitoreogps.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.roussun.android.apps.monitoreogps.MyApplication;
import com.roussun.android.apps.monitoreogps.R;
import com.roussun.android.apps.monitoreogps.model.entity.Device;
import com.roussun.android.apps.monitoreogps.model.entity.Position;
import com.roussun.android.apps.monitoreogps.model.entity.User;
import com.roussun.android.apps.monitoreogps.webservice.GetPositionByDeviceTask;

/**
 * Created by christiansasig on 8/9/16.
 */
public class PositionFragment extends Fragment implements OnMapReadyCallback {

    private ProgressDialog progressDialogTaskAsync;
    private GoogleMap mMap;
    private MapView mapView;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private PolylineOptions rectOptions;
    Polyline polyline;
    private User user;
    private View rootView;

    public PositionFragment() {
        // Required empty public constructor
    }



    /** Called when sample is created. Displays generic UI with welcome text. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_position, container, false);

        // Inflate the layout for this fragment

        //this.user = (User)getArguments().getSerializable("user");
        this.user = ((MyApplication) this.getActivity().getApplication()).getUser();

        this.getViews(rootView);
        this.getAcctions();

        this.progressDialogTaskAsync = new ProgressDialog(getActivity());
        this.progressDialogTaskAsync.setIcon(android.R.drawable.stat_sys_upload);
        this.progressDialogTaskAsync.setIndeterminate(false);
        this.progressDialogTaskAsync.setTitle("");
        this.progressDialogTaskAsync.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialogTaskAsync.setCancelable(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        refreshPostionsByDevice();
    }

    public void refreshPostionsByDevice() {
        if(this.user.getDevices().size() == 0)
        {
            getAlert("Aviso", "No tiene dispositivos asignados a su usuario");
        }

        mMap.clear();
        for (final Device device: this.user.getDevices()) {
            new GetPositionByDeviceTask(getContext(), device, new GetPositionByDeviceTask.ConfirmCallback() {
                @Override
                public void onConfirmReceived(Position position) {
                    if(position != null)
                    {
                        // Add a marker in Sydney and move the camera
                        //LatLng sydney = new LatLng(-0.9495872624688885,-78.69712829589844);
                        LatLng sydney = new LatLng(position.getLatitude(),position.getLongitude());
                        Marker marker  = mMap.addMarker(new MarkerOptions().position(sydney).title(device.getName()).snippet("IP:"  + device.getIp()));
                        marker.showInfoWindow();
                        //marker.getPosition().equals()
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                    }
                    else
                    {
                        Toast.makeText(getContext(), "No se pudo obtener la ubicación actual", Toast.LENGTH_LONG).show();
                    }
                }
            }).execute(MyApplication.URL_GET_POSITION_BY_DEVICE);
        }
    }

    private void getViews(View view) {
       // this.operatorRadioGroup = (RadioGroup) view.findViewById(R.id.operatorRadioGroup);
    }

    private void emplyFileds() {

        /*operatorRadioGroup.clearCheck();
        phoneEditText.setText("");
        valueEditText.setText("");*/
    }

    private void getAcctions() {

        /*this.rechargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

    }


    private void getAlert(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(message).setTitle(title);

        builder.setPositiveButton(R.string.accept,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            //buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }
}
