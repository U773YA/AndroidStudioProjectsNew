package com.project.android.studentshelper;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Utteya on 3/29/2018.
 */

public class SignUp extends AppCompatActivity {
    private MaterialEditText edit_CIN;
    private MaterialEditText edit_Password;
    private MaterialEditText edit_confirm;
    private MaterialEditText edit_question;
    private MaterialEditText edit_answer;
    private Button btn_signup;

    private ProgressDialog load;

    private final SingletonClass obj= SingletonClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        obj.state_signup_page=true;

        edit_CIN =(MaterialEditText)findViewById(R.id.cin);
        edit_CIN.setText(QueryPreferences.getcinnumber(getApplicationContext()));
        edit_Password =(MaterialEditText)findViewById(R.id.password);
        edit_confirm =(MaterialEditText)findViewById(R.id.confirm);
        edit_question =(MaterialEditText)findViewById(R.id.question);
        edit_answer =(MaterialEditText)findViewById(R.id.answer);

        btn_signup =(Button)findViewById(R.id.signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QueryPreferences.setcinnumber(getApplicationContext(), edit_CIN.getText().toString());
                if (edit_CIN.getText().toString().isEmpty() || edit_Password.getText().toString().isEmpty() || edit_confirm.getText().toString().isEmpty() || edit_question.getText().toString().isEmpty() || edit_answer.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "A field cannot be blank.", Toast.LENGTH_SHORT).show();
                }else if(!(edit_Password.getText().toString().equalsIgnoreCase(edit_confirm.getText().toString()))){
                    Toast.makeText(getApplicationContext(),"The passwords does not match", Toast.LENGTH_SHORT).show();
                } else if (edit_CIN.getText().toString().contains(" ") || edit_Password.getText().toString().contains(" ") || edit_confirm.getText().toString().contains(" ")) {
                    Toast.makeText(getApplicationContext(), "No spaces allowed", Toast.LENGTH_SHORT).show();
                } else {
                    String ipaddress = QueryPreferences.getipaddress(getApplicationContext());
                    if (ipaddress.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "IP Address not given.", Toast.LENGTH_SHORT).show();
                    } else {
                        String question=edit_question.getText().toString().replace(" ","&-*");
                        String answer=edit_answer.getText().toString().replace(" ","&-*");
                        String uri= Uri.parse("http://"+ipaddress+"/myapp/signup.php?").buildUpon()
                                .appendQueryParameter("cin", edit_CIN.getText().toString())
                                .appendQueryParameter("password", edit_Password.getText().toString())
                                .appendQueryParameter("question", question)
                                .appendQueryParameter("answer", answer)
                                .build().toString();
                        new HttpGetAsync(uri, new AsyncCallback() {
                            @Override
                            public void onStart() {
                                load= ProgressDialog.show(SignUp.this,"","loading");
                            }

                            @Override
                            public void onFinish(String jsonReceive) {
                                load.cancel();
                                if(!jsonReceive.isEmpty()){
                                    try{
                                        JSONObject jsonObj=new JSONObject(jsonReceive);
                                        JSONArray jsonArray=jsonObj.getJSONArray("result");
                                        JSONObject j=jsonArray.getJSONObject(0);
                                        String result=j.getString("result");
                                        if(result.equalsIgnoreCase("success")){
                                            QueryPreferences.setcinnumber(getApplicationContext(), edit_CIN.getText().toString());
                                            obj.state_signup_page=false;
                                            finish();
                                            //Intent intent=LoginPage.newIntent(getApplicationContext());
                                            //startActivity(intent);
                                        }else if(result.equalsIgnoreCase("already exists")){
                                            Toast.makeText(getApplicationContext(),"Account already exists. Please Login.", Toast.LENGTH_SHORT).show();
                                        }
                                    }catch (JSONException e) {
                                        Log.e("JSONError", "JSON Parsing error");
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(),"IP Address error.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
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
                View view = null;
                view  = inflater.inflate(R.layout.dialog_ipaddress, null);
                final EditText text=(EditText)view.findViewById(R.id.editText_ip);
                String myString=QueryPreferences.getipaddress(SignUp.this);
                text.setText(myString);
                new AlertDialog.Builder(this)
                        .setTitle("Add IpAddress")
                        .setView(view)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener(){
                                    public void onClick(DialogInterface dialog, int which){
                                        QueryPreferences.setipaddress(SignUp.this,text.getText().toString());
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
        if(!(obj.state_signup_page))
            finish();
    }

    @Override
    public void onBackPressed(){
        obj.state_signup_page=false;
        super.onBackPressed();
        //Intent intent=new Intent(SignUp.this,LoginPage.class);
        //startActivity(intent);
    }

    public static Intent newIntent(Context packageContext) {
        Intent i = new Intent(packageContext, SignUp.class);
        return i;
    }
}
