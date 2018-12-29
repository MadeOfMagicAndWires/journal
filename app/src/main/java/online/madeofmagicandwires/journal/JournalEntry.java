package online.madeofmagicandwires.journal;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Class representing the data model of a journal entry
 *
 */
public class JournalEntry implements Serializable {

    /* possible values for the Mood enumerated annotation **/
    // TODO: allow localization
    public static final String MOOD_ANGRY = "Angry";
    public static final String MOOD_BORED = "Bored";
    public static final String MOOD_HAPPY = "Happy";
    public static final String MOOD_SAD   = "Sad";
    public static final String MOOD_UNKNOWN = "Unknown";

    /** possible values representing which data is missing from entry **/
    public static final int MISSING_NOTHING = 0;
    public static final int MISSING_TITLE = -1;
    public static final int MISSING_CONTENT = -2;
    public static final int MISSING_MOOD = -3;

    @IntDef({
            MISSING_NOTHING,
            MISSING_TITLE,
            MISSING_CONTENT,
            MISSING_MOOD,
    })
    @Retention(RetentionPolicy.SOURCE)

    /**
     * Enum-like defining which data might be missing
     */
    public @interface Missing {}



    /* Defining a String enumerated annotation */
    @StringDef({MOOD_ANGRY, MOOD_BORED, MOOD_HAPPY, MOOD_SAD, MOOD_UNKNOWN})

    /**
     * "Enum-like" representation of a mood.
     *
     * Possible values include {@link #MOOD_ANGRY}, {@link #MOOD_BORED}, {@link #MOOD_HAPPY},
     * {@link #MOOD_SAD}, and {@link #MOOD_UNKNOWN}
     */
    public @interface Mood {}


    /* actual members */
    @Nullable
    /** represents the database databaseId of the journal entry **/
    private long databaseId = -1;
    /** represents the title of the journal entry **/
    private String title;
    /** represents the text of the journal entry **/
    private String content;
    /** Represents the mood associated with the journal entry **/
    private @Mood String mood;
    private Timestamp timestamp;


    /**
     * Constructor providing data for all possible members; used for entries retrieved from
     * the database
     * @param title the title of this entry
     * @param content the body of this entry
     * @param mood the mood associated with this entry
     * @param timestamp the timestamp of creation of this entry
     * @param databaseId  the database databaseId of this entry
     */
    public JournalEntry(String title, String content, @Mood String mood, Timestamp timestamp, long databaseId) {
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.timestamp = timestamp;
        this.databaseId = databaseId;
    }

    /**
     * Constructor forsaking the databaseId field which is left at null; used for creation of new entries
     * @param title the title of this entry
     * @param content the body of this entry
     * @param mood the mood associated with this entry
     * @param timestamp the timestamp of creation of this entry
     */
    public JournalEntry(String title, String content, @Mood String mood, Timestamp timestamp) {
        this(title, content, mood, timestamp, -1);
    }


    /**
     * Constructor setting the least amount of members manually; used by lazy people
     *
     * {@link #timestamp}is set to time of allocation, @{#databaseId} is set to null
     * @param title the title of this entry
     * @param content the body of this entry
     * @param mood the mood associated with this entry
     */
    public JournalEntry(String title, String content, @Mood String mood) {
        this(title, content, mood, new Timestamp(new Date().getTime()), -1);
    }

    /**
     * Returns the title of this entry
     * @return String representing the title of this entry
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Returns the message of this entry
     * @return String containing the content of this entry
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Returns the mood associated with this entry
     * @return a Mood value representing the mood of this entry
     */
    public @Mood String getMood() {
        return this.mood;
    }

    /**
     * Returns the timestamp of this entry
     * @return the timestamp of the entry's creation
     */
    public Timestamp getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns the database databaseId of this entry
     * @return Integer corresponding to the unique databaseId of this entry inside the SQLite database
     */
    public long getDatabaseId() {
        return this.databaseId;
    }

    /**
     * UpTimestamps the title of this entry
     * @param title the title to replace the current one
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * UpTimestamps the content of this entry
     * @param content the text to replace the current entry body
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * UpTimestamps the mood associated with this entry
     * @param mood the new mood to replace the current one
     */
    public void setMood(@Mood String mood) {
        this.mood = mood;
    }

    /**
     * UpTimestamps the creation timestamp of this entry
     * @param timestamp the new timestamp to replace the old one
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns whether this entry has a database databaseId attached to it
     * @return true if this entry has a unique databaseId; false if not.
     */
    public boolean hasDatabaseId() {
        return (this.databaseId != -1);
    }


    @NonNull
    @Override
    /**
     * Returns a representation of this entry
     * @return a Human readable summary of this JournalEntry instance.
     */
    public String toString() {
        return getTimestamp().toString() + " | " +  getTitle() + ": " + getContent();
    }

    public @Missing int getMissingDataType() {
        if(title.isEmpty()) {
            return MISSING_TITLE;
        } else if (content.isEmpty()) {
            return MISSING_CONTENT;
        } else if (mood.isEmpty() || mood.equals(MOOD_UNKNOWN)) {
            return MISSING_MOOD;
        } else {
            return MISSING_NOTHING;
        }

    }

    /**
     * Returns whether this journal entry is empty
     * @return true if this entry lacks a title or content, or if the set mood is unknown
     */
    public boolean isEmpty() {
        return (title.isEmpty() || content.isEmpty() || mood.isEmpty() || mood.equals(MOOD_UNKNOWN));

    }
}
