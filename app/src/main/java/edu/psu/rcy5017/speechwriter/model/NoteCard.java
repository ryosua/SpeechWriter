package edu.psu.rcy5017.speechwriter.model;

/**
 * A model class representing a note card.
 * @author Ryan Yosua
 *
 */
public class NoteCard {
    
    private final long id;
    private final long speechID;
    
    private String title;
    private int order;
    
    public NoteCard(long id, String title, long speechID) {
        this.id = id;
        this.title = title;
        this.order = (int)id;
        this.speechID = speechID;
    }

    public long getId() {
        return id;
    }
  
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getOrder() {
        return order;
    }
    
    public void setOrder(int order) {
        this.order = order;
    }

    public long getSpeechID() {
        return speechID;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return title;
    }
    
}