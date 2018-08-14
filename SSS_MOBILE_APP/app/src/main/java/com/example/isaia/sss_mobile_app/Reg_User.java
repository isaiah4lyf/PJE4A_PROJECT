package com.example.isaia.sss_mobile_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_User;
import android.view.LayoutInflater;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;

public class Reg_User extends AppCompatActivity {

    private EditText user_Name;
    private EditText Password;
    private EditText ConfirmPassword;
    private EditText Email;
    private EditText ConfirmEmail;

    private String User_Name_String;
    private String Password_String;
    private String ConfirmPassowrd_String;
    private String Email_String;
    private String ConfirmEmail_String;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__user);
        //Registering a user
        user_Name = (EditText) findViewById(R.id.User_Name);
        Password = (EditText) findViewById(R.id.Password);
        ConfirmPassword = (EditText) findViewById(R.id.ConfirmPassword);
        Email = (EditText) findViewById(R.id.Email);
        ConfirmEmail = (EditText) findViewById(R.id.ConfirmEmail);


        final Button reg = (Button) findViewById(R.id.Sign_Up);
        reg.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        User_Name_String = user_Name.getText().toString().replaceAll(" ","");
                        Password_String = Password.getText().toString().replaceAll(" ","");
                        ConfirmPassowrd_String = ConfirmPassword.getText().toString().replaceAll(" ","");
                        Email_String = Email.getText().toString().replaceAll(" ","");
                        ConfirmEmail_String = ConfirmEmail.getText().toString().replaceAll(" ","");

                        if(!User_Name_String.equals(""))
                        {
                            if(!Password_String.equals(""))
                            {
                                if(!ConfirmPassowrd_String.equals(""))
                                {
                                    if(!Email_String.equals(""))
                                    {
                                        if(!ConfirmEmail_String.equals(""))
                                        {
                                            if(Password_String.equals(ConfirmPassowrd_String))
                                            {

                                                if(Email_String.equals(ConfirmEmail_String))
                                                {
                                                    reg_us tast = new reg_us();
                                                    tast.execute();

                                                }
                                                else
                                                {
                                                    Toast.makeText(getApplicationContext(), "Email does not match!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(getApplicationContext(), "Password does not match!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "Confirm Password text field empty!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Password text field empty!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Confirm Email text field empty!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Email text field empty!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "User name text field empty!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
    private class reg_us extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
            progressDialog = ProgressDialog.show(Reg_User.this,"Please wait.","Connecting..!", true);

        }
        @Override
        protected String doInBackground(String... urls) {

            Insert_User insert_user = new Insert_User();
            return insert_user.Do_The_work(User_Name_String,Email_String,Password_String);

        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            try {
                progressDialog.dismiss();
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.images_vn_req_popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
                TextView startNow = (TextView)popupView.findViewById(R.id.startNow);
                startNow.setOnClickListener(new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        DBHelper mydb = new DBHelper(getApplicationContext());
                        String insert = String.valueOf(mydb.insert_Login_State(User_Name_String,Password_String));
                        Intent intent = null;
                        try {
                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Main_Activity_Images"));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }});
                TextView startLater = (TextView)popupView.findViewById(R.id.startLater);
                startLater.setOnClickListener(new Button.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                        Intent intent = null;
                        try {
                            intent = new Intent(getApplicationContext(), Login.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }});
                popupWindow.showAsDropDown(user_Name, -20, -50);
                popupWindow.setFocusable(true);
                popupWindow.update();

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
