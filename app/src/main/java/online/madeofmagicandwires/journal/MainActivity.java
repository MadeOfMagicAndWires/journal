package online.madeofmagicandwires.journal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new CreateEntryListener());


        updateState();
    }

    /**
     * Initiates and links ListView to adapter and entries.
     */
    @Override
    public void updateState(){
        ListView list = findViewById(R.id.entriesList);
        if(db == null) {
            db = EntryDatabase.getInstance(getApplicationContext());
            Cursor c = db.selectAll();
            adapter = new EntryAdapter(this, EntryAdapter.DEFAULT_LAYOUT, c, EntryAdapter.NO_SELECTION);
            listener = new CursorAdapterListener();


            list.setEmptyView(findViewById(R.id.emptyListState));
            list.setAdapter(adapter);
            list.setOnItemClickListener(listener);
            list.setOnItemLongClickListener(listener);

        } else {
            Cursor c = db.selectAll();
            adapter.swapCursor(c);
        }
    }


}
