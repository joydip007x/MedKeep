package com.example.medx;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class FrontPageActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);


        Thread front_page = new Thread()
        {
            @Override
            public void run()
            {
                try{
                    sleep(1800);
                    Intent i= new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        };
        front_page.start();
    }
}
