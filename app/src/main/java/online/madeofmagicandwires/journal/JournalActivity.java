package online.madeofmagicandwires.journal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


/**
 * Journal Activity superclass containing some common members and methods for activities
 * handling JournalEntries
 */
abstract public class JournalActivity extends AppCompatActivity {

    /*
     * Parent classes containing all listener events needed by JournalActivities
     */

    /**
     * Listener class to be used to open the "create a new entry" dialogue
     */
    public class CreateEntryListener implements View.OnClickListener {

        /**
         * Opens InputActivity to create a new Journal Entry on click
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            createEntry(v);
        }
    }

    /**
     * Listener class to be used by the CursorAdapter; Shows the details of or deletes the selected entry.
     */
    public class CursorAdapterListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


        /**
         * Opens DetailActivity to show the details of the selected entry
         *
         * @param parent   The AdapterView where the click happened.
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         * @param id       The row id of the item that was clicked.
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("item type", parent.getItemAtPosition(position).toString());
            if(db != null) {
                JournalEntry entry =  db.getEntry(id);
                if(entry != null) {
                    if(view.getContext() instanceof JournalActivity) {
                        showEntry((JournalActivity) view.getContext(), entry);
                    }

                }
            }
        }

        /**
         * Deletes the selected Journal Entry from the SQLiteDatabase
         *
         * @param parent   The AbsListView where the click happened
         * @param view     The view within the AbsListView that was clicked
         * @param position The position of the view in the list
         * @param id       The row id of the item that was clicked
         * @return true if the callback consumed the long click, false otherwise
         */
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO: implement this
            db.delete(id);
            updateState();
            return false;
        }
    }




    /** the standard key under which to put or retrieve a Journal Entry to or from a Bundle **/
    public static final String JOURNAL_ENTRY_BUNDLE_KEY = "JournalEntry";
    public static final int REQUEST_EDIT_ENTRY = 1;


    public JournalEntry entry = null;
    public static EntryDatabase db = null;


    /**
     * Retrieves a journal entry from savedInstanceState or Intent if available
     * @param savedInstanceState saved data from a suspended state
     *
     */
    public void retrieveJournalEntry(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(JOURNAL_ENTRY_BUNDLE_KEY)) {
            entry = (JournalEntry) savedInstanceState.getSerializable(JOURNAL_ENTRY_BUNDLE_KEY);
        }
    }

    /**
     * Retrieves a journal entry from intent
     * @param i the intent to retrieve journal entry from
     */
    public void retrieveJournalEntry(Intent i) {
        if(i.hasExtra(JOURNAL_ENTRY_BUNDLE_KEY)) {
            entry = (JournalEntry) i.getSerializableExtra(JOURNAL_ENTRY_BUNDLE_KEY);
        }

    }

    /**
     * Retrieves a journal entry from the result returned by a previous activity
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
            updateState();
        } else {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Updates views with data contained in Journal entry or EntryDatabase
     *
     * Used to be called updateData() but was renamed to be more descriptive of its actual behaviour
     */
    abstract public void updateState();


    /**
     * Opens InputActivity to create and a fresh Journal entry to be edited by the user
     * @param v a View inside a JournalActivity context
     * @see #editEntry
     * @see InputActivity
     */
    public void createEntry(View v) {
        if(v.getContext() instanceof JournalActivity) {
            editEntry((JournalActivity) v.getContext(), new JournalEntry("", "", JournalEntry.MOOD_UNKNOWN));
        }

    }

    /**
     * Starts the InputActivity to create or open a Journal entry to be edited by the user
     *
     * @param activity the Activity from which to start InputActivity
     * @param entry   a possible entry to pass along to InputActivity, will be ignored if null
     * @see InputActivity
     */
    public void editEntry(@NonNull JournalActivity activity, @Nullable JournalEntry entry) {
        Intent intent = new Intent(activity, InputActivity.class);
        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // always return to launcher activity;
        if(entry != null) {
            intent.putExtra(JOURNAL_ENTRY_BUNDLE_KEY, entry);

        }

        activity.startActivityForResult(intent, REQUEST_EDIT_ENTRY);

    }

    /**
     * Saves a new or updated entry to the Journal Entries database
     * @param entry Journal Entry to be inserted or updated in the database
     */
    public void saveEntry(JournalEntry entry) {
        long result = -1;
        if(entry != null && !entry.isEmpty()) {
            EntryDatabase db = EntryDatabase.getInstance(this);


            // entry is not in database already, insert
            Log.d("entry has databaseId", String.valueOf(entry.hasDatabaseId()));
            Log.d("entry database id ", String.valueOf(entry.getDatabaseId()));
            Log.d("entry data", entry.toString());
            result = db.insert(entry);
        }

        if(result <= -1) {
            Toast ts = Toast.makeText(
                    this,
                    "Something went wrong and the entry was not saved",
                    Toast.LENGTH_LONG);
            ts.show();
        }

    }

    /**
     * Shows an entries details in DetailActivity
     * @param activity the activity to start DetailActivity from (will in practice always be MainActivity)
     * @param entry the JournalEntry object to show
     */
    public void showEntry(@NonNull JournalActivity activity, @NonNull JournalEntry entry) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(JOURNAL_ENTRY_BUNDLE_KEY, entry);

        activity.startActivity(intent);

    }


    /**
     * Custom onSaveState that saves a journalentry to be retrieved on create
     * @param outState bundle to store data in to be reused next time this activity is drawn
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(entry != null) {
            outState.putSerializable(JOURNAL_ENTRY_BUNDLE_KEY, entry);
        }
    }
}
