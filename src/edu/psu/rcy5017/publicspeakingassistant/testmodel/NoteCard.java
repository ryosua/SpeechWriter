package edu.psu.rcy5017.publicspeakingassistant.testmodel;

import java.util.ArrayList;

/**
 * A note card for a speech. A note card may or may not have a title, and contains note to be displayed as a bulleted list.
 * @author ryosua
 *
 */
public class NoteCard {
	
	private String title = "";
	private final ArrayList<String> notes = new ArrayList<String>();
	
	public NoteCard(String title) {
		this.setTitle(title);
	}
	
	public void addNote(int position, String noteText) {
		notes.add(position, noteText);
	}
	
	public void addNote(String noteText) {
		notes.add(noteText);
	}
	
	public boolean removeNote(String note) {
		return notes.remove(notes);
	}
	
	public Object removeNote(int index) {
		return notes.remove(index);
	}
	
	// Getters
	public String getTitle() {
		return title;
	}
	public ArrayList<String> getNotes() {
		return notes;
	}
	
	// Setters
	public void setTitle(String title) {
		this.title = title;
	}
}
