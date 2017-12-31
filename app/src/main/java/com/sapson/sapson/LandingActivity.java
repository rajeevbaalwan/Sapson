package com.sapson.sapson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class LandingActivity extends AppCompatActivity {
    private ImageView splash_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        splash_image = (ImageView) findViewById(R.id.splash_view);
        
    }
}
