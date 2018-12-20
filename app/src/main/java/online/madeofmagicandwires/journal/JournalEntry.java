package online.madeofmagicandwires.journal;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

/**
 * Class representing the data model of a journal entry
 *
 */
public class JournalEntry implements Serializable {

    /* possible values for the Mood enumerated annotation **/
    public static final String MOOD_ANGRY = "Angry";
    public static final String MOOD_BORED = "Bored";
    public static final String MOOD_HAPPY = "Happy";
    public static final String MOOD_SAD   = "Sad";

    /* Defining a String enumerated annotation */
    @StringDef({MOOD_ANGRY, MOOD_BORED, MOOD_HAPPY, MOOD_SAD})
    @Retention(RetentionPolicy.SOURCE)

    /**
     * "Enum-like" representation of a mood.
     *
     * Possible values include {@link #MOOD_ANGRY}, {@link #MOOD_BORED}, {@link #MOOD_HAPPY}, and
     * {@link #MOOD_SAD}
     */
    public @interface Mood {}


    /* actual members */
    @Nullable
    /** represents the database id of the journal entry **/
    private Integer id = null;
    /** represents the title of the journal entry **/
    private String title;
    /** represents the text of the journal entry **/
    private String content;
    /** Represents the mood associated with the journal entry **/
    private Mood mood;
    private Date timestamp;

    /**
     * Constructor providing data for all possible members; used for entries retrieved from
     * the database
     * @param title the title of this entry
     * @param content the body of this entry
     * @param mood the mood associated with this entry
     * @param timestamp the timestamp of creation of this entry
     * @param id  the database id of this entry
     */
    public JournalEntry(String title, String content, Mood mood, Date timestamp, Integer id) {
        this.title = title;
        this.content = content;
        this.mood = mood;
        this.timestamp = timestamp;
        this.id = id;
    }

    /**
     * Constructor forsaking the id field which is left at null; used for creation of new entries
     * @param title the title of this entry
     * @param content the body of this entry
     * @param mood the mood associated with this entry
     * @param timestamp the timestamp of creation of this entry
     */
    public JournalEntry(String title, String content, Mood mood, Date timestamp) {
        this(title, content, mood, timestamp, null);
    }


    /**
     * Constructor setting the least amount of members manually; used by lazy people
     *
     * {@link #timestamp}is set to time of allocation, @{#id} is set to null
     * @param title the title of this entry
     * @param content the body of this entry
     * @param mood the mood associated with this entry
     */
    public JournalEntry(String title, String content, Mood mood) {
        this(title, content, mood, new Date(), null);
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
    public Mood getMood() {
        return this.mood;
    }

    /**
     * Returns the timestamp of this entry
     * @return the timestamp of the entry's creation
     */
    public Date getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns the database id of this entry
     * @return Integer corresponding to the unique id of this entry inside the SQLite database
     */
    @Nullable
    public Integer getId() {
        return this.id;
    }

    /**
     * Updates the title of this entry
     * @param title the title to replace the current one
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Updates the content of this entry
     * @param content the text to replace the current entry body
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Updates the mood associated with this entry
     * @param mood the new mood to replace the current one
     */
    public void setMood(Mood mood) {
        this.mood = mood;
    }

    /**
     * Updates the creation timestamp of this entry
     * @param timestamp the new timestamp to replace the old one
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Returns whether this entry has a database id attached to it
     * @return true if this entry has a unique id; false if not.
     */
    public boolean hasId() {
        return (this.id != null);
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
}
