package com.ghosts.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by Allam on 2/9/2016.
 */
public class DatePickerActivity extends SingleFragmentActivity {
    private static String mDateIntent = "Date Intent";

    private Date mDate;
    private Date getDate(){
        mDate = (Date) getIntent().getSerializableExtra(mDateIntent);
        return mDate;
    }
    @Override
    protected Fragment createFragment() {

        return DatePickerFragment.newInstance(getDate());
    }

    public static Intent newIntent(Context context , Date date){
        Intent intent = new Intent(context , DatePickerActivity.class);
        intent.putExtra(mDateIntent, date);
        return intent;
    }




}
