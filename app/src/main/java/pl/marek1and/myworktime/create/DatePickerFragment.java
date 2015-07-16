package pl.marek1and.myworktime.create;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private TimeType type;
    private DatePickerListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        Bundle args = getArguments();
        type = TimeType.values()[args.getInt("TIME_TYPE")];
        long time = args.getLong("TIME");
        if(time > 0) {
            c.setTimeInMillis(time);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.listener = listener;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(listener != null) {
            listener.onDateSet(type, year, month, day);
        }
    }
}