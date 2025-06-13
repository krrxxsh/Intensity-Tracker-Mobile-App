package com.example.intensitytracker.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ImageEntity.class}, version = 3, exportSchema = false)
public abstract class HistoryDatabase extends RoomDatabase {
    private static HistoryDatabase instance;

    public abstract HistoryDao historyDao();

    public static synchronized HistoryDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            HistoryDatabase.class, "history_database")
                    .fallbackToDestructiveMigration() // <-- Add this
                    .build();
        }
        return instance;
    }
}
