package com.ghosts.android.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.transform.Result;

/**
 * Created by Allam on 2/9/2016.
 */
public class TimePickerFragment extends DialogFragment {
    private TimePicker mTimePicker;
    private static final String mTimeKey = "time key";
    public static final String mTimeKeyBack = "time key";
    private Calendar calendar;
    private Button mButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.time_picker_fragment, container, false);
        mTimePicker = (TimePicker) v.findViewById(R.id.time_picker);

            Date date = (Date) getArguments().getSerializable(mTimeKey);

        calendar = Calendar.getInstance();
        calendar.setTime(date);
        int Hour = calendar.get(calendar.HOUR);
        int Minute = calendar.get(calendar.MINUTE);

        mTimePicker.setCurrentHour(Hour);
        mTimePicker.setCurrentMinute(Minute);

        mButton = (Button) v.findViewById(R.id.ok_button_time);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Hour = mTimePicker.getCurrentHour();
                int Minute = mTimePicker.getCurrentMinute();
                setResult(Activity.RESULT_OK, Hour, Minute);
                getActivity().finish();
            }
        });


        return v;
    }

    private void setResult(int ResultCode, int Hour, int Minute) {
        if (getTargetFragment() == null) {
            int Year = calendar.get(calendar.YEAR);
            int Month = calendar.get(calendar.MONTH);
            int Day = calendar.get(calendar.DAY_OF_MONTH);
            Date date = new GregorianCalendar(Year, Month, Day, Hour, Minute).getTime();
            Intent intent = new Intent();
            intent.putExtra(mTimeKeyBack, date);
            getActivity().setResult(ResultCode, intent);
        } else {
            int Year = calendar.get(calendar.YEAR);
            int Month = calendar.get(calendar.MONTH);
            int Day = calendar.get(calendar.DAY_OF_MONTH);
            Date date = new GregorianCalendar(Year, Month, Day, Hour, Minute).getTime();
            Intent intent = new Intent();
            intent.putExtra(mTimeKeyBack, date);
            getTargetFragment().onActivityResult(getTargetRequestCode(), ResultCode, intent);
        }
    }

    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(mTimeKey, date);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }


}
