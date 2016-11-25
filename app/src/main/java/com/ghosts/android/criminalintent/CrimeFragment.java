package com.ghosts.android.criminalintent;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.ghosts.android.criminalintent.database.CrimeDBSchema;
import com.ghosts.android.criminalintent.database.CrimeDBSchema.CrimeTable;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ahmed Allam on 2/4/2016.
 */
public class CrimeFragment extends Fragment {
    private EditText mTittleEditText;
    private Crime mCrime;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private Button mSendReport;
    private Button mGetSusbect;
    private String DateTag = "DateTag";
    private final int mDateRequestCode = 0;
    private final int mTimeRequestCode = 1;
    private final int requestSusbect = 2;
    private static final int mPermisionRequestCode = 3;
    private static final int mPermisionCameraRequestCode = 5;
    private final int mCameraRequestCode = 4;
    private Date mDateSelected;
    private long mContactID;
    private Button mCallButton;
    private String mPhoneNumber;
    private boolean mGrantPermissionFlag = false;
    private ImageButton mCameraButton;
    private ImageView mImageView;
    private File mPhotoFile;
    private String ImageTag = "ImageTag";
    private Callbacks mCallbacks;
    private String mTimePickerFragment = "Time picker Fragment";
    private static String mArgKEY = "com.ghosts.android.criminalintent.argKey";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        UUID uuid = (UUID) getArguments().getSerializable(mArgKEY);
        mCrime = CrimeLab.get(getActivity()).getCrimeById(uuid);
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);
        mTittleEditText = (EditText) v.findViewById(R.id.crime_title);
        mTittleEditText.setText(mCrime.getTittle());
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        mTittleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTittle(s.toString());
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        mTimeButton = (Button) v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isTab = getResources().getBoolean(R.bool.isTablet);
                if (isTab) {
                    FragmentManager fragmentManager = getFragmentManager();
                    TimePickerFragment timePickerFragment = TimePickerFragment.newInstance(mCrime.getDate());
                    timePickerFragment.setTargetFragment(CrimeFragment.this, mTimeRequestCode);
                    timePickerFragment.show(fragmentManager, mTimePickerFragment);
                } else {
                    Intent intent = TimePickerActivity.newIntent(getActivity(), mCrime.getDate());
                    startActivityForResult(intent, mTimeRequestCode);
                }
            }
        });


        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isTab = getResources().getBoolean(R.bool.isTablet);
                if (isTab) {
                    FragmentManager fm = getFragmentManager();
                    DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                    datePickerFragment.setTargetFragment(CrimeFragment.this, mDateRequestCode);
                    datePickerFragment.show(fm, DateTag);
                } else {
                    Intent intent = DatePickerActivity.newIntent(getActivity(), mCrime.getDate());
                    startActivityForResult(intent, mDateRequestCode);
                }


            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });
        mSendReport = (Button) v.findViewById(R.id.report_button);
        mSendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject)).startChooser();

            }
        });

        mGetSusbect = (Button) v.findViewById(R.id.susbect_button);
        final Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mGetSusbect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLab.get(getActivity()).updateCrime(mCrime);
                startActivityForResult(pickContactIntent, requestSusbect);

            }
        });
        if (mCrime.getSusbect() != null) {
            mGetSusbect.setText(mCrime.getSusbect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mGetSusbect.setEnabled(false);
        }

        mCallButton = (Button) v.findViewById(R.id.call_susbect_button);
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCrime.getSusbect() == null) {
                    Toast.makeText(getContext(), R.string.no_susbect_selected, Toast.LENGTH_LONG).show();
                } else {
                    GrantPermission(mPermisionRequestCode);
                }
            }
        });

        mCameraButton = (ImageButton) v.findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GrantPermission(mPermisionCameraRequestCode);
            }
        });
        mImageView = (ImageView) v.findViewById(R.id.image_view);
        updatePhotoFile();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                ImageDialog dialog = ImageDialog.newInstance(mCrime.getId());
                dialog.show(fragmentManager, ImageTag);
            }
        });
        return v;
    }

    public void updatePhotoFile() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mImageView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mImageView.setImageBitmap(bitmap);
        }

    }

    private void openCamera() {
        final Intent CameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean CanTakePhoto = mPhotoFile != null &&
                CameraIntent.resolveActivity(getActivity().getPackageManager()) != null;
        if (CanTakePhoto) {
            Uri uri = Uri.fromFile(mPhotoFile);
            CameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(CameraIntent, mCameraRequestCode);
        }

    }


    public void GrantPermission(int requestCode) {
        switch (requestCode) {
            case mPermisionRequestCode:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
                } else
                    showContact();
                return;
            case mPermisionCameraRequestCode:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getActivity()
                        , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                } else
                    openCamera();
                return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mPermisionRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showContact();
            } else {
                Toast.makeText(getContext(), "Until you Accept the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == mPermisionCameraRequestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(getContext(), "Until you Accept the permission, we canot open the Camera", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void showContact() {
        Cursor phoneCursor = getActivity().getContentResolver().query(
                Phone.CONTENT_URI, null, null, null, null);

        try {
            if (phoneCursor.getCount() == 0) {
                Toast.makeText(getContext(), "No number for this susbect!", Toast.LENGTH_SHORT).show();
                return;
            }

            phoneCursor.moveToFirst();
            while (!phoneCursor.isAfterLast()) {
                String x = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.CONTACT_ID));
                if (Integer.parseInt(x) == mCrime.getContactID()) {
                    mPhoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(Phone.NUMBER));
                }
                phoneCursor.moveToNext();
            }
        } finally {
            phoneCursor.close();
        }

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mPhoneNumber));
        startActivity(intent);

    }


    private void updateDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd");
        mDateButton.setText(simpleDateFormat.format(mCrime.getDate()));
    }

    private void updateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        mTimeButton.setText(simpleDateFormat.format(mCrime.getDate()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == mDateRequestCode) {
            mDateSelected = (Date) data.getSerializableExtra(DatePickerFragment.mDateArgKeyBACK);
            mCrime.setDate(mDateSelected);
            updateCrime();
            updateDate();

        } else if (requestCode == mTimeRequestCode) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.mTimeKeyBack);
            mCrime.setDate(date);
            updateCrime();
            updateTime();

        } else if (requestCode == requestSusbect && data != null) {
            Uri UriContact = data.getData();

            String[] DisplayNameField = new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts._ID};

            Cursor cursor = getActivity().getContentResolver().query(UriContact, DisplayNameField, null, null, null);


            try {
                if (cursor.getCount() == 0) {
                    return;
                }
                cursor.moveToFirst();
                String susbectName = cursor.getString(0);
                updateCrime();
                mContactID = cursor.getLong(1);
                mCrime.setContactID(mContactID);

                mCrime.setSusbect(susbectName);
                CrimeLab.get(getActivity()).updateCrime(mCrime);
                mGetSusbect.setText(susbectName);

            } finally {
                cursor.close();
            }

        } else if (requestCode == mCameraRequestCode) {
            updateCrime();
            updatePhotoFile();
        }
    }

    public static CrimeFragment newInstance(UUID id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(mArgKEY, id);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_Done_item:
                getActivity().finish();
                return true;
            case R.id.menu_delete_item:
                CrimeLab.get(getActivity()).deleteCrime(CrimeTable.Cols.UUID, new String[]{mCrime.getId().toString()});
                getActivity().finish();
                CrimeLab.get(getActivity()).updateCrime(mCrime);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private String getCrimeReport() {
        String Solved = null;
        boolean solved = mCrime.isSolved();
        if (solved)
            Solved = getString(R.string.crime_report_solved);
        else
            Solved = getString(R.string.crime_report_unsolved);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM dd");
        String crimeDate = simpleDateFormat.format(mCrime.getDate()).toString();

        String susbect = mCrime.getSusbect();
        if (susbect == null)
            susbect = getString(R.string.crime_report_no_suspect);
        else
            susbect = getString(R.string.crime_report_suspect, susbect);

        String report = getString(R.string.crime_report, mCrime.getTittle(), crimeDate, Solved, susbect);
        return report;


    }

    public interface Callbacks {
        void onCrimeUpdate(Crime crime);
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

    private void updateCrime() {
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdate(mCrime);
    }
}
