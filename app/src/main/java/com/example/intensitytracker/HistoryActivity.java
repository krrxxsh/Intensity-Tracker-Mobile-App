package com.example.intensitytracker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intensitytracker.database.HistoryDatabase;
import com.example.intensitytracker.database.ImageEntity;

import java.util.List;
import java.util.concurrent.Executors;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch history in background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            List<ImageEntity> historyList = HistoryDatabase.getInstance(this).historyDao().getAllImages();

            runOnUiThread(() -> {
                if (historyList.isEmpty()) {
                    Toast.makeText(this, "No history found", Toast.LENGTH_SHORT).show();
                }
                adapter = new HistoryAdapter(historyList);
                historyRecyclerView.setAdapter(adapter);
            });
        });
    }
}
