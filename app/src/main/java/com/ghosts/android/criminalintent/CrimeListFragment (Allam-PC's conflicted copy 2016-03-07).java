package com.ghosts.android.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ahmed Allam on 2/5/2016.
 */
public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    private int mPosition;
    private boolean mVisibleSubtitle;
    private String subtitle;
    private Button mAddCrime;
    private Callbacks mCallbacks;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        mAddCrime = (Button) v.findViewById(R.id.add_crime_in_list);

        if (CrimeLab.get(getActivity()).getCrimes().size() == 0) {
            Toast.makeText(getContext(), R.string.no_crimes, Toast.LENGTH_LONG).show();
            mAddCrime.setVisibility(View.VISIBLE);
            mAddCrime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Crime crime = new Crime();
                    CrimeLab.get(getActivity()).addCrime(crime);
                    Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                    startActivity(intent);

                }
            });
        } else
            mAddCrime.setVisibility(View.GONE);


        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("key");
            mVisibleSubtitle = savedInstanceState.getBoolean("bool");
            subtitle = savedInstanceState.getString("subtitle");
            updateSubtitle();
        }
        updateUI();
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key", mPosition);
        outState.putBoolean("bool", mVisibleSubtitle);
        outState.putString("subtitle", subtitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        updateSubtitle();
        if (CrimeLab.get(getActivity()).getCrimes().size() == 0) {
            mAddCrime.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), R.string.no_crimes, Toast.LENGTH_LONG).show();
        } else
            mAddCrime.setVisibility(View.GONE);

    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimeLab.getCrimes());
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setmCrimes(crimeLab.getCrimes());
            mAdapter.notifyItemChanged(mPosition);
        }
        updateSubtitle();
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mCrimeTitleHolderTextView;
        private TextView mDateTextView;
        private CheckBox mSolved;
        private Crime mCrime;


        public CrimeHolder(View itemView) {
            super(itemView);
            mCrimeTitleHolderTextView = (TextView) itemView.findViewById(R.id.upper_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.lower_text_view);
            mSolved = (CheckBox) itemView.findViewById(R.id.right_checkbox);
            mSolved.setClickable(false);
            itemView.setOnClickListener(this);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd MMM   HH:mm");

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mCrimeTitleHolderTextView.setText(crime.getTittle());
            mDateTextView.setText(simpleDateFormat.format(crime.getDate()));
            mSolved.setChecked(crime.isSolved());

        }

        @Override
        public void onClick(View v) {
            mPosition = getLayoutPosition();
            mCallbacks.onCrimeSelected(mCrime);

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        public List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.crime_list_item, parent, false);
            return new CrimeHolder(v);
        }

        private void setmCrimes(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list_menu, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_show_subtitle);
        if (mVisibleSubtitle) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        } else {
            showSubtitle.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_crime_item:
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                updateUI();
                mCallbacks.onCrimeSelected(crime);
                return true;
            case R.id.menu_show_subtitle:
                mVisibleSubtitle = !mVisibleSubtitle;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int crimesize = crimeLab.getCrimes().size();
        subtitle = getResources().getQuantityString(R.plurals.subtitle_quantity, crimesize, crimesize);

        if (!mVisibleSubtitle)
            subtitle = null;

        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    public interface Callbacks{
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}





