package com.example.historyhike.controller;

import com.example.historyhike.model.Artefact;
import com.example.historyhike.model.Museum;

public class MuseumController {
    private Museum museum;

    public MuseumController() {
        museum = new Museum();
    }

    public void addArtefact(Artefact artefact) {
        museum.addArtefact(artefact);
    }

    public Artefact getLastArtefact() {
        if (!museum.getArtefacts().isEmpty()) {
            return museum.getArtefacts().get(museum.getArtefacts().size() - 1);
        }
        return null;
    }
}
