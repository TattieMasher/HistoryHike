package com.example.historyhike.model;

public class User {
    private int id;
    private String email;
    private String firstName;
    private String surname;

    public User(int id, String email, String firstName, String surname) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.surname = surname;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }
}
