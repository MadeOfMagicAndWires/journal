package online.madeofmagicandwires.journal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends JournalActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }

    @Override
    public void updateState() {
        // TODO: implement
    }
}
