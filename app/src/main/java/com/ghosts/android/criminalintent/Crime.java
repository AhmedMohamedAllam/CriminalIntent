package com.ghosts.android.criminalintent;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Ahmed Allam on 2/4/2016.
 */
public class Crime {
    private UUID mId;
    private String mTittle;
    private Date mDate;
    private boolean mSolved;
    private String mSusbect;
    private long mContactID;


    public Crime() {
        this(UUID.randomUUID());
    }

    public long getContactID() {
        return mContactID;
    }

    public void setContactID(long contactID) {
        mContactID = contactID;
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
    }


    public UUID getId() {
        return mId;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getTittle() {
        return mTittle;

    }

    public void setTittle(String tittle) {
        mTittle = tittle;
    }

    public String getSusbect() {
        return mSusbect;
    }

    public void setSusbect(String susbect) {
        mSusbect = susbect;
    }

    public String getPhotoName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
