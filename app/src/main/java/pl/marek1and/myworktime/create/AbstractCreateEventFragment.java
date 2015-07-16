package pl.marek1and.myworktime.create;

import android.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.marek1and.myworktime.db.beans.WorkTime;

public abstract class AbstractCreateEventFragment extends Fragment implements TimePickerListener, DatePickerListener  {

    public static final String TAG = "create_event_fragment";

    protected static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");
    protected static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");

    protected Calendar startDateTime;
    protected Calendar endDateTime;
    protected WorkTime.Type type;

    public AbstractCreateEventFragment(WorkTime.Type type) {
        this.type = type;
    }

    @Override
    public void onDateSet(TimeType type, int year, int month, int day) {
        Calendar cal = getCalendarByType(type);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
    }

    @Override
    public void onTimeSet(TimeType type, int hourOfDay, int minute) {
        Calendar cal = getCalendarByType(type);
        cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
    }

    protected Calendar getCalendarByType(TimeType type) {
        Calendar cal = null;
        switch(type) {
            case START:
                cal = startDateTime;
                break;
            case END:
                cal = endDateTime;
                break;
        }
        return cal;
    }

    protected void clearTime(Calendar cal) {
        cal.clear(Calendar.HOUR_OF_DAY);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
    }

    public Date getStartDateTime() {
        return startDateTime.getTime();
    }

    public Date getEndDateTime() {
        if(endDateTime == null) {
            return null;
        }
        return endDateTime.getTime();
    }

    public WorkTime.Type getType() {
        return type;
    }

}
