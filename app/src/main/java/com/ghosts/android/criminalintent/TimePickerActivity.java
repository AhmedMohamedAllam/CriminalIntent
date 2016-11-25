package com.ghosts.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.Date;

/**
 * Created by Allam on 2/12/2016.
 */
public class TimePickerActivity extends SingleFragmentActivity {
    private static String dateIntentKey = "date intent key";
    private Date mDate;
    private Date getDate(){
        mDate = (Date) getIntent().getSerializableExtra(dateIntentKey);
        return mDate;
    }
    @Override
    protected Fragment createFragment() {
        return TimePickerFragment.newInstance(getDate());
    }

    public static Intent newIntent(Context context , Date date){
        Intent intent = new Intent(context , TimePickerActivity.class);
        intent.putExtra( dateIntentKey , date);
        return intent;
    }
}
