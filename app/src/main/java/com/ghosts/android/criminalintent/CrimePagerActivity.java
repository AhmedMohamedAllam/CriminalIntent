package com.ghosts.android.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Ahmed Allam on 2/7/2016.
 */
public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks {
    private ViewPager mViewPager ;
    private List<Crime> mCrimes;
    private static String mIdKEY = "com.ghosts.android.criminalintent.GetUUID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        final UUID mUUID = (UUID) getIntent().getSerializableExtra(mIdKEY);

                mCrimes = CrimeLab.get(getBaseContext()).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.activity_view_pager);
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }


            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        mViewPager.setCurrentItem(getPosition(mUUID));
    }

    private int getPosition(UUID id){
       for(int i =0 ; i<mCrimes.size(); i++){
           if(mCrimes.get(i).getId().equals(id))
           return i;
       }
        return 0;
    }
    public static Intent newIntent(Context context , UUID id ){
        Intent intent = new Intent(context , CrimePagerActivity.class);
        intent.putExtra(mIdKEY, id);
        return intent;
    }


    @Override
    public void onCrimeUpdate(Crime crime) {

    }
}
