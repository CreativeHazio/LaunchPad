package com.creativehazio.launchpad.model;

import java.util.ArrayList;

public class CloudNote {
    private String id;
    private ArrayList<Note> notes;

    public CloudNote() {
    }

    public CloudNote(String id, ArrayList<Note> notes) {
        this.id = id;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public ArrayList<Note> getNotes() {
        return notes;
    }
}
