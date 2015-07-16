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

public class AddWorkTimeFragment extends AbstractCreateEventFragment implements View.OnClickListener {

    private TextView tvStartDate;
    private TextView tvStartTime;
    private TextView tvEndDate;
    private TextView tvEndTime;

    public AddWorkTimeFragment() {
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

        endDateTime = Calendar.getInstance();
        endDateTime.add(Calendar.HOUR_OF_DAY, 8);
        endDateTime.set(Calendar.MINUTE, startDateTime.get(Calendar.MINUTE));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fgmt_create_add_worktime, container, false);

        tvStartDate = (TextView) v.findViewById(R.id.tv_startdate_val);
        tvStartTime = (TextView) v.findViewById(R.id.tv_starttime_val);
        tvEndDate = (TextView) v.findViewById(R.id.tv_enddate_val);
        tvEndTime = (TextView) v.findViewById(R.id.tv_endtime_val);

        tvStartDate.setOnClickListener(this);
        tvStartTime.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);

        updateDateTextView(TimeType.START);
        updateDateTextView(TimeType.END);
        updateTimeTextView(TimeType.START);
        updateTimeTextView(TimeType.END);

        return v;
    }

    @Override
    public void onClick(View v) {
        DialogFragment df = null;
        TimeType type = null;
        long time = 0;
        switch(v.getId()) {
            case R.id.tv_startdate_val:
                DatePickerFragment dpfStart = new DatePickerFragment();
                dpfStart.setDatePickerListener(this);
                df = dpfStart;
                type = TimeType.START;
                time = startDateTime.getTimeInMillis();
                break;
            case R.id.tv_enddate_val:
                DatePickerFragment dpfStop = new DatePickerFragment();
                dpfStop.setDatePickerListener(this);
                df = dpfStop;
                type = TimeType.END;
                time = endDateTime.getTimeInMillis();
                break;
            case R.id.tv_starttime_val:
                TimePickerFragment tpfStart = new TimePickerFragment();
                tpfStart.setTimePickerListener(this);
                df = tpfStart;
                type = TimeType.START;
                time = startDateTime.getTimeInMillis();
                break;
            case R.id.tv_endtime_val:
                TimePickerFragment tpfStop = new TimePickerFragment();
                tpfStop.setTimePickerListener(this);
                df = tpfStop;
                type = TimeType.END;
                time = endDateTime.getTimeInMillis();
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
    public void onDateSet(TimeType type, int year, int month, int day) {
        super.onDateSet(type, year, month, day);
        updateDateTextView(type);
    }

    @Override
    public void onTimeSet(TimeType type, int hourOfDay, int minute) {
        super.onTimeSet(type, hourOfDay, minute);
        updateTimeTextView(type);
    }

    private void updateDateTextView(TimeType type) {
        switch(type) {
            case START:
                tvStartDate.setText(DATE_FORMATTER.format(startDateTime.getTime()));
                break;
            case END:
                tvEndDate.setText(DATE_FORMATTER.format(endDateTime.getTime()));
                break;
        }
    }

    private void updateTimeTextView(TimeType type) {
        switch(type) {
            case START:
                tvStartTime.setText(TIME_FORMATTER.format(startDateTime.getTime()));
                break;
            case END:
                tvEndTime.setText(TIME_FORMATTER.format(endDateTime.getTime()));
                break;
        }
    }

}
