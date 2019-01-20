package online.madeofmagicandwires.journal;


import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class DetailActivity extends JournalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //draw views
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
          getSupportActionBar().setHomeButtonEnabled(true);
        }

        // retrieve data from saved state or intent if the that is not available
        retrieveJournalEntry(savedInstanceState);
        if(entry==null) {
            retrieveJournalEntry(getIntent());
        }

        // bind data to views
        updateState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.details_action_edit:
                if(entry != null) {
                    editEntry(this, entry);
                } else {
                    Toast toast = Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG);
                    toast.show();
                }
                return true;
                default:
                    return super.onOptionsItemSelected(item);


        }
    }


    @Override
    public void updateState() {

        if(entry != null) {

            // Set the mood
            @JournalEntry.Mood String mood = entry.getMood();

            View root = findViewById(R.id.toolbar_root);
            TextView moodEmoji = findViewById(R.id.details_entry_mood_emoji);
            switch (mood) {
                case JournalEntry.MOOD_ANGRY:
                    root.setBackgroundColor(getColor(R.color.moodAngry));
                    moodEmoji.setText(R.string.emoji_angry);
                    moodEmoji.setContentDescription(getString(R.string.mood_angry));
                    break;
                case JournalEntry.MOOD_BORED:
                    root.setBackgroundColor(getColor(R.color.moodBored));
                    moodEmoji.setText(R.string.emoji_bored);
                    moodEmoji.setContentDescription(getString(R.string.mood_bored));
                    break;
                case JournalEntry.MOOD_HAPPY:
                    root.setBackgroundColor(getColor(R.color.moodHappy));
                    moodEmoji.setText(R.string.emoji_happy);
                    moodEmoji.setContentDescription(getString(R.string.mood_happy));
                    break;
                case JournalEntry.MOOD_SAD:
                    root.setBackgroundColor(getColor(R.color.moodSad));
                    moodEmoji.setText(R.string.emoji_sad);
                    moodEmoji.setContentDescription(getString(R.string.mood_sad));
                    break;
                case JournalEntry.MOOD_UNKNOWN:
                    break;

                    default:
                        break;
            }

            // set title and date
            TextView entryTitle = findViewById(R.id.details_entry_title);
            TextView entryDate = findViewById(R.id.details_entry_date);
            entryTitle.setText(entry.getTitle());
            entryDate.setText(EntryAdapter.formatDate(this, entry.getTimestamp(), false));

            //set content
            TextView entryContent = findViewById(R.id.details_entry_content);
            entryContent.setText(entry.getContent());
        }
    }
}
