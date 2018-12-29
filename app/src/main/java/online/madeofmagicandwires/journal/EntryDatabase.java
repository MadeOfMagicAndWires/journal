package online.madeofmagicandwires.journal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class EntryDatabase extends SQLiteOpenHelper {

    /**
     * Constant members relating to the database and table values to be used
     * to avoid sanitation and typos
     **/
    public static final String DB_NAME = "JournalEntryDatabase.db";
    public static final int DB_VERSION = 3;
    public static final String DB_TABLE_NAME = "entries";

    public static final String TABLE_COLUMN_ID = "_id";
    public static final String TABLE_COLUMN_TITLE = "title";
    public static final String TABLE_COLUMN_CONTENT = "content";
    public static final String TABLE_COLUMN_MOOD = "mood";
    public static final String TABLE_COLUMN_TIMESTAMP = "entryDate";

    /** singleton instances to be returned if set **/
    private static EntryDatabase instance = null;


    private EntryDatabase(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME +
                "("  +
                TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
                TABLE_COLUMN_TITLE +  " TEXT NOT NULL," +
                TABLE_COLUMN_CONTENT + " TEXT NOT NULL," +
                TABLE_COLUMN_MOOD + " TEXT," +
                TABLE_COLUMN_TIMESTAMP + " TIMESTAMP)");


    }


    /**
     * Returns the singleton EntryDatabase instance, use this when requiring a EntryDatabase object
     * @param context Application/Activity context
     * @return the singleton EntryDatabase instance
     */
    public static synchronized EntryDatabase getInstance(@NonNull Context context) {
        if(instance == null) {
            instance = new EntryDatabase(context);
        }
        return instance;
    }

    /**
     * Returns all entries in the table
     * @return Cursor containing all the data
     */
    public Cursor selectAll() {
        // we're not writing in this method so getReadableDatabase instead
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + DB_TABLE_NAME + " ORDER BY " + TABLE_COLUMN_TIMESTAMP + " DESC",
                null);
    }

    /**
     * Returns all entries as a list of JournalEntry objects
     *
     * As I'm getting conflicting answer as whether it's better to use a Cursor or
     * to close the database as quickly as possible I've added an implimentation that returns all
     * entries as their respective JournalEntry objects.
     *
     * @return
     */
    public List<JournalEntry> selectAllAsList() {
        Cursor c = selectAll();
        List<JournalEntry> entries = new ArrayList<>();
        int i;
        for(i=0;i<c.getCount();i++); {
;           c.moveToPosition(i);
            entries.add(new JournalEntry(
                    c.getString(c.getColumnIndex(TABLE_COLUMN_TITLE)),
                    c.getString(c.getColumnIndex(TABLE_COLUMN_CONTENT)),
                    c.getString(c.getColumnIndex(TABLE_COLUMN_MOOD)),
                    Timestamp.valueOf(c.getString(c.getColumnIndex(TABLE_COLUMN_TIMESTAMP))),
                    c.getLong(c.getColumnIndex(TABLE_COLUMN_ID))
                    )
            );
        }
        c.close();
        return entries;
    }

    /**
     * Returns the database entry as a JournalEntry object
     * @param id the database id of the object; corresponding to its value under the {@link #TABLE_COLUMN_ID} column
     * @return JournalEntry object containing all the entry's data in the database
     */
    public JournalEntry getEntry(long id) {
        SQLiteDatabase db = getReadableDatabase();
        JournalEntry entry = null;
        Cursor c =  db.query(DB_TABLE_NAME,
                null,
                 TABLE_COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        // Log.d("getEntry", String.valueOf(c.getCount()));
        Log.d("table", DatabaseUtils.dumpCursorToString(c));
        Log.d("columnIndex", TABLE_COLUMN_TITLE + ": " + c.getColumnIndex(TABLE_COLUMN_TITLE));

        if (c.getCount() != 0 && c.moveToFirst()) {
            entry =  new JournalEntry(
                    c.getString(c.getColumnIndex(TABLE_COLUMN_TITLE)),
                    c.getString(c.getColumnIndex(TABLE_COLUMN_CONTENT)),
                    c.getString(c.getColumnIndex(TABLE_COLUMN_MOOD)),
                    Timestamp.valueOf(c.getString(c.getColumnIndex(TABLE_COLUMN_TIMESTAMP))),
                    c.getLong(c.getColumnIndex(TABLE_COLUMN_ID))
            );
        }

         db.close();
         return entry;
    }

    /**
     * Inserts a journal entry into the database
     * @param entry journal entry to insert
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert(JournalEntry entry) {
        long returnValue = -2;
        // put all values into container values
        ContentValues values = new ContentValues();
        values.put(TABLE_COLUMN_TITLE, entry.getTitle());
        values.put(TABLE_COLUMN_CONTENT, entry.getContent());
        values.put(TABLE_COLUMN_MOOD, entry.getMood());
        values.put(TABLE_COLUMN_TIMESTAMP, entry.getTimestamp().toString());
        // if entry has a Database id, include it
        if(entry.hasDatabaseId()) {
            values.put(TABLE_COLUMN_ID, entry.getDatabaseId());
        }

        Log.d("entry values", values.toString());

        // open db and insert or replace the journal entry
        SQLiteDatabase db = getWritableDatabase();
        returnValue = db.insertWithOnConflict(
                    DB_TABLE_NAME,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE); // replace existing entry if id is already present

        Log.d("insert", String.valueOf(returnValue));

        db.close();
        return returnValue;

    }


    /**
     * Removes a selected entry
     * @param id the database id of the entry to be removed
     * @return the number of rows affected
     */
    public int delete(long id) {
        SQLiteDatabase db = getWritableDatabase();
        int resultCode = db.delete(
                DB_TABLE_NAME,
                TABLE_COLUMN_ID + "=?",
                new String[] {String.valueOf(id)}
        );
        db.close();
        return resultCode;
    }


    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
            this.onCreate(db);
        }

    }
}
