package com.example.myapplicationhopefullylast;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return email+" "+note.toString();
    }
}
