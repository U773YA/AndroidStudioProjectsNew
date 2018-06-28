package com.project.android.studentshelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android.studentshelper.Networking.AsyncCallback;
import com.project.android.studentshelper.Networking.HttpGetAsync;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Utteya on 3/29/2018.
 */

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private LinearLayout lin_OnlineLibrary;
    private LinearLayout lin_Noticeboard;
    private LinearLayout lin_Marks;
    private LinearLayout lin_Attendence;
    private LinearLayout lin_OnlineFees;
    private LinearLayout lin_Ecampus;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    private TextView txt_Name;
    private TextView txt_Dept;
    private TextView txt_Sem;
    private TextView txt_Roll;
    private ImageView img_Profile;

    private ProgressDialog load;

    private long mBackPressed = 0;

    SingletonClass obj=SingletonClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        String uri = Uri.parse("http://" + QueryPreferences.getipaddress(getApplicationContext()) + "/myapp/initializer.php?").buildUpon()
                .appendQueryParameter("cin", QueryPreferences.getcinnumber(getApplicationContext()))
                .build().toString();
        new HttpGetAsync(uri, new AsyncCallback() {
            @Override
            public void onStart() {
                load = ProgressDialog.show(MainPage.this, "", "loading");
                load.setCancelable(true);
            }

            @Override
            public void onFinish(String jsonReceive) {
                load.cancel();
                if (!jsonReceive.isEmpty()) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonReceive);
                        JSONArray jsonArray = jsonObj.getJSONArray("result");
                        JSONObject j = jsonArray.getJSONObject(0);
                        Students s = new Students(j.getString("CIN_Number"), j.getString("Student_Name"), j.getString("Registration_Number"), j.getString("Department_Code"), j.getString("Guardian_Name"), j.getString("Address"), j.getString("Mobile_Number"), j.getString("Date_of_Birth"), j.getString("Blood_Group"), j.getString("Mail_ID"), j.getString("Image_of_Student"), j.getString("Subject_Combination"), j.getString("Semester"));
                        obj.setStudent(s);
                        img_Profile =(CircleImageView)findViewById(R.id.profile_image);
                        new DownloadImageTask(img_Profile).execute("http://" + QueryPreferences.getipaddress(getApplicationContext()) + "/images/Student_Images/" + obj.getStudent().getImagename());
                        txt_Name = (TextView) findViewById(R.id.text_username);
                        txt_Name.setText(obj.getStudent().getName());
                        txt_Roll =(TextView)findViewById(R.id.text_roll);
                        txt_Roll.setText("Roll no. - "+obj.getStudent().getCin().substring(13));
                        txt_Dept =(TextView)findViewById(R.id.text_dept);
                        txt_Dept.setText(obj.getStudent().getDept());
                        txt_Sem =(TextView)findViewById(R.id.text_sem);
                        txt_Sem.setText("Sem - "+obj.getStudent().getSem());
                    } catch (JSONException e) {
                        Log.e("JSONError", "JSON Parsing error");
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "IP Address error.", Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();

        lin_OnlineLibrary = (LinearLayout) findViewById(R.id.online_library);
        lin_OnlineLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://220.225.85.237:8001/"));
                startActivity(browserIntent);
            }
        });

        lin_Noticeboard = (LinearLayout) findViewById(R.id.notice);
        lin_Noticeboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =Noticeboard.newIntent(getApplicationContext()) ;
                startActivity(i);
            }
        });

        lin_Marks = (LinearLayout) findViewById(R.id.marks);
        lin_Marks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Sem_Chooser.newIntent(getApplicationContext());
                startActivity(i);
            }
        });

        lin_Attendence = (LinearLayout) findViewById(R.id.attendance);
        lin_Attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = Uri.parse("http://" + QueryPreferences.getipaddress(getApplicationContext()) + "/myapp/attendance_check.php?").buildUpon().appendQueryParameter("cin", QueryPreferences.getcinnumber(getApplicationContext())).build().toString();
                new HttpGetAsync(uri, new AsyncCallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onFinish(String jsonReceive) {
                        if (!jsonReceive.isEmpty()) {
                            Intent intent = Attendance.newIntent(getApplicationContext());
                            intent.putExtra("json", jsonReceive);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "IP Address error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute();
            }
        });

        lin_OnlineFees = (LinearLayout) findViewById(R.id.Online_Fees);
        lin_OnlineFees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sxceducation.in/onlinefees.htm"));
                startActivity(browserIntent);
            }
        });

        lin_Ecampus = (LinearLayout) findViewById(R.id.ecampus);
        lin_Ecampus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://epaathsala.com/erpnew/loginecampusstud.aspx"));
                startActivity(browserIntent);
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final int[] mResources = {
                R.drawable.first,
                R.drawable.second,
                R.drawable.third,
                R.drawable.fourth
        };

        CustomPagerAdapter_SlideShow mCustomPagerAdapterSlideShow = new CustomPagerAdapter_SlideShow(this, mResources);

        int i = 0;
        final ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mCustomPagerAdapterSlideShow);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 400ms
                mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1) % mResources.length);
                handler.postDelayed(this, 4000);
            }
        }, 4000);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        String myString = QueryPreferences.getusername(MainPage.this);
        if (myString.isEmpty()) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(load.isShowing()){
            load.dismiss();
        }else if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            if (mBackPressed + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(getApplicationContext(), "Press Back again to exit.", Toast.LENGTH_SHORT).show();
            }
            mBackPressed = System.currentTimeMillis();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.idCard) {
            Intent intent=new Intent(getApplicationContext(), IDFront.class);
            startActivity(intent);
        } else if (id == R.id.ipAddress) {
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_ipaddress,null,false);
            final EditText text=(EditText)view.findViewById(R.id.editText_ip);
            String myString=QueryPreferences.getipaddress(MainPage.this);
            text.setText(myString);
            new AlertDialog.Builder(MainPage.this)
                    .setTitle("Add IpAddress")
                    .setView(view)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    QueryPreferences.setipaddress(MainPage.this,text.getText().toString());
                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

        } else if (id == R.id.changePassword) {
            Intent intent= ChangePasswordpost.newIntent(getApplicationContext());
            startActivity(intent);

        } else if (id == R.id.logOut) {
            QueryPreferences.setusername(MainPage.this,"");
            Intent intent=new Intent(MainPage.this,LoginPage.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, MainPage.class);
        return i;
    }
}
