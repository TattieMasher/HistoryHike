package com.example.historyhike.view;

import android.content.DialogInterface;
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

import com.bumptech.glide.Glide;
import com.example.historyhike.R;
import com.google.android.material.button.MaterialButton;

public class ObjectiveCompleteDialogFragment extends DialogFragment {

    private DialogInterface.OnDismissListener onDismissListener; // Listener for dismiss event

    // Now using imageURL string
    public static ObjectiveCompleteDialogFragment newInstance(String title, String description, String imageURL) {
        ObjectiveCompleteDialogFragment frag = new ObjectiveCompleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putString("imageURL", imageURL);
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
        String imageURL = getArguments().getString("imageURL"); // Retrieve the image URL

        TextView titleView = view.findViewById(R.id.obj_dialog_title);
        TextView descriptionView = view.findViewById(R.id.obj_dialog_description);
        ImageView imageView = view.findViewById(R.id.obj_dialog_image);
        MaterialButton closeButton = view.findViewById(R.id.close_objective_dialog);

        titleView.setText(title);
        descriptionView.setText(description);
        Glide.with(this).load(imageURL).into(imageView); // Load the image with Glide

        closeButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);  // Trigger the custom dismiss listener
        }
    }

    // Method to set the dismiss listener
    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }
}