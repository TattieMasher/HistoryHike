package com.example.historyhike.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.historyhike.R;
import com.google.android.material.button.MaterialButton;

public class ObjectiveCompleteDialogFragment extends DialogFragment {

    // Update newInstance to include an imageResourceId parameter
    public static ObjectiveCompleteDialogFragment newInstance(String title, String description, int imageResourceId) {
        ObjectiveCompleteDialogFragment frag = new ObjectiveCompleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putInt("imageResourceId", imageResourceId); // Add this line
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_objective_completion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "Objective Completed");
        String description = getArguments().getString("description", "");
        int imageResourceId = getArguments().getInt("imageResourceId");

        // Populate UI elements with the supplied data
        TextView titleView = view.findViewById(R.id.obj_dialog_title);
        TextView descriptionView = view.findViewById(R.id.obj_dialog_description);
        ImageView imageView = view.findViewById(R.id.obj_dialog_image);
        MaterialButton closeButton = view.findViewById(R.id.close_objective_dialog);

        titleView.setText(title);
        descriptionView.setText(description);
        imageView.setImageResource(imageResourceId); // Set the image resource

        closeButton.setOnClickListener(v -> dismiss());
    }
}
