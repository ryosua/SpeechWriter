package edu.psu.rcy5017.publicspeakingassistant.model;

import java.util.ArrayList;

public class NoteCard {
	
	private String title = "";
	private final ArrayList<String> notes = new ArrayList<String>();
	
	public NoteCard(String title) {
		this.setTitle(title);
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

	public void addNote(int position, String noteText) {
		notes.add(position, noteText);
	}
	
	public void addNote(String noteText) {
		notes.add(noteText);
	}
}
