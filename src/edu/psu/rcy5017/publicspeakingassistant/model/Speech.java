package edu.psu.rcy5017.publicspeakingassistant.model;

/**
 * A model class representing a speech.
 * @author Ryan Yosua
 *
 */
public class Speech {
    
    private final long id;
    
    private String title;
    
    public Speech(long id, String title) {
        this.id = id;
        this.title = title;
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

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return title;
    }
    
} 