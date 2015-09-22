package edu.psu.rcy5017.speechwriter.model;

/**
 * A model class representing a speech.
 * @author Ryan Yosua
 *
 */
public class Speech {
    
    private final long id;
    
    private String title;
    private int order;
    
    public Speech(long id, String title) {
        this.id = id;
        this.title = title;
        this.order = (int)id;
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

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return title;
    }
    
} 