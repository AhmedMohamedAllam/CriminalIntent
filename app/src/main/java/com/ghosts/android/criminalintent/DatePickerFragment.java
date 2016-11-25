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
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Ahmed Allam on 2/7/2016.
 */

public class DatePickerFragment extends DialogFragment {
    public static String mDateArgKey = "Date Argument";
    public static String mDateArgKeyBACK = "Date Argument BACK";
    private Button mOkButton;

    private DatePicker mDatePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


    Date mDate = (Date) getArguments().getSerializable(mDateArgKey);

        View view = inflater.inflate(R.layout.date_picker ,container , false);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int Year = calendar.get(calendar.YEAR);
        int Month = calendar.get(calendar.MONTH);
        int Day = calendar.get(calendar.DAY_OF_MONTH);



        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
        mDatePicker.init(Year, Month, Day, null);

    mOkButton = (Button) view.findViewById(R.id.ok_button);
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
                setResult(Activity.RESULT_OK, date);
                getActivity().finish();
            }
        });


    return view;
    }

    private void setResult(int ResultCode , Date date) {
        if (getTargetFragment() == null) {
            Intent intent = new Intent();
            intent.putExtra(mDateArgKeyBACK, date);
            getActivity().setResult(Activity.RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra(mDateArgKeyBACK, date);
            getTargetFragment().onActivityResult(getTargetRequestCode(), ResultCode, intent);
        }
    }

    public static DatePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(mDateArgKey, date);
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);
        return datePickerFragment;
    }

}
