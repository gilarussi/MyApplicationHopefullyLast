package com.example.adminapp;

public class Person {
    public String email;
    public Note note;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Person(String email, Note note) {
        this.email = email;
        this.note = note;
    }
}
