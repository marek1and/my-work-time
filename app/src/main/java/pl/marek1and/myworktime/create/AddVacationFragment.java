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

public class AddVacationFragment extends AbstractCreateEventFragment implements View.OnClickListener {

    private TextView tvStartDate;
    private TextView tvEndDate;

    public AddVacationFragment() {
        super(WorkTime.Type.VACATION);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startDateTime = Calendar.getInstance();
        clearTime(startDateTime);

        endDateTime = Calendar.getInstance();
        clearTime(endDateTime);
        endDateTime.add(Calendar.SECOND, -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fgmt_create_add_vacation, container, false);

        tvStartDate = (TextView) v.findViewById(R.id.tv_startdate_val);
        tvEndDate = (TextView) v.findViewById(R.id.tv_enddate_val);

        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);

        updateDateTextView(TimeType.START);
        updateDateTextView(TimeType.END);

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

}
