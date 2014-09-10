package edu.psu.rcy5017.publicspeakingassistant.model;

import java.util.ArrayList;

import android.util.Log;

public class TestNoteList extends NoteList {
	
	private static final String TAG = "TestNoteList";

	@Override
	public void populateList() {
		ArrayList<NoteCard> list = getList();
		
		final NoteCard testNoteCard1 = new NoteCard("Inroduction");
		testNoteCard1.addNote("Once upon a time...");
		testNoteCard1.addNote("Interesting fact 1");
		testNoteCard1.addNote("Interesting fact 2");
		
		final NoteCard testNoteCard2 = new NoteCard("Middle");
		testNoteCard2.addNote("Supporting evidence 1");
		testNoteCard2.addNote("Supporting evidence 2");
		testNoteCard2.addNote("Supporting evidence 3");
		
		final NoteCard testNoteCard3 = new NoteCard("The End");
		testNoteCard3.addNote("Summary of point 1");
		testNoteCard3.addNote("Summary of point 2");
		testNoteCard3.addNote("Summary of point 3");
	}
	
	public void logNotes() {
		ArrayList<NoteCard> list = getList();
		
		String noteListAsText = "";

		for (NoteCard card: list) {
			noteListAsText += card.getTitle();
			ArrayList<String> notes = card.getNotes();
			
			for (String note: notes ) {
				noteListAsText += "  ";
				noteListAsText += note;
			}
		}
		
		Log.d(TAG, noteListAsText);
	}

}