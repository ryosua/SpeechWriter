package edu.psu.rcy5017.publicspeakingassistant.model;

import java.util.ArrayList;

public abstract class NoteList {
	
	private final ArrayList<NoteCard> list = new ArrayList<NoteCard>();
	
	public NoteList() {
		
	}
	
	// Getters
	public ArrayList<NoteCard> getList() {
		return list;
	}
	
	/**
	 * Creates the note cards and adds them to the list.
	 */
	public abstract void populateList();
	
}
