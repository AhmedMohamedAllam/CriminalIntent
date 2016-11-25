package com.ghosts.android.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by Ahmed Allam on 2/5/2016.
 */
public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks , CrimeFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(getBaseContext(), crime.getId());
            startActivity(intent);
        }else {
            Fragment fragment = CrimeFragment.newInstance(crime.getId());
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment_container , fragment).commit();
        }
    }

    @Override
    public void onCrimeUpdate(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}
