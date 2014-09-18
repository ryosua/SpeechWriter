package edu.psu.rcy5017.publicspeakingassistant;
import java.util.List;

import edu.psu.rcy501.publicspeakingassistant.R;
import edu.psu.rcy5017.publicspeakingassistant.model.NoteCardListDBTest;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class SpeechListActivity extends ListActivity {
    private SpeechListDataSource datasource;
    private static final String TAG = "SpeechListActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_list);

        datasource = new SpeechListDataSource(this);
        datasource.open();

        List<NoteCardListDBTest> values = datasource.getAllNoteCardListDBTests();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<NoteCardListDBTest> adapter = new ArrayAdapter<NoteCardListDBTest>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<NoteCardListDBTest> adapter = (ArrayAdapter<NoteCardListDBTest>) getListAdapter();
        NoteCardListDBTest speech = null;
        switch (view.getId()) {
        
        case R.id.add:
            // save the new speech to the database
        	
            speech = datasource.createNoteCardListDBTest("New Speech");
            adapter.add(speech);
            
            break;
        case R.id.delete:
            if (getListAdapter().getCount() > 0) {
                speech = (NoteCardListDBTest) getListAdapter().getItem(0);
                datasource.deleteNoteCardListDBTest(speech);
                adapter.remove(speech);
            }
            
            break;
            
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

} 