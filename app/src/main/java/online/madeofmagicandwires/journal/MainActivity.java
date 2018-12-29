package online.madeofmagicandwires.journal;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends JournalActivity {

    private static EntryAdapter adapter = null;
    private static JournalActivity.CursorAdapterListener listener = null;

    /**
     * Retrieves a journal entry from a previous activity
     * @param requestCode the code of the type of request
     * @param resultCode the code of the result
     * @param data  the data of the result
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_EDIT_ENTRY) {
            if(resultCode == RESULT_OK) {
                retrieveJournalEntry(data);
                saveEntry(entry);
            }
            this.updateState();
        } else {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Draw the screen and bind its data
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new CreateEntryListener());

        // retrieve database data and bind it to the listview
        updateState();
    }

    /**
     * Initiates and links the ListView to the data from the database and required event listeners
     */
    @Override
    public void updateState(){
        ListView list = findViewById(R.id.entriesList);

        // create database and adapter if needed
        if(db == null) {
            db = EntryDatabase.getInstance(getApplicationContext());
            Cursor c = db.selectAll();
            adapter = new EntryAdapter(
                    this,
                    EntryAdapter.DEFAULT_LAYOUT,
                    c,
                    EntryAdapter.NO_SELECTION);

        } else { // otherwise, swap cursor
            Cursor c = db.selectAll();
            Log.d("updateState", DatabaseUtils.dumpCursorToString(c));
            adapter.swapCursor(c);
        }

        // attach adapter if needed
        if(list.getAdapter() == null) {
            list.setAdapter(adapter);
        }

        // attach listeners if needed
        if(!list.hasOnClickListeners()) {
            listener = new CursorAdapterListener();
            list.setOnItemClickListener(listener);
            list.setOnItemLongClickListener(listener);
        }

        // attach empty state view if needed
        if(list.getEmptyView() == null) {
            list.setEmptyView(findViewById(R.id.emptyListState));
        }

    }


}
