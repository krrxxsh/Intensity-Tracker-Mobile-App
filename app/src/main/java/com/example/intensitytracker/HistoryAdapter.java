package com.example.intensitytracker;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.intensitytracker.database.ImageEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<ImageEntity> historyList;
    private Set<Integer> selectedPositions = new HashSet<>();

    public Set<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public List<ImageEntity> getHistoryList() {
        return historyList;
    }

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

        // Image
        Bitmap bitmap = imageEntity.getImageBitmap();
        if (bitmap != null) {
            holder.image.setImageBitmap(bitmap);
        }

        // Intensity and date
        holder.intensity.setText("Intensity: " + String.format(Locale.US, "%.2f", imageEntity.getAvgIntensity()));
        String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US)
                .format(new Date(imageEntity.getTimestamp()));
        holder.date.setText("Date: " + formattedDate);

        // Checkbox
        holder.checkBox.setOnCheckedChangeListener(null); // prevent unwanted callbacks
        holder.checkBox.setChecked(selectedPositions.contains(position));
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;
            if (isChecked) {
                selectedPositions.add(adapterPosition);
            } else {
                selectedPositions.remove(adapterPosition);
            }
        });

        // NEW: Thumbnail click listener
        holder.image.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, ImageProcessingActivity.class);
            intent.putExtra("fromHistory", true);
            intent.putExtra("imageId", imageEntity.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView intensity;
        TextView date;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            intensity = itemView.findViewById(R.id.intensity);
            date = itemView.findViewById(R.id.date);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}