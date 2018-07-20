package com.example.isaia.sss_mobile_app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Insert_User;
import com.example.isaia.sss_mobile_app.SSS_CLIENT_FUNCTIONS.Pred_User;

public class Reg_User extends AppCompatActivity {
    private TextView txvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg__user);

        //Registering a user
        txvResult = (TextView) findViewById(R.id.textView);

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                //txvResult.setText("Hello");

                try {
                    reg_us tast = new reg_us();
                    tast.execute();

                }
                catch (Exception ex)
                {
                    txvResult.setText(ex.toString());
                }
            }
        });

        thread.start();

    }
    private class reg_us extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            //if you want, start progress dialog here
        }
        @Override
        protected String doInBackground(String... urls) {

            Insert_User insert_user = new Insert_User();
            return insert_user.Do_The_work("Isaiah");
        }
        @Override
        protected void onPostExecute(String result) {
            //if you started progress dialog dismiss it here
            txvResult.setText(result);
        }
    }

}
