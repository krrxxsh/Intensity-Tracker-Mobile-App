package com.example.intensitytracker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.example.intensitytracker.database.HistoryDatabase;
import com.example.intensitytracker.database.ImageEntity;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageProcessingActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView resultTextView;
    private HistoryDatabase database;

    static {
        if (!OpenCVLoader.initDebug()) {
            Log.e("OpenCV", "Unable to load OpenCV");
        } else {
            Log.d("OpenCV", "OpenCV loaded successfully");
        }
    }

    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    launchCropper(imageUri);
                }
            });

    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bitmap bitmap = (Bitmap) result.getData().getExtras().get("data");
                    Uri tempUri = getImageUriFromBitmap(bitmap);
                    launchCropper(tempUri);
                }
            });

    private final ActivityResultLauncher<CropImageContractOptions> cropImageLauncher =
            registerForActivityResult(new CropImageContract(), result -> {
                if (result.isSuccessful()) {
                    Uri croppedUri = result.getUriContent();
                    try {
                        Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedUri);
                        processImage(selectedBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed to load cropped image", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cropping failed", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_processing);

        imageView = findViewById(R.id.processedImageView);
        resultTextView = findViewById(R.id.intensityValueTextView);
        database = HistoryDatabase.getInstance(this);

        String source = getIntent().getStringExtra("source");
        if ("camera".equals(source)) {
            openCamera();
        } else if ("gallery".equals(source)) {
            openGallery();
        }

        Button saveButton = findViewById(R.id.saveButton);
        Button historyButton = findViewById(R.id.historyButton);

        saveButton.setOnClickListener(v -> {
            Toast.makeText(this, "Image already saved after processing", Toast.LENGTH_SHORT).show();
        });

        historyButton.setOnClickListener(v -> {
            Intent intent = new Intent(ImageProcessingActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(intent);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void launchCropper(Uri imageUri) {
        CropImageOptions cropOptions = new CropImageOptions();
        cropOptions.guidelines = CropImageView.Guidelines.ON;

        CropImageContractOptions options = new CropImageContractOptions(imageUri, cropOptions);
        cropImageLauncher.launch(options);
    }

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "CapturedImage", null);
        return Uri.parse(path);
    }

    private void processImage(Bitmap bitmap) {
        if (bitmap == null) return;

        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Mat mat = new Mat();
        Utils.bitmapToMat(mutableBitmap, mat);

        Mat gray = new Mat();
        Imgproc.cvtColor(mat, gray, Imgproc.COLOR_BGR2GRAY);

        Core.MinMaxLocResult mmr = Core.minMaxLoc(gray);
        Point maxLoc = mmr.maxLoc;

        int roiSize = 6;
        int startX = (int) Math.max(0, maxLoc.x - roiSize / 2);
        int startY = (int) Math.max(0, maxLoc.y - roiSize / 2);
        int width = (int) Math.min(roiSize, gray.cols() - startX);
        int height = (int) Math.min(roiSize, gray.rows() - startY);

        Rect roiRect = new Rect(startX, startY, width, height);
        Mat roi = new Mat(gray, roiRect);

        Scalar meanScalar = Core.mean(roi);
        double meanIntensity = meanScalar.val[0];

        Core.MinMaxLocResult roiMMR = Core.minMaxLoc(roi);
        double minIntensity = roiMMR.minVal;
        double maxIntensity = roiMMR.maxVal;

        Canvas canvas = new Canvas(mutableBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawRect(startX, startY, startX + width, startY + height, paint);

        imageView.setImageBitmap(mutableBitmap);

        String result = String.format(
                "Mean Intensity: %.2f\n" +
                        "Min: %.2f   Max: %.2f\n" +
                        "Location: X = %d, Y = %d\n" +
                        "ROI Size: %d x %d (%.0f pixels)",
                meanIntensity, minIntensity, maxIntensity,
                startX, startY, width, height, (double) width * height
        );

        resultTextView.setText(result);

        // Save to Room DB in background thread
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mutableBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] imageBytes = stream.toByteArray();

        ImageEntity entity = new ImageEntity(imageBytes, meanIntensity, System.currentTimeMillis());
        new Thread(() -> {
            database.historyDao().insert(entity);
        }).start();
    }
}
