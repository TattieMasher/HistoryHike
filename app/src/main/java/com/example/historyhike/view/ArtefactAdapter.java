package com.example.historyhike.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.historyhike.R;
import com.example.historyhike.model.Artefact;

import java.util.List;

public class ArtefactAdapter extends RecyclerView.Adapter<ArtefactAdapter.ArtefactViewHolder> {

    private List<Artefact> artefacts;
    private Context context;

    public ArtefactAdapter(List<Artefact> artefacts, Context context) {
        this.artefacts = artefacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ArtefactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artefact, parent, false);
        return new ArtefactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtefactViewHolder holder, int position) {
        Artefact artefact = artefacts.get(position);
        holder.textViewArtefactName.setText(artefact.getName());
        Glide.with(context).load(artefact.getImageUrl()).into(holder.imageViewArtefact);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ArtefactDetailActivity.class);
            intent.putExtra("description", artefact.getDescription());
            intent.putExtra("imageUrl", artefact.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return artefacts.size();
    }

    public static class ArtefactViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewArtefact;
        TextView textViewArtefactName;

        public ArtefactViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewArtefact = itemView.findViewById(R.id.imageViewArtefact);
            textViewArtefactName = itemView.findViewById(R.id.textViewArtefactName);
        }
    }
}
