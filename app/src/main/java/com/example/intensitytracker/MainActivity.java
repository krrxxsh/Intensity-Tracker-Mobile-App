package com.example.intensitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton, galleryButton, historyButton;
    private ImageView logoImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoImage = findViewById(R.id.logoImageView);
        cameraButton = findViewById(R.id.buttonCamera);
        galleryButton = findViewById(R.id.buttonGallery);
        historyButton = findViewById(R.id.buttonHistory);

        logoImage.setImageResource(R.drawable.logo); // Make sure logo.png is in drawable

        cameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImageProcessingActivity.class);
            intent.putExtra("source", "camera"); // or "gallery"
            startActivity(intent);

        });

        galleryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImageProcessingActivity.class);
            intent.putExtra("source", "gallery"); // or "gallery"
            startActivity(intent);

        });


        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }
}
