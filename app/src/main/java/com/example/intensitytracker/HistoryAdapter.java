package com.example.intensitytracker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intensitytracker.database.ImageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ImageEntity> historyList;

    public HistoryAdapter(List<ImageEntity> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageEntity imageEntity = historyList.get(position);

        // ✅ Correct image display
        Bitmap bitmap = imageEntity.getImageBitmap();
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        }

        // Intensity
        holder.intensity.setText("Intensity: " + String.format(Locale.US, "%.2f", imageEntity.getIntensityValue()));

        // Timestamp
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US)
                .format(new Date(imageEntity.getTimestamp()));
        holder.date.setText("Date: " + formattedDate);
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView intensity;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            intensity = itemView.findViewById(R.id.intensity);
            date = itemView.findViewById(R.id.date);
        }
    }
}
