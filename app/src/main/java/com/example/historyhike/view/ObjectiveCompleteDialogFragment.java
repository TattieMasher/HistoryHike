package com.example.historyhike.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.historyhike.R;
import com.google.android.material.button.MaterialButton;

public class ObjectiveCompleteDialogFragment extends DialogFragment {

    public static ObjectiveCompleteDialogFragment newInstance(String title, String description) {
        ObjectiveCompleteDialogFragment frag = new ObjectiveCompleteDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("description", description);
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

        TextView titleView = view.findViewById(R.id.obj_dialog_title);
        TextView descriptionView = view.findViewById(R.id.obj_dialog_description);
        MaterialButton closeButton = view.findViewById(R.id.close_objective_dialog);

        titleView.setText(title);
        descriptionView.setText(description);

        closeButton.setOnClickListener(v -> dismiss());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}