package com.ghosts.android.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.ghosts.android.criminalintent.database.CrimeBaseHelper;
import com.ghosts.android.criminalintent.database.CrimeCursorWraper;
import com.ghosts.android.criminalintent.database.CrimeDBSchema;
import com.ghosts.android.criminalintent.database.CrimeDBSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Ahmed Allam on 2/5/2016.
 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
            return sCrimeLab;
        }
        return sCrimeLab;
    }

    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE, crime.getTittle());
        values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Cols.SUSBECT, crime.getSusbect());
        values.put(CrimeTable.Cols.CONTACT_ID, crime.getContactID());

        return values;
    }

    private CrimeLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    }

    public List<Crime> getCrimes() {
        mCrimes = new ArrayList<>();
        CrimeCursorWraper cursor = getQuery(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                mCrimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return mCrimes;
    }

    private CrimeCursorWraper getQuery(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new CrimeCursorWraper(cursor);
    }

    public void addCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    public void deleteCrime(String whereClause, String[] whereArgs) {
        mDatabase.delete(CrimeTable.NAME, whereClause + " = ?", whereArgs);
    }


    public void updateCrime(Crime crime) {
        String uuidString = crime.getId().toString();
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeTable.NAME, values, CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public Crime getCrimeById(UUID id) {
        CrimeCursorWraper cursor = getQuery(CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});
        if (cursor.getCount() == 0)
            return null;

        try {
            cursor.moveToFirst();
            return cursor.getCrime();

        } finally {
            cursor.close();
        }

    }

    public File getPhotoFile(Crime crime) {

        File ExternalFilesDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (ExternalFilesDir == null)
            return null;

            return new File(ExternalFilesDir , crime.getPhotoName());


    }
}
