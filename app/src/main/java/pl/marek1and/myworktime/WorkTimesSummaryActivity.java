package pl.marek1and.myworktime;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.ListView;

import pl.marek1and.myworktime.db.DatabaseManager;

public class WorkTimesSummaryActivity  extends ActionBarActivity {

    private Toolbar toolbar;
    private DatabaseManager db;
    private ListView worktimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_summary);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        db = new DatabaseManager(this);


    }


}
