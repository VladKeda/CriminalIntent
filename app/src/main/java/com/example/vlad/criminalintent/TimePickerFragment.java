package com.example.vlad.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "date";
    private TimePicker timePicker;
    public static final String EXTRA_DATE = "com.example.vlad.criminalintent.date";

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_time, null);

        timePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(minute);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        Date date = (Date) getArguments().getSerializable(ARG_DATE);
                        Calendar calendar = Calendar.getInstance();

                        calendar.setTime(date);
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);

                        sendResult(Activity.RESULT_OK, calendar.getTime());
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
