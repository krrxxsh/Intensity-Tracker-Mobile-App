package com.example.intensitytracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VideoFrameSelectorActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer exoPlayer;
    private Button btnCaptureFrame;
    private Uri videoUri;

    // ✅ Declare cropper launcher
    private final ActivityResultLauncher<CropImageContractOptions> cropImageLauncher =
            registerForActivityResult(new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    Uri croppedUri = result.getUriContent();

                    // After cropping, launch ImageProcessingActivity
                    Intent intent = new Intent(VideoFrameSelectorActivity.this, ImageProcessingActivity.class);
                    intent.putExtra("frameUri", croppedUri.toString());
                    startActivity(intent);
                    finish();

                } else {
                    showToast("Cropping failed");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_frame_selector);

        playerView = findViewById(R.id.playerView);
        btnCaptureFrame = findViewById(R.id.btnCaptureFrame);

        String videoUriStr = getIntent().getStringExtra("videoUri");
        if (videoUriStr == null) {
            finish();
            return;
        }

        videoUri = Uri.parse(videoUriStr);

        // Initialize ExoPlayer
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();

        btnCaptureFrame.setOnClickListener(v -> captureFrame());
    }

    private void captureFrame() {
        long currentPosMs = exoPlayer.getCurrentPosition();

        new Thread(() -> {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(this, videoUri);
                Bitmap frameBitmap = retriever.getFrameAtTime(currentPosMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
                retriever.release();

                if (frameBitmap != null) {
                    File imageFile = saveBitmapToFile(frameBitmap);
                    if (imageFile != null) {
                        Uri imageUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", imageFile);

                        // ✅ Launch cropper on UI thread
                        runOnUiThread(() -> launchCropper(imageUri));

                    } else {
                        showToast("Failed to save captured frame");
                    }
                } else {
                    showToast("Captured frame is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Error capturing frame: " + e.getMessage());
            } finally {
                try {
                    retriever.release();
                } catch (Exception ignored) {}
            }
        }).start();
    }

    private void launchCropper(Uri imageUri) {
        CropImageOptions cropOptions = new CropImageOptions();
        cropOptions.guidelines = CropImageView.Guidelines.ON;

        CropImageContractOptions options = new CropImageContractOptions(imageUri, cropOptions);
        cropImageLauncher.launch(options);
    }

    private File saveBitmapToFile(Bitmap bitmap) throws IOException {
        File outputDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "captured_frames");
        if (!outputDir.exists()) outputDir.mkdirs();

        File outputFile = new File(outputDir, "frame_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = new FileOutputStream(outputFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.flush();
        out.close();
        return outputFile;
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(VideoFrameSelectorActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
}
