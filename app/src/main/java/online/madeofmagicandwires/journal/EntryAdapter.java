package online.madeofmagicandwires.journal;


import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ResourceCursorAdapter;


public class EntryAdapter extends ResourceCursorAdapter implements AdapterView.OnItemClickListener {

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
    public EntryAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }


    /**
     * Bind an existing view to the data pointed to by cursor
     *
     * @param view    Existing view, returned earlier by newView
     * @param context Interface to application's global information
     * @param cursor  The cursor from which to get the data. The cursor is already
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {


    }





    /**
     * Opens DetailActivity showing the details of the journal entry clicked.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


}
