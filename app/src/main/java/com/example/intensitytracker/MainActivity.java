package com.example.intensitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton, galleryButton, historyButton, videoButton;
    private ImageView logoImage;
    private static final int REQUEST_VIDEO_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoImage = findViewById(R.id.logoImageView);
        cameraButton = findViewById(R.id.buttonCamera);
        galleryButton = findViewById(R.id.buttonGallery);
        historyButton = findViewById(R.id.buttonHistory);
        videoButton = findViewById(R.id.buttonVideo);

        logoImage.setImageResource(R.drawable.menu_new);

        cameraButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImageProcessingActivity.class);
            intent.putExtra("source", "camera");
            startActivity(intent);
        });

        galleryButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ImageProcessingActivity.class);
            intent.putExtra("source", "gallery");
            startActivity(intent);
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        videoButton.setOnClickListener(v -> {
            Intent takeVideoIntent = new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
            takeVideoIntent.putExtra(android.provider.MediaStore.EXTRA_DURATION_LIMIT, 10); // Optional: 10s limit
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, VideoFrameSelectorActivity.class);
            intent.putExtra("videoUri", data.getData().toString());
            startActivity(intent);
        }
    }
}
