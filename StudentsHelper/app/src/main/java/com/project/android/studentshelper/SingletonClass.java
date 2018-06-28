package com.project.android.studentshelper;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Utteya on 3/29/2018.
 */

public class SingletonClass {
    private static final SingletonClass ourInstance = new SingletonClass();

    public static boolean state_login_page;
    public static boolean state_signup_page;
    public static boolean state_forgotpassword_page;
    public static boolean state_splash_page;
    public static boolean state_change_password;

    private static String notice;
    private static ArrayList<String> absent;
    private static Students student;
    private static Bitmap studentImage;

    public static SingletonClass getInstance() {
        return ourInstance;
    }

    private SingletonClass() {
    }

    public static String getNotice() {
        return notice;
    }

    public static void setNotice(String notice) {
        SingletonClass.notice = notice;
    }

    public static ArrayList<String> getAbsent() {
        return absent;
    }

    public static void setAbsent(ArrayList<String> absent) {
        SingletonClass.absent = absent;
    }

    public static Students getStudent() {
        return student;
    }

    public static void setStudent(Students student) {
        SingletonClass.student = student;
    }

    public static Bitmap getStudentImage() {
        return studentImage;
    }

    public static void setStudentImage(Bitmap studentImage) {
        SingletonClass.studentImage = studentImage;
    }


}
