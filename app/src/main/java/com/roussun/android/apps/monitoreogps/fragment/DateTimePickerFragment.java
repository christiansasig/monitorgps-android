package com.roussun.android.apps.monitoreogps.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.roussun.android.apps.monitoreogps.R;

import java.util.Calendar;

public class DateTimePickerFragment extends DialogFragment {

	private DatePicker datePicker;
	private TimePicker timePicker;
	private DateTimePickerFragment.OnDateSetListener mDateSetListener;

	public interface OnDateSetListener {
		void onDateSet(DatePicker date, TimePicker time, int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute);
	}


	public DateTimePickerFragment(DateTimePickerFragment.OnDateSetListener listener) {
		mDateSetListener = listener;
    }

    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    return new AlertDialog.Builder(getActivity())
        .setTitle("Seleccione la fecha y hora")
        .setView(getContentView())
        .setPositiveButton("Aceptar",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
					if (mDateSetListener != null) {
						mDateSetListener.onDateSet(datePicker, timePicker, datePicker.getYear(),
								datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
					}

                    
                }
            }
        )
        .setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                	dialog.cancel();
                }
            }
        )
        .create();
    }
     
    @SuppressLint("InflateParams")
	private View getContentView() {
	    LayoutInflater inflater = getActivity().getLayoutInflater();
	    View view = inflater.inflate(R.layout.fragment_datetime, null);
	    datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		datePicker.updateDate(year,month, day);

		timePicker.setCurrentHour(hour);
		timePicker.setCurrentMinute(minute);
		//datePicker.init(year, month, day, null);

	    return view;
    }
}
