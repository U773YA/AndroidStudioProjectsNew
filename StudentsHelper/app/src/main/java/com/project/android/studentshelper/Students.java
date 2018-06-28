package com.project.android.studentshelper;

/**
 * Created by Utteya on 3/29/2018.
 */

public class Students {
    private String mCin;
    private String mName;
    private String mReg;
    private String mDept;
    private String mGuardian;
    private String mAddress;
    private String mMob;
    private String mDob;
    private String mBlood;
    private String mMail;
    private String mImagename;
    private String mSub;
    private String mSem;

    Students(String cin, String name, String reg, String dept, String guardian, String address, String mob, String dob, String blood, String mail, String imagename, String sub, String sem) {
        this.mCin = cin;
        this.mName = name;
        this.mReg = reg;
        this.mDept = dept;
        this.mGuardian = guardian;
        this.mAddress = address;
        this.mMob = mob;
        this.mDob = dob;
        this.mBlood = blood;
        this.mMail = mail;
        this.mImagename = imagename;
        this.mSub = sub;
        this.mSem = sem;
    }

    public String getCin() {
        return mCin;
    }

    public String getName() {
        return mName;
    }

    public String getDept() {
        return mDept;
    }

    public String getGuardian() {
        return mGuardian;
    }

    public String getAddress() {
        return mAddress;
    }

    public String getMob() {
        return mMob;
    }

    public String getDob() {
        return mDob;
    }

    public String getBlood() {
        return mBlood;
    }

    public String getMail() {
        return mMail;
    }

    public String getImagename() {
        return mImagename;
    }

    public String getReg() {
        return mReg;
    }

    public String getSub() {
        return mSub;
    }

    public String getSem() {
        return mSem;
    }
}
