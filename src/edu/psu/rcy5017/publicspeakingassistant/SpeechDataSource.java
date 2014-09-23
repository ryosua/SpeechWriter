package edu.psu.rcy5017.publicspeakingassistant;

import java.util.ArrayList;
import java.util.List;

import edu.psu.rcy5017.publicspeakingassistant.model.NoteCardListDBTest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SpeechDataSource {

    // Database fields
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.SPEECH_TITLE };

    public SpeechDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public NoteCardListDBTest createNoteCardListDBTest(String noteCardList) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SPEECH_TITLE, noteCardList);
        long insertId = database.insert(DatabaseHelper.NOTE_CARD_LIST_NAME, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.NOTE_CARD_LIST_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        NoteCardListDBTest newNoteCardListDBTest = cursorToNoteCardListDBTest(cursor);
        cursor.close();
        return newNoteCardListDBTest;
    }

    public void deleteNoteCardListDBTest(NoteCardListDBTest noteCardList) {
        long id = noteCardList.getId();
        System.out.println("NoteCardListDBTest deleted with id: " + id);
        database.delete(DatabaseHelper.NOTE_CARD_LIST_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }
    
    public NoteCardListDBTest renameNoteCardListDBTest(String noteCardList) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.SPEECH_TITLE, noteCardList);
        long insertId = database.insert(DatabaseHelper.NOTE_CARD_LIST_NAME, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.NOTE_CARD_LIST_NAME,
                allColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        NoteCardListDBTest newNoteCardListDBTest = cursorToNoteCardListDBTest(cursor);
        cursor.close();
        return newNoteCardListDBTest;
    }

    public List<NoteCardListDBTest> getAllNoteCardListDBTests() {
        List<NoteCardListDBTest> noteCardLists = new ArrayList<NoteCardListDBTest>();

        Cursor cursor = database.query(DatabaseHelper.NOTE_CARD_LIST_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            NoteCardListDBTest noteCardList = cursorToNoteCardListDBTest(cursor);
            noteCardLists.add(noteCardList);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return noteCardLists;
    }

    private NoteCardListDBTest cursorToNoteCardListDBTest(Cursor cursor) {
        NoteCardListDBTest noteCardList = new NoteCardListDBTest();
        noteCardList.setId(cursor.getLong(0));
        noteCardList.setTitle(cursor.getString(1));
        return noteCardList;
    }
} 