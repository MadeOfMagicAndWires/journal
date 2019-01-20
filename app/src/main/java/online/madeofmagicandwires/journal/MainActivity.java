package online.madeofmagicandwires.journal;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

public class MainActivity extends JournalActivity {

    private static EntryAdapter adapter = null;
    private static JournalActivity.CursorAdapterListener listener = null;


    /**
     * Draw the screen and bind its data
     * @param savedInstanceState data saved from previous state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // draw views
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set event listener for floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new CreateEntryListener());

        // set event listener for SwipeRefreshLayout
        SwipeRefreshLayout refreshLayout = findViewById(R.id.swipe_refresh);
        CursorRefreshListener refreshListener = new CursorRefreshListener(refreshLayout);


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
