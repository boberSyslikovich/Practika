package com.pupukaka.practika;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class TaskMessageAdapter extends ArrayAdapter<TaskMessage> {

    public TaskMessageAdapter(@NonNull Context context, int resource, List<TaskMessage> tasks) {
        super(context, resource, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            convertView = ((Activity)getContext()).getLayoutInflater()
                    .inflate(R.layout.task_layout, parent, false);
        }

        View view;
        ImageView blueprintImageView = convertView.findViewById(R.id.blueprintImageView);
//        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView numberOfWorkPlaceTextView = convertView.findViewById(R.id.numberOfWorkPlaceTextView);
        TextView timeTextView = convertView.findViewById(R.id.timeTextView);
//        ImageView performedImageView = convertView.findViewById(R.id.performedImageView);
//        ImageView cancelImageView = convertView.findViewById(R.id.cancelImageView);

        TaskMessage tasks = getItem(position);

        Glide.with(blueprintImageView.getContext())
                .load(tasks.getImageUrl())
                .into(blueprintImageView);
//        descriptionTextView.setText(tasks.getNotes());
        numberOfWorkPlaceTextView.setText(tasks.getWorkplaceNumber());
        timeTextView.setText(tasks.getTime());


        return convertView;
    }
}
