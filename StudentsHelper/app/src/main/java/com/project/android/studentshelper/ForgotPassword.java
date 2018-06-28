package com.project.android.studentshelper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class ForgotPassword extends AppCompatActivity {
    private MaterialEditText MET_CIN;
    private MaterialEditText MET_Question;
    private MaterialEditText MET_Answer;
    private Button btn_Fetch;
    private Button btn_Check;

    private String mSecurityQuestion;
    private String mSecurityAnswer;

    private ProgressDialog load;

    SingletonClass obj=SingletonClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        obj.state_forgotpassword_page=true;

        MET_CIN =(MaterialEditText)findViewById(R.id.cin_number);
        MET_Question =(MaterialEditText)findViewById(R.id.security_question);
        MET_Answer =(MaterialEditText)findViewById(R.id.answer);
        MET_CIN.setText(QueryPreferences.getcinnumber(getApplicationContext()));

        btn_Fetch =(Button)findViewById(R.id.fetch);
        btn_Fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MET_CIN.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "MET_CIN field cannot be blank.", Toast.LENGTH_SHORT).show();
                }else if(MET_CIN.getText().toString().contains(" ")){
                    Toast.makeText(getApplicationContext(),"No spaces allowed",Toast.LENGTH_SHORT).show();
                }
                else {
                    String uri = Uri.parse("http://" + QueryPreferences.getipaddress(getApplicationContext()) + "/myapp/forgot.php?").buildUpon()
                            .appendQueryParameter("cin", MET_CIN.getText().toString())
                            .build().toString();
                    new HttpGetAsync(uri, new AsyncCallback() {
                        @Override
                        public void onStart() {
                            load= ProgressDialog.show(ForgotPassword.this,"","loading");
                        }

                        @Override
                        public void onFinish(String jsonReceive) {
                            load.cancel();
                            if(!jsonReceive.isEmpty()){
                                try {
                                    JSONObject jsonObj = new JSONObject(jsonReceive);
                                    JSONArray jsonArray = jsonObj.getJSONArray("result");
                                    JSONObject j = jsonArray.getJSONObject(0);
                                    String result = j.getString("result");
                                    if (result.equalsIgnoreCase("success")){
                                        JSONObject j1=jsonArray.getJSONObject(1);
                                        mSecurityQuestion =j1.getString("Security_Question");
                                        mSecurityAnswer =j1.getString("Answer");
                                        MET_Question.setText(mSecurityQuestion.replace("&-*"," "));
                                    }else{
                                        Toast.makeText(getApplicationContext(),"CIN Not Registered. Please SIGN UP!",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Log.e("JSONError", "JSON Parsing error");
                                }
                            }
                        }
                    }).execute();
                }
            }
        });

        btn_Check =(Button)findViewById(R.id.check);
        btn_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MET_Answer.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Answer field cannot be blank.", Toast.LENGTH_SHORT).show();
                }else {
                    if (MET_Answer.getText().toString().equalsIgnoreCase(mSecurityAnswer)) {
                        obj.state_forgotpassword_page=false;
                        Intent intent = new Intent(getApplicationContext(), ChangePasswordpre.class);
                        intent.putExtra("cin", MET_CIN.getText().toString());
                        startActivity(intent);
                    }
                }
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
                String myString=QueryPreferences.getipaddress(getApplicationContext());
                text.setText(myString);
                new AlertDialog.Builder(this)
                        .setTitle("Add IpAddress")
                        .setView(view)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        QueryPreferences.setipaddress(getApplicationContext(),text.getText().toString());
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
        if(!(obj.state_forgotpassword_page))
            finish();
    }
}
