package com.example.intensitytracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insert(ImageEntity imageEntity);

    @Query("SELECT * FROM image_table ORDER BY timestamp DESC")
    List<ImageEntity> getAllImages();

    @Query("SELECT * FROM image_table WHERE id = :imageId")
    ImageEntity getImageById(int imageId);
}
