package com.project.android.studentshelper;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.project.android.studentshelper.Networking.AsyncCallback;
import com.project.android.studentshelper.Networking.HttpGetAsync;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Utteya on 3/29/2018.
 */

public class ChangePasswordpre extends AppCompatActivity {
    private MaterialEditText MET_Newpassword;
    private MaterialEditText MET_Confirm;
    private Button btn_Submit;
    private ProgressDialog load;

    SingletonClass obj=SingletonClass.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        obj.state_forgotpassword_page=true;

        MET_Newpassword =(MaterialEditText)findViewById(R.id.newpassword);
        MET_Confirm =(MaterialEditText)findViewById(R.id.confirmpassword);

        btn_Submit =(Button)findViewById(R.id.button_submit);
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MET_Newpassword.getText().toString().isEmpty() || MET_Confirm.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "A field cannot be blank.", Toast.LENGTH_SHORT).show();
                }else if(!MET_Newpassword.getText().toString().equalsIgnoreCase(MET_Confirm.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Passwords do not match.",Toast.LENGTH_SHORT).show();
                }else if(MET_Newpassword.getText().toString().contains(" ") || MET_Confirm.getText().toString().contains(" ")){
                    Toast.makeText(getApplicationContext(),"No spaces allowed",Toast.LENGTH_SHORT).show();
                }
                else{
                    String uri= Uri.parse("http://"+QueryPreferences.getipaddress(getApplicationContext())+"/myapp/reset.php?").buildUpon()
                            .appendQueryParameter("cin",getIntent().getStringExtra("cin"))
                            .appendQueryParameter("password", MET_Newpassword.getText().toString())
                            .build().toString();
                    new HttpGetAsync(uri, new AsyncCallback() {
                        @Override
                        public void onStart() {
                            load=ProgressDialog.show(ChangePasswordpre.this,"","loading");
                        }

                        @Override
                        public void onFinish(String jsonReceive) {
                            load.cancel();
                            if(jsonReceive.contains("success")){
                                Toast.makeText(getApplicationContext(),"Password Successfully Reset.",Toast.LENGTH_SHORT).show();
                                Thread timerThread = new Thread(){
                                    public void run(){
                                        try{
                                            sleep(1500);
                                        }catch(InterruptedException e){
                                            e.printStackTrace();
                                        }finally {
                                            obj.state_forgotpassword_page=false;
                                            finish();
                                            //Intent intent=new Intent(getApplicationContext(),LoginPage.class);
                                            //startActivity(intent);
                                        }
                                    }
                                };
                                timerThread.start();
                            }
                        }
                    }).execute();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!(obj.state_forgotpassword_page))
            finish();
    }

    @Override
    public void onBackPressed(){
        obj.state_forgotpassword_page=false;
        super.onBackPressed();
        //Intent intent=new Intent(SignUp.this,LoginPage.class);
        //startActivity(intent);
    }
}
