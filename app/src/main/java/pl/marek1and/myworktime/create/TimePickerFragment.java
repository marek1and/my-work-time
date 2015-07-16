package pl.marek1and.myworktime.create;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimeType type;
    private TimePickerListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Bundle args = getArguments();
        type = TimeType.values()[args.getInt("TIME_TYPE")];
        long time = args.getLong("TIME");
        if(time > 0) {
            c.setTimeInMillis(time);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }
        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void setTimePickerListener(TimePickerListener listener) {
        this.listener = listener;
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(listener != null) {
            listener.onTimeSet(type, hourOfDay, minute);
        }
    }
}
