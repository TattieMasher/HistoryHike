package com.example.historyhike.model;

import java.util.ArrayList;

public class Museum {
    private ArrayList<Artefact> artefacts;

    public Museum() {
        this.artefacts = null;
    }

    public Museum(ArrayList<Artefact> artefacts) {
        this.artefacts = artefacts;
    }

    public void addArtefact(Artefact artefact) {
        if (!this.artefacts.contains(artefact)) {
            this.artefacts.add(artefact);
        }
    }
}