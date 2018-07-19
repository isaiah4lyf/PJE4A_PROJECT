package com.example.isaia.sss_mobile_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        try {

                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Camera_Activity"));
                            startActivity(intent);

                        } catch (ClassNotFoundException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        final Button register = (Button) findViewById(R.id.sign_up);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = null;
                        try {

                            intent = new Intent(getApplicationContext(), Class.forName("com.example.isaia.sss_mobile_app.Camera_Activity"));
                            startActivity(intent);

                        } catch (ClassNotFoundException e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
