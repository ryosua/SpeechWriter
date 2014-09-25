package edu.psu.rcy5017.publicspeakingassistant.model;

/**
 * A model class representing a note.
 * @author Ryan Yosua
 *
 */
public class Note {

	private final long id;
    private final long speechID;
    
    private String text;
   
    public Note(long id, long speechID, String text) {
		this.id = id;
		this.speechID = speechID;
		this.text = text;
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

    public long getSpeechID() {
		return speechID;
	}

	// Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return text;
    }
	
}