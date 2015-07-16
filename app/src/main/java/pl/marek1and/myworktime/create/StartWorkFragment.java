package pl.marek1and.myworktime.create;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import pl.marek1and.myworktime.R;
import pl.marek1and.myworktime.db.beans.WorkTime;

public class StartWorkFragment extends AbstractCreateEventFragment implements View.OnClickListener{

    private TextView tvStartTime;

    public StartWorkFragment() {
        super(WorkTime.Type.NORMAL);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startDateTime = Calendar.getInstance();
        startDateTime.clear(Calendar.SECOND);
        startDateTime.clear(Calendar.MILLISECOND);

        int unroundedMinutes = startDateTime.get(Calendar.MINUTE);
        int mod = unroundedMinutes % 5;
        if(mod > 0) {
            startDateTime.add(Calendar.MINUTE, 5 - mod);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fgmt_create_start_work, container, false);

        tvStartTime = (TextView) v.findViewById(R.id.tv_starttime_val);
        tvStartTime.setOnClickListener(this);
        updateTimeTextView(TimeType.START);

        return v;
    }

    @Override
    public void onClick(View v) {
        DialogFragment df = null;
        TimeType type = null;
        long time = 0;
        switch(v.getId()) {
            case R.id.tv_starttime_val:
                TimePickerFragment tpfStart = new TimePickerFragment();
                tpfStart.setTimePickerListener(this);
                df = tpfStart;
                type = TimeType.START;
                time = startDateTime.getTimeInMillis();
                break;
        }
        if(df != null) {
            Bundle args = new Bundle();
            args.putInt("TIME_TYPE", type.ordinal());
            args.putLong("TIME", time);
            df.setArguments(args);
            df.show(getFragmentManager(), "dateOrTimePicker");
        }
    }

    @Override
    public void onTimeSet(TimeType type, int hourOfDay, int minute) {
        super.onTimeSet(type, hourOfDay, minute);
        updateTimeTextView(type);
    }

    private void updateTimeTextView(TimeType type) {
        switch(type) {
            case START:
                tvStartTime.setText(TIME_FORMATTER.format(startDateTime.getTime()));
                break;
        }
    }

}
