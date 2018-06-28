package com.project.android.studentshelper.Networking;

/**
 * Created by Utteya on 3/29/2018.
 */

public interface AsyncCallback {
    void onStart();
    void onFinish(String jsonReceive);
}
