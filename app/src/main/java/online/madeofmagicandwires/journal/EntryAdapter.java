package online.madeofmagicandwires.journal;


import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import static online.madeofmagicandwires.journal.EntryDatabase.TABLE_COLUMN_MOOD;
import static online.madeofmagicandwires.journal.EntryDatabase.TABLE_COLUMN_TIMESTAMP;
import static online.madeofmagicandwires.journal.EntryDatabase.TABLE_COLUMN_TITLE;
import static online.madeofmagicandwires.journal.JournalEntry.MOOD_ANGRY;
import static online.madeofmagicandwires.journal.JournalEntry.MOOD_BORED;
import static online.madeofmagicandwires.journal.JournalEntry.MOOD_HAPPY;
import static online.madeofmagicandwires.journal.JournalEntry.MOOD_SAD;


public class EntryAdapter extends ResourceCursorAdapter {

    /** The default layout to inflate **/
    public static final @LayoutRes int DEFAULT_LAYOUT = R.layout.entry_row;
    /** represents 48 hours counted out to milliseconds */
    public static final long LONG_TIME_48_HOURS = (2 * 24 * 60 * 60 * 1000);


    /**
     * Standard constructor.
     *
     * @param context The context where the ListView associated with this adapter is running
     * @param layout  Resource identifier of a layout file that defines the views
     *                for this list item.  Unless you override them later, this will
     *                define both the item views and the drop down views.
     * @param c       The cursor from which to get the data.
     * @param flags   Flags used to determine the behavior of the adapter,
     *                as per {@link android.widget.CursorAdapter#CursorAdapter(Context, Cursor, int)}.
     */
    public EntryAdapter(@NonNull  Context context, @Nullable @LayoutRes int layout, @NonNull Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    /**
     * Constructor with default behavior as per
     * {@link android.widget.CursorAdapter#CursorAdapter(Context, Cursor, boolean)}; it is recommended
     * you not use this, but instead {@link android.widget.ResourceCursorAdapter#ResourceCursorAdapter(Context, int, Cursor, int)}.
     * When using this constructor, {@link #FLAG_REGISTER_CONTENT_OBSERVER}
     * will always be set.
     *
     * @param context     The context where the ListView associated with this adapter is running
     * @param layout      resource identifier of a layout file that defines the views
     *                    for this list item.  Unless you override them later, this will
     *                    define both the item views and the drop down views.
     * @param c           The cursor from which to get the data.
     * @param autoRequery If true the adapter will call requery() on the
     *                    cursor whenever it changes so the most recent
     */
    public EntryAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
    }

    public EntryAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, DEFAULT_LAYOUT, c, autoRequery);
    }


    /**
     * constructor that does not require a layout resource id but uses {@link #DEFAULT_LAYOUT} instead
     * @param context activity context
     * @param c       ResourceCursor
     * @param flags   Flags
     */
    public EntryAdapter(@NonNull Context context, @NonNull Cursor c, int flags) {
        this(context, DEFAULT_LAYOUT, c, flags);
    }




    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param root    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param c  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View root, Context context, Cursor c) {
        // create entry from Cursor data
        String entryTitle =  c.getString(c.getColumnIndex(TABLE_COLUMN_TITLE));
        String entryMood =   c.getString(c.getColumnIndex(TABLE_COLUMN_MOOD));
        Timestamp entryDateTime =  Timestamp.valueOf(c.getString(c.getColumnIndex(TABLE_COLUMN_TIMESTAMP)));

        // retrieve respective field views
        TextView title = root.findViewById(R.id.entryTitle);
        TextView mood  = root.findViewById(R.id.entryMood);
        TextView date  = root.findViewById(R.id.entryDate);
        TextView moodEmoji = root.findViewById(R.id.entryMoodEmoji);


        //bind fields to data
        title.setText(entryTitle);
        mood.setText(entryMood);
        moodEmoji.setText(getMoodEmoji(entryMood));
        date.setText(formatDate(context, entryDateTime));

    }

    public static @StringRes int getMoodEmoji(@NonNull @JournalEntry.Mood String mood) {
        switch (mood) {
            case MOOD_ANGRY:
                return R.string.emoji_angry;
            case MOOD_BORED:
                return R.string.emoji_bored;
            case MOOD_HAPPY:
                return R.string.emoji_happy;
            case MOOD_SAD:
                return R.string.emoji_sad;
            default:
                return -1;
        }

    }

    /**
     * Format date to specific values depending on how long ago it was
     * @param datetime
     * @return formatted date, such as "x seconds ago", "x minute(s) ago" "yesterday" or short iso 8601 format
     */
    public static String formatDate(Context c, @NonNull Timestamp datetime) {
        String formatted = "";
        long now = System.currentTimeMillis();
        long timeSince = now - datetime.getTime();

        //             equals 48 hours as a long
        if(timeSince < LONG_TIME_48_HOURS) {
             formatted = (String) DateUtils.getRelativeTimeSpanString(
                     datetime.getTime(),
                     now,
                     DateUtils.MINUTE_IN_MILLIS,
                     DateUtils.FORMAT_ABBREV_RELATIVE);
        } else {
            SimpleDateFormat shortIsoFormat = new SimpleDateFormat("yyyy-MM-dd",
                    c.getResources().getConfiguration().getLocales().get(0));
            formatted = shortIsoFormat.format(datetime);
        }
        return formatted;
    }



}
