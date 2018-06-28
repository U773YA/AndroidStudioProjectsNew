package com.project.android.studentshelper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Utteya on 3/29/2018.
 */

public class QueryPreferences {
    public static String getcinnumber(Context context){
        SharedPreferences settings = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        return settings.getString("cinnumber", "");
    }

    public static String getipaddress(Context context){
        SharedPreferences settings=context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        return settings.getString("dialog_ipaddress","");
    }

    public static void setusername(Context context, String username){
        SharedPreferences settings = context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public static void setcinnumber(Context context, String cinnumber){
        SharedPreferences settings=context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("cinnumber",cinnumber);
        editor.commit();
    }

    public static void setipaddress(Context context, String ipaddress){
        SharedPreferences settings=context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("dialog_ipaddress",ipaddress);
        editor.commit();
    }

    public static String getusername(Context context){
        SharedPreferences settings=context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        return settings.getString("username","");
    }

    public static String getimageurl(Context context){
        SharedPreferences settings=context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        return settings.getString("imageurl","");
    }

    public static void setimageurl(Context context, String imageurl){
        SharedPreferences settings=context.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=settings.edit();
        editor.putString("imageurl",imageurl);
        editor.commit();
    }
}
