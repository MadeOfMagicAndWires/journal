package online.madeofmagicandwires.journal;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Parcelable SavedState that remembers checked state.
 * @see "https://kmansoft.com/2011/01/11/checkable-image-button/"
 * @see Parcelable
 * @see android.view.View.BaseSavedState
 */
public class MoodRadioButtonSaveState extends View.BaseSavedState {

    /**
     * Parcelable Creator.
     */
    public static final Parcelable.Creator<MoodRadioButtonSaveState> CREATOR =
            new Parcelable.Creator<MoodRadioButtonSaveState>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it
         * from the given Parcel whose data had previously been written by
         * {@link Parcelable#writeToParcel Parcelable.writeToParcel()}.
         *
         * @param source The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        @Override
        public MoodRadioButtonSaveState createFromParcel(Parcel source) {
            return new MoodRadioButtonSaveState(source);
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry
         * initialized to null.
         */
        @Override
        public MoodRadioButtonSaveState[] newArray(int size) {
            return new MoodRadioButtonSaveState[size];
        }
    };

    /** keeps track of whether the viewstate was checked **/
    boolean checked;

    /**
     * Constructor called by derived classes when creating their SavedState objects
     *
     * @param superState The state of the superclass of this view
     */
    public MoodRadioButtonSaveState(Parcelable superState) {
        super(superState);
    }

    /**
     * Constructor called by derived classes when creating their SavedState objects
     *
     * @param in The state of this view
     */
    private MoodRadioButtonSaveState(Parcel in) {
        super(in);
        checked = (Boolean) in.readValue(null);
    }

    /**
     * Writes this view to a Parcel
     * @param out Parcel containing the values
     * @param flags parcelable flags
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeValue(checked);
    }
}