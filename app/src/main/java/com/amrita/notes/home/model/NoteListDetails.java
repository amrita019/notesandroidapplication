package com.amrita.notes.home.model;

public class NoteListDetails {

    private String id;
    private String name;
    private String description;
    private String important;

    public NoteListDetails(String id, String name, String description, String important){
        this.id = id;
        this.name = name;
        this.description = description;
        this.important = important;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String isImportant() {
        return important;
    }
}
