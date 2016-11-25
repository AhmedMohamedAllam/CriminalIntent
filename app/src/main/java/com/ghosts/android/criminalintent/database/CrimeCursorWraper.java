package com.ghosts.android.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.ghosts.android.criminalintent.Crime;
import com.ghosts.android.criminalintent.database.CrimeDBSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWraper extends CursorWrapper {
    public CrimeCursorWraper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));
        String susbect = getString(getColumnIndex(CrimeTable.Cols.SUSBECT));
        long Contact_id  = getLong(getColumnIndex(CrimeTable.Cols.CONTACT_ID));

        Crime crime = new Crime(UUID.fromString(uuidString));
        crime.setTittle(title);
        crime.setDate(new Date(date));
        crime.setSolved(isSolved != 0);
        crime.setSusbect(susbect);
        crime.setContactID(Contact_id);

        return crime;
    }
}
