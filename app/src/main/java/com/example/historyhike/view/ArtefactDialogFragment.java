package com.example.historyhike.view;

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

public class ArtefactDialogFragment extends DialogFragment {
    public static ArtefactDialogFragment newInstance(String title, String description, String imageUrl) {
        ArtefactDialogFragment frag = new ArtefactDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
        args.putString("imageURL", imageUrl);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artefact_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString("title", "Objective Completed");
        String description = getArguments().getString("description", "");
        String imageURL = getArguments().getString("imageURL"); // Retrieve the image URL

        TextView titleView = view.findViewById(R.id.artefact_title);
        TextView descriptionView = view.findViewById(R.id.artefact_description);
        ImageView imageView = view.findViewById(R.id.artefact_image);
        MaterialButton closeButton = view.findViewById(R.id.close_artefact_dialog);

        titleView.setText(title);
        descriptionView.setText(description);
        Glide.with(this).load(imageURL).into(imageView); // Load the image with Glide

        closeButton.setOnClickListener(v -> dismiss());
    }
}
