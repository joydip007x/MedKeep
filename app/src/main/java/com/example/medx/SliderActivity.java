package com.example.medx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.medx.databinding.SliderBinding;

import java.util.ArrayList;

public class SliderActivity extends AppCompatActivity {
    private Button BsignUp,BsignIn;


    private ImageSlider imageSlider;
    public static Uri getUrl(int res){
        return Uri.parse("android.resource://com.example.medx/" + res);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slider);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageSlider = findViewById(R.id.imageSlider2);

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(String.valueOf(getUrl(R.drawable.slide1)), ScaleTypes.FIT));
        slideModels.add(new SlideModel( String.valueOf(getUrl(R.drawable.slide2)), ScaleTypes.FIT));
        slideModels.add(new SlideModel( String.valueOf(getUrl(R.drawable.slide3)), ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);


        BsignIn = findViewById(R.id.btn_signin);
        BsignUp = findViewById(R.id.btn_signup);

        BsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), SignUpActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });
        BsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getApplicationContext(), SignInActivity.class);
                a.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(a);
            }
        });


    }
}