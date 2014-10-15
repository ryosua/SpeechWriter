package edu.psu.rcy5017.publicspeakingassistant.model;

public class SpeechRecording {
    
    private final long id;
    private final long speechID;
    
    private String title;
    private String file;
    
    public SpeechRecording(long id, String title, long speechID) {
        this.id = id;
        this.title = title;
        this.file = "/" + this.id;
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
    
    public String getFile() {
        return file;
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