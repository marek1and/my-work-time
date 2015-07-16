package pl.marek1and.myworktime;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.util.List;

import pl.marek1and.myworktime.db.DatabaseManager;
import pl.marek1and.myworktime.db.beans.WorkTime;

public class WorkTimeActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_worktime);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        db = new DatabaseManager(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_worktime, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        Fragment af = fm.findFragmentByTag(CreatePanelFragment.FGMT_TAG);

        if (af != null) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    public void addEventAction(View v) {

        ImageButton b = (ImageButton)v;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.btn_rotation);
        b.startAnimation(slide);

        FragmentManager fm = getFragmentManager();
        Fragment af = fm.findFragmentByTag(CreatePanelFragment.FGMT_TAG);
        FragmentTransaction ft = fm.beginTransaction();

        if (af != null) {

            if(af instanceof CreatePanelFragment) {
                CreatePanelFragment cpf = (CreatePanelFragment)af;
                addWorkTimeData(cpf.getWorkTimeData());
            }

            fm.popBackStack();
        } else {
            ft.add(R.id.fragment_container, new CreatePanelFragment(), CreatePanelFragment.FGMT_TAG)
              .addToBackStack(null)
              .commit();
        }

    }

    private void addWorkTimeData(WorkTime wt) {

        if(db != null) {
            db.addWorkTime(wt);
        }

    }
}
