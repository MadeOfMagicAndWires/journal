package online.madeofmagicandwires.journal;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.Timestamp;

import static online.madeofmagicandwires.journal.JournalEntry.MISSING_CONTENT;
import static online.madeofmagicandwires.journal.JournalEntry.MISSING_MOOD;
import static online.madeofmagicandwires.journal.JournalEntry.MISSING_NOTHING;
import static online.madeofmagicandwires.journal.JournalEntry.MISSING_TITLE;


public class InputActivity extends JournalActivity {

    private MoodRadioButtonListener moodRadioButtonListenerInstance;

    private class MoodRadioButtonListener implements RadioGroup.OnCheckedChangeListener {

        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            @JournalEntry.Mood String selectedMood = ((MoodRadioButton) findViewById(checkedId)).getMood();
            if(!selectedMood.equals(entry.getMood())) {
                entry.setMood(selectedMood);
            }
        }
    }



    /**
     * OnCreate method; draws the activity layout,
     * @param savedInstanceState Bundle of data saved from previous instances
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // draw views
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(entry == null) {
            retrieveJournalEntry(getIntent());
            retrieveJournalEntry(savedInstanceState);
        }

        // update with data from retrieved journal entry or database
        updateState();

    }


    /**
     * Updates views with entry data if available.
     *
     */
    @Override
    public void updateState() {
        EditText title = findViewById(R.id.input_entry_title);
        EditText content = findViewById(R.id.input_entry_content);
        RadioGroup mood  = findViewById(R.id.input_entry_mood);

        title.setText(entry.getTitle());
        content.setText(entry.getContent());

        moodRadioButtonListenerInstance = new MoodRadioButtonListener();
        mood.setOnCheckedChangeListener(moodRadioButtonListenerInstance);

        switch (entry.getMood()) {
            case JournalEntry.MOOD_ANGRY:
                mood.check(R.id.input_entry_mood_angry);
                break;
            case JournalEntry.MOOD_BORED:
                mood.check(R.id.input_entry_mood_bored);
                break;
            case JournalEntry.MOOD_HAPPY:
                mood.check(R.id.input_entry_mood_happy);
                break;
            case JournalEntry.MOOD_SAD:
                mood.check(R.id.input_entry_mood_sad);
                break;
            case JournalEntry.MOOD_UNKNOWN:
                break;
        }


    }

    /**
     * Inflates the menu options into the AppBar
     * @param menu toolbar menu to which menu options are attached
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return true;
    }

    /**
     * Listener for action buttons and menu itens
     * @param selectedItem
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem selectedItem) {
        @IdRes int itemId = selectedItem.getItemId();

        switch (itemId) {
            case R.id.input_action_save:
                if(entry != null) {
                    updateEntry();
                    returnJournalEntry(entry);
                }
                return true;
            default:
                return super.onOptionsItemSelected(selectedItem);

        }

    }

    /**
     * returns an entry back to the previous activity
     * @param entry journal entry to send to a different activity
     */
    public void returnJournalEntry(@NonNull JournalEntry entry) {
        Intent i = getIntent();
        if(!entry.isEmpty()) {
            i.putExtra(JOURNAL_ENTRY_BUNDLE_KEY, entry);
            setResult(RESULT_OK, i);
            finish();
        } else {
            @JournalEntry.Missing int whatIsMissing = entry.getMissingDataType();
            String missingAlertText = "";
            switch (whatIsMissing) {
                case MISSING_TITLE:
                    missingAlertText = "Please write a title";
                    break;
                case MISSING_CONTENT:
                    missingAlertText = "Please write some content";
                    break;
                case MISSING_MOOD:
                    missingAlertText = "Please pick a mood";
                    break;
                case MISSING_NOTHING:
                    break;
            }
            Toast ts = Toast.makeText(this, missingAlertText, Toast.LENGTH_LONG);
            ts.show();
        }


    }

    /**
     * Updates entry data based on user inputted data
     * @return updates journalentry
     */
    private void updateEntry() {
        EditText title = findViewById(R.id.input_entry_title);
        EditText content = findViewById(R.id.input_entry_content);

        entry.setTitle(title.getText().toString());
        entry.setContent(content.getText().toString());
        entry.setTimestamp(new Timestamp(System.currentTimeMillis()));
        // mood is set on check

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(entry != null) {
            outState.putSerializable(JOURNAL_ENTRY_BUNDLE_KEY, entry);
        }
    }
}
