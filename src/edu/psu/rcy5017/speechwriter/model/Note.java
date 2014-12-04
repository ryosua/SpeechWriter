package edu.psu.rcy5017.speechwriter.model;

/**
 * A model class representing a note.
 * @author Ryan Yosua
 *
 */
public class Note {

    private final long id;
    private final long noteCardID;
    
    private String text;
    private int order;
   
    public Note(long id, long noteCardID, String text) {
        this.id = id;
        this.noteCardID = noteCardID;
        this.text = text;
        this.order = (int)id;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getNoteCardID() {
        return noteCardID;
    }
    
    public int getOrder() {
        return order;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return text;
    }
    
}