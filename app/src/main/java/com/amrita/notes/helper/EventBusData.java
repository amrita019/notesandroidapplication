package com.amrita.notes.helper;

import com.amrita.notes.home.model.NoteListDetails;

public class EventBusData {

    private NoteListDetails noteListDetails;
    private boolean isEdit;

    public EventBusData(NoteListDetails noteListDetails, boolean isEdit){
        this.noteListDetails = noteListDetails;
        this.isEdit = isEdit;
    }

    public NoteListDetails getNoteListDetails(){
        return noteListDetails;
    }

    public boolean isEdit() {
        return isEdit;
    }
}
