package com.historyhike.historyhike_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "artefact_user")
public class ArtefactUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "artefact_id")
    private Artefact artefact;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "collected_date")
    private LocalDateTime collectedDate;

    public ArtefactUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Artefact getArtefact() {
        return artefact;
    }

    public void setArtefact(Artefact artefact) {
        this.artefact = artefact;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCollectedDate() {
        return collectedDate;
    }

    public void setCollectedDate(LocalDateTime collectedDate) {
        this.collectedDate = collectedDate;
    }
}