package com.example.intensitytracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intensitytracker.database.HistoryDatabase;
import com.example.intensitytracker.database.ImageEntity;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
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

        Button btnExport = findViewById(R.id.btnExportExcel);
        btnExport.setOnClickListener(v -> {
            if (adapter == null) return;
            Set<Integer> selected = adapter.getSelectedPositions();
            if (selected.isEmpty()) {
                Toast.makeText(this, "Select at least one item", Toast.LENGTH_SHORT).show();
                return;
            }
            // Run export in background
            Executors.newSingleThreadExecutor().execute(() -> exportToExcel(selected));
        });

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

    private void exportToExcel(Set<Integer> selectedPositions) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Images");
            List<ImageEntity> historyList = adapter.getHistoryList();

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Image Name");
            headerRow.createCell(1).setCellValue("x");
            headerRow.createCell(2).setCellValue("y");
            headerRow.createCell(3).setCellValue("Width");
            headerRow.createCell(4).setCellValue("Height");
            headerRow.createCell(5).setCellValue("Max Intensity");
            headerRow.createCell(6).setCellValue("Min Intensity");
            headerRow.createCell(7).setCellValue("Avg Intensity");
            headerRow.createCell(8).setCellValue("Timestamp");

            int rowNum = 1;
            for (Integer pos : selectedPositions) {
                ImageEntity entity = historyList.get(pos);

                Row dataRow = sheet.createRow(rowNum++);
                dataRow.createCell(0).setCellValue(entity.getImageName());
                dataRow.createCell(1).setCellValue(entity.getX());
                dataRow.createCell(2).setCellValue(entity.getY());
                dataRow.createCell(3).setCellValue(entity.getWidth());
                dataRow.createCell(4).setCellValue(entity.getHeight());
                dataRow.createCell(5).setCellValue(entity.getMaxIntensity());
                dataRow.createCell(6).setCellValue(entity.getMinIntensity());
                dataRow.createCell(7).setCellValue(entity.getAvgIntensity());
                // Optional: format timestamp as readable date
                String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .format(new Date(entity.getTimestamp()));
                dataRow.createCell(8).setCellValue(dateStr);
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "history_export_" + timeStamp + ".xlsx";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();

            runOnUiThread(() -> showExportDialog(file));
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(this, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void showExportDialog(File file) {
        new AlertDialog.Builder(this)
                .setTitle("Exported")
                .setMessage("Excel file saved at:\n" + file.getAbsolutePath())
                .setPositiveButton("Open", (dialog, which) -> openExcelFile(file))
                .setNegativeButton("Share", (dialog, which) -> shareExcelFile(file))
                .setNeutralButton("Close", null)
                .show();
    }

    private void openExcelFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(intent, "Open Excel File"));
        } catch (Exception e) {
            Toast.makeText(this, "No app found to open Excel files", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareExcelFile(File file) {
        Uri fileUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.putExtra(Intent.EXTRA_STREAM, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share Excel File"));
    }
}