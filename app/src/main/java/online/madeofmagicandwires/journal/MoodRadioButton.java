package online.madeofmagicandwires.journal;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.CompoundButton;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom Checkable View containing a Mood value
 *
 * @see "https://kmansoft.com/2011/01/11/checkable-image-button/"
 * @see "https://crosp.net/blog/software-development/mobile/android/creating-custom-radio-groups-radio-buttons-android/"
 * @see JournalEntry.Mood
 * @see Checkable
 */
public class MoodRadioButton extends android.support.v7.widget.AppCompatRadioButton implements Checkable {

    private static final int MOOD_ANGRY = 0;
    private static final int MOOD_BORED = 1;
    private static final int MOOD_HAPPY = 2;
    private static final int MOOD_SAD   = 3;
    private static final int MOOD_UNKNOWN = -1;

    /** mood attr enum-like, defining the possible values allowed for the mood attribute **/
    @IntDef({MOOD_ANGRY, MOOD_BORED, MOOD_HAPPY, MOOD_SAD, MOOD_UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MoodAttr {}


    /** represents the mood of this View **/
    private @JournalEntry.Mood String mood;


    /**
     * Constructor foregoing the attributes parameter
     * @param context activity context
     */
    public MoodRadioButton(Context context) {
        this(context, null);
    }

    /**
     * Standard constructor
     * @param context activity context
     * @param attrs attribrutes of this view
     */
    public MoodRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);



        // Retrieve custom attributes from AttributeSets
        TypedArray  styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MoodRadioButton);
        @MoodAttr int moodAttr = styledAttrs.getInt(R.styleable.MoodRadioButton_moodValue, MOOD_UNKNOWN);
        setMood(moodAttr);

        styledAttrs.recycle();

    }

    /**
     * parses the moodAttr and sets mood to the  corresponding JournalEntry.Mood value
     * @param moodAttr
     * @see MoodAttr
     * @see JournalEntry.Mood
     * @see #mood
     */
    public void setMood(@MoodAttr int moodAttr) {
        switch (moodAttr) {
            case MOOD_ANGRY:
                this.mood = JournalEntry.MOOD_ANGRY;
                break;
            case MOOD_BORED:
                this.mood = JournalEntry.MOOD_BORED;
                break;
            case MOOD_HAPPY:
                this.mood = JournalEntry.MOOD_HAPPY;
                break;
            case MOOD_SAD:
                this.mood = JournalEntry.MOOD_SAD;
                break;
            case MOOD_UNKNOWN:
                this.mood = JournalEntry.MOOD_UNKNOWN;
        }
    }

    /**
     * Gets the mood value associated with this View
     * @return
     */
    public @JournalEntry.Mood String getMood() {
        return this.mood;
    }


    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superS = super.onSaveInstanceState();
        MoodRadioButtonSaveState ss = new MoodRadioButtonSaveState(superS);
        ss.checked = isChecked();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        MoodRadioButtonSaveState ss = (MoodRadioButtonSaveState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setChecked(ss.checked);
        requestLayout();
    }

    public MoodRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    @Override
    public void setOnCheckedChangeListener(@Nullable CompoundButton.OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
    }
}
