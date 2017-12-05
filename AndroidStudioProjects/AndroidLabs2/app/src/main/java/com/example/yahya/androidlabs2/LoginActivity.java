package com.example.yahya.androidlabs2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onCreate()");

        // use EditText instead of TextView because that is where you type
        final EditText tv = (EditText)findViewById(R.id.editText1);
//        final TextView tv = (TextView)findViewById(R.id.textView);
        final SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        Button bLogin = (Button)findViewById(R.id.button_login);
        bLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email = tv.getText().toString();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("emailKey", email);
                editor.apply();

                Intent startIntent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(startIntent);
            }
        });
    }

    protected void onStart(){
        super.onStart();
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onStart()");
        SharedPreferences preferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String email = preferences.getString("emailKey", null);
//        TextView tv = (TextView)findViewById(R.id.textView);
        // use EditText instead of TextView because that is where you type
        final EditText tv = (EditText)findViewById(R.id.editText1);
        tv.setText(email);
    }

    protected void onRestart(){
        super.onRestart();
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onRestart()");
    }

    protected void onResume(){
        super.onResume();
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onPause(){
        super.onPause();
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop(){
        super.onStop();
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy(){
        super.onDestroy();
        final String ACTIVITY_NAME = "LoginActivity";
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
