package com.roussun.android.apps.monitoreogps.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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
import com.roussun.android.apps.monitoreogps.utils.Utils;
import com.roussun.android.apps.monitoreogps.webservice.GetPositionByDeviceTask;
import com.roussun.android.apps.monitoreogps.webservice.RetriveHistorialPositionByDateTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by christiansasig on 8/9/16.
 */
public class HistorialPositionFragment extends Fragment implements OnMapReadyCallback{

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

    private FloatingActionButton searchButton;
    private Spinner devices;
    private Button dateStart;
    private Button dateEnd;

    public HistorialPositionFragment() {
        // Required empty public constructor
    }



    /** Called when sample is created. Displays generic UI with welcome text. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_historial_position, container, false);

        // Inflate the layout for this fragment

        //this.user = (User)getArguments().getSerializable("user");
        this.user = ((MyApplication) this.getActivity().getApplication()).getUser();

        this.getViews(rootView);
        this.getAcctions();

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


    private void getViews(View view) {
        this.searchButton = (FloatingActionButton) view.findViewById(R.id.searchButton);
        this.dateStart = (Button) view.findViewById(R.id.dateStart);
        this.dateEnd = (Button) view.findViewById(R.id.dateEnd);
        this.devices = (Spinner) view.findViewById(R.id.device);
       // this.operatorRadioGroup = (RadioGroup) view.findViewById(R.id.operatorRadioGroup);
    }

    private void emplyFileds() {

        /*operatorRadioGroup.clearCheck();
        phoneEditText.setText("");
        valueEditText.setText("");*/
    }

    private void getAcctions() {
        ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(getContext(), android.R.layout.simple_spinner_item, this.user.getDevices());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.devices.setAdapter(adapter);

        this.dateStart.setText(Utils.getCurrentDateAndHour());
        this.dateEnd.setText(Utils.getCurrentDateAndHour());

        this.searchButton.setOnClickListener(new View.OnClickListener() {
            Device device = (Device) devices.getSelectedItem();
            @Override
            public void onClick(View view) {
               new RetriveHistorialPositionByDateTask(getContext(), device, dateStart.getText().toString(), dateEnd.getText().toString(), new RetriveHistorialPositionByDateTask.ConfirmCallback() {
                   @Override
                   public void onConfirmReceived(List<Position> positions) {
                       if(positions.size() == 0)
                       {
                            Toast.makeText(getContext(), "No existen registros", Toast.LENGTH_LONG).show();
                       }
                       if(mMap != null)
                       {
                           mMap.clear();
                           for (Position position:positions
                                   ) {

                               LatLng sydney = new LatLng(position.getLatitude(),position.getLongitude());
                               Marker marker  = mMap.addMarker(new MarkerOptions().position(sydney).title(device.getName()).snippet("Fecha: " + position.getCreatedAt()));
                               marker.showInfoWindow();
                               mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                               mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                           }
                       }

                   }
               }).execute(MyApplication.URL_GET_HISTORIAL_POSITIONS_BY_DEVICE);
            }
        });



        this.dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DateTimePickerFragment(new DateTimePickerFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date, TimePicker time, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
                        String startDate = Utils.getDateTimeStringFormat(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                        dateStart.setText(startDate);
                    }
                });
                newFragment.show(getFragmentManager(), "dateTimePicker1");
            }
        });

        this.dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DateTimePickerFragment(new DateTimePickerFragment.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker date, TimePicker time, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
                        String endDate = Utils.getDateTimeStringFormat(year, monthOfYear, dayOfMonth, hourOfDay, minute);
                        dateEnd.setText(endDate);
                    }
                });
                newFragment.show(getFragmentManager(), "dateTimePicker2");
            }
        });

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
