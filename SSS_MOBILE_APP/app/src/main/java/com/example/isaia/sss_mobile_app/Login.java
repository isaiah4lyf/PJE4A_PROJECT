package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.isaia.sss_mobile_app.Database.DBHelper;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    private EditText User_Name;
    private EditText Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        User_Name = (EditText)findViewById(R.id.User_Name);
        Password = (EditText)findViewById(R.id.Password);
        final Button Reg_User = (Button) findViewById(R.id.Reg_User);

        Reg_User.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Intent intent = null;
                        try {
                            DBHelper mydb = new DBHelper(getApplicationContext());
                            String insert = String.valueOf(mydb.insert_Login_State(User_Name.getText().toString().replaceAll(" ",""),Password.getText().toString()));
                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Capture_Images_Auto"));
                            startActivity(intent);

                        } catch (ClassNotFoundException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );
        final Button Login = (Button) findViewById(R.id.Login);
        Login.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Intent intent = null;
                        try {
                            DBHelper mydb = new DBHelper(getApplicationContext());
                            String insert = String.valueOf(mydb.insert_Login_State(User_Name.getText().toString().replaceAll(" ",""),Password.getText().toString()));
                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Predict_User"));
                            startActivity(intent);


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );
    }
}
