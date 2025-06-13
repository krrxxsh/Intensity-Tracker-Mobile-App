package com.example.intensitytracker.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "image_table")
public class ImageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "image_name")
    private String imageName;

    @ColumnInfo(name = "x")
    private int x;

    @ColumnInfo(name = "y")
    private int y;

    @ColumnInfo(name = "width")
    private int width;

    @ColumnInfo(name = "height")
    private int height;

    @ColumnInfo(name = "max_intensity")
    private float maxIntensity;

    @ColumnInfo(name = "min_intensity")
    private float minIntensity;

    @ColumnInfo(name = "avg_intensity")
    private float avgIntensity;

    @ColumnInfo(name = "image_bytes")
    private byte[] imageBytes;

    @ColumnInfo(name = "timestamp")
    private long timestamp;



    // Constructor
    public ImageEntity(String imageName, int x, int y, int width, int height,
                       float maxIntensity, float minIntensity, float avgIntensity,
                       byte[] imageBytes, long timestamp) {
        this.imageName = imageName;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxIntensity = maxIntensity;
        this.minIntensity = minIntensity;
        this.avgIntensity = avgIntensity;
        this.imageBytes = imageBytes;
        this.timestamp = timestamp;
    }

    public Bitmap getImageBitmap() {
        if (imageBytes == null) return null;
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    // --- Room requires getter/setter for id! ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    // Getters and setters...
    public String getImageName() { return imageName; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public float getMaxIntensity() { return maxIntensity; }
    public float getMinIntensity() { return minIntensity; }
    public float getAvgIntensity() { return avgIntensity; }
    public byte[] getImageBytes() { return imageBytes; }
    public long getTimestamp() { return timestamp; }
}