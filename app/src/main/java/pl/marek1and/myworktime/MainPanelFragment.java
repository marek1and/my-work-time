package pl.marek1and.myworktime;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import pl.marek1and.myworktime.dc.DigitalClockUpdateTask;
import pl.marek1and.myworktime.dc.DigitalClockUpdater;

public class MainPanelFragment extends Fragment implements DigitalClockUpdater {

    private TextView digitalClockValue;
    private DigitalClockUpdateTask digitalClockUpdaterThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fgmt_main, container);
        digitalClockValue = (TextView)v.findViewById(R.id.digital_clock);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        digitalClockUpdaterThread = new DigitalClockUpdateTask(this);
        digitalClockUpdaterThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        digitalClockUpdaterThread.cancelTask();
    }

    @Override
    public void updateDigitalClock() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                digitalClockValue.setText(sdf.format(new Date()));
            }
        });
    }
}
