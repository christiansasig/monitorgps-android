package com.roussun.android.apps.monitoreogps.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.roussun.android.apps.monitoreogps.MainActivity;
import com.roussun.android.apps.monitoreogps.MyApplication;
import com.roussun.android.apps.monitoreogps.R;
import com.roussun.android.apps.monitoreogps.model.entity.Device;
import com.roussun.android.apps.monitoreogps.model.entity.User;

public class DeviceFragmentDialog extends DialogFragment {

    private ListView list;
    private User user;

    public interface ConfirmDeviceFragmentDialogListener {
        void onFinishDialogStatus(Device device);
    }


    public DeviceFragmentDialog() {
        // Empty constructor required for DialogFragment
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Mis dispositivos")
                .setMessage("Seleccione ...")
                .setView(getContentView())


                .create();
    }

    @SuppressLint("InflateParams")
    private View getContentView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_device, null);
        this.user = ((MyApplication) this.getActivity().getApplication()).getUser();
        this.list = ((ListView) view.findViewById(R.id.devices));
        this.list.setAdapter( new ArrayAdapter<Device>(getContext(), android.R.layout.simple_list_item_1, this.user.getDevices()));
        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dismiss();
                ((MainActivity)getActivity()).onFinishDialogStatus((Device) adapterView.getAdapter().getItem(i));
            }
        });
        return view;
    }

}
