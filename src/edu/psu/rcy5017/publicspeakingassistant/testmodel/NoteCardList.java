package edu.psu.rcy5017.publicspeakingassistant.testmodel;

import java.util.ArrayList;

/**
 * A list of note cards for a speech. 
 * @author Ryan Yosua
 *
 */
public abstract class NoteCardList {
	
	private final ArrayList<NoteCard> list = new ArrayList<NoteCard>();
	
	public NoteCardList() {
		
	}
	
	public void addNoteCard(NoteCard noteCard) {
		list.add(noteCard);
	}
	
	public void addNoteCard(int index, NoteCard noteCard) {
		list.add(index, noteCard);
	}
	
	public boolean removeNoteCard(NoteCard noteCard) {
		return list.remove(noteCard);
	}
	
	public Object removeNoteCard(int index) {
		return list.remove(index);
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
