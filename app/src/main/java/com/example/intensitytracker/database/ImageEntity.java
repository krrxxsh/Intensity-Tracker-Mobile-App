package com.example.intensitytracker.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table")
public class ImageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    @ColumnInfo(name = "mean_intensity")
    private double meanIntensity;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    // Constructor
    public ImageEntity(byte[] image, double meanIntensity, long timestamp) {
        this.image = image;
        this.meanIntensity = meanIntensity;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public double getMeanIntensity() {
        return meanIntensity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setMeanIntensity(double meanIntensity) {
        this.meanIntensity = meanIntensity;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // --- Custom Helper Methods ---

    // Convert image byte array to Bitmap (for display)
    public Bitmap getImageBitmap() {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    // Wrapper for clarity where "intensityValue" is expected
    public double getIntensityValue() {
        return meanIntensity;
    }
}
