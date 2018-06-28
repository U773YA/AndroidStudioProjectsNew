package com.project.android.studentshelper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android.studentshelper.Networking.AsyncCallback;
import com.project.android.studentshelper.Networking.HttpGetAsync;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Utteya on 3/29/2018.
 */

public class LoginPage extends AppCompatActivity {
    private MaterialEditText edit_CIN;
    private MaterialEditText edit_Password;
    private Button btn_Submit;
    private TextView txt_Signup;
    private TextView txt_Forgot;

    private ProgressDialog load;

    private final SingletonClass obj= SingletonClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        ActionBar Action=getSupportActionBar();
        Action.setTitle("LOGIN");

        obj.state_login_page=true;
        load=new ProgressDialog(LoginPage.this);

        edit_CIN =(MaterialEditText)findViewById(R.id.cin);
        edit_CIN.setText(QueryPreferences.getcinnumber(LoginPage.this));
        edit_Password =(MaterialEditText)findViewById(R.id.password);

        btn_Submit = (Button) findViewById(R.id.login);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryPreferences.setcinnumber(getApplicationContext(), edit_CIN.getText().toString());
                if (edit_CIN.getText().toString().isEmpty() || edit_Password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "A field cannot be blank.", Toast.LENGTH_SHORT).show();
                } else if(edit_CIN.getText().toString().contains(" ") || edit_Password.getText().toString().contains(" ")){
                    Toast.makeText(getApplicationContext(),"No spaces allowed.",Toast.LENGTH_SHORT).show();
                }
                else {
                    String ipaddress = QueryPreferences.getipaddress(getApplicationContext());
                    if (ipaddress.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "IP Address not given.", Toast.LENGTH_SHORT).show();
                    } else {
                        String uri= Uri.parse("http://"+ipaddress+"/myapp/login.php?").buildUpon()
                                .appendQueryParameter("cin", edit_CIN.getText().toString())
                                .appendQueryParameter("password", edit_Password.getText().toString())
                                .build().toString();
                        new HttpGetAsync(uri, new AsyncCallback() {
                            @Override
                            public void onStart() {
                                load= ProgressDialog.show(LoginPage.this,"","loading");
                                load.setCancelable(true);
                            }
                            @Override
                            public void onFinish(String jsonReceive) {
                                load.cancel();
                                if(!jsonReceive.isEmpty()) {
                                    try {
                                        JSONObject jsonObj = new JSONObject(jsonReceive);
                                        JSONArray jsonArray = jsonObj.getJSONArray("result");
                                        JSONObject j = jsonArray.getJSONObject(0);
                                        String result=j.getString("result");
                                        if(result.equalsIgnoreCase("success")) {
                                            j = jsonArray.getJSONObject(1);
                                            String cin = j.getString("CIN_Number");
                                            String name = j.getString("Student_Name");
                                            QueryPreferences.setusername(getApplicationContext(),name);
                                            QueryPreferences.setcinnumber(getApplicationContext(),cin);
                                            obj.state_login_page=false;
                                            Intent intent = new Intent(LoginPage.this, MainPage.class);
                                            startActivity(intent);
                                        }else if(result.equalsIgnoreCase("wrong password")){
                                            Toast.makeText(LoginPage.this,"Password Incorrect.", Toast.LENGTH_SHORT).show();
                                            edit_Password.setText("");
                                        }else if(result.equalsIgnoreCase("wrong cin number")){
                                            Toast.makeText(LoginPage.this,"CIN Number Incorrect.", Toast.LENGTH_SHORT).show();
                                            edit_Password.setText("");
                                        }else if(result.equalsIgnoreCase("does not exist")){
                                            Toast.makeText(LoginPage.this,"Account does not exist. Please SIGN UP.", Toast.LENGTH_SHORT).show();
                                            txt_Signup.setHighlightColor(getResources().getColor(R.color.orange));
                                        }
                                    } catch (JSONException e) {
                                        Log.e("JSONError", "JSON Parsing error");
                                    }
                                }else{
                                    Toast.makeText(LoginPage.this,"IP Address error.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    }
                }
            }
        });

        txt_Signup =(TextView)findViewById(R.id.signup);
        txt_Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=SignUp.newIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        txt_Forgot =(TextView)findViewById(R.id.forgot);
        txt_Forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_ipaddress:
                LayoutInflater inflater = getLayoutInflater();
                View view  = inflater.inflate(R.layout.dialog_ipaddress, null);
                final EditText text=(EditText)view.findViewById(R.id.editText_ip);
                String myString=QueryPreferences.getipaddress(LoginPage.this);
                text.setText(myString);
                new AlertDialog.Builder(this)
                        .setTitle("Add IpAddress")
                        .setView(view)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        QueryPreferences.setipaddress(LoginPage.this,text.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
                return true;
            default:return false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!(obj.state_login_page))
            finish();
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, LoginPage.class);
        return i;
    }

    @Override
    public void onBackPressed() {
        if (load.isShowing()) {
            load.dismiss();
        }
        else
            super.onBackPressed();
    }
}
