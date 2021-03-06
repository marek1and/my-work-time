package pl.marek1and.myworktime.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.marek1and.myworktime.db.DateConversion;
import pl.marek1and.myworktime.db.beans.Note;
import pl.marek1and.myworktime.db.beans.Transport;
import pl.marek1and.myworktime.db.beans.WorkTime;

import static pl.marek1and.myworktime.db.schema.WorkTimeView.VIEW_NAME;

class WorkTimeViewDao {

    private SQLiteDatabase db;

    public WorkTimeViewDao(SQLiteDatabase db) {
        this.db = db;
    }

    public static String getSinceStartOfWeekQuery(String table) {
        return String.format("SELECT * FROM %s WHERE STARTTIME >= date('now', 'weekday 1', '-7 day')", table);
    }

    public static String getSinceStartOfMonthQuery(String table) {
        return String.format("SELECT * FROM %s WHERE STARTTIME >= date('now', 'start of month')", table);
    }

    public static String getSinceStartOfYearQuery(String table) {
        return String.format("SELECT * FROM %s WHERE STARTTIME >= date('now', 'start of year')", table);
    }

    public static String getBetweenQuery(String table) {
        return String.format("SELECT * FROM %s WHERE STARTTIME >= ? AND STARTTIME < ?", table);
    }

    public static String getCurrentWorkTimeQuery(String table) {
        return String.format("SELECT * FROM %s WHERE ENDTIME IS NULL ORDER BY STARTTIME DESC LIMIT 1", table);
    }

    public static String getActualWorkTimesQuery(String table) {
        return String.format("SELECT * from %s WHERE (CURRENT_TIMESTAMP BETWEEN STARTTIME AND ENDTIME) OR (STARTTIME <= CURRENT_TIMESTAMP AND ENDTIME IS NULL)", table);
    }

    public WorkTime get(Object id) {
        String query = String.format("SELECT * FROM %S WHERE W_ID = ?;", VIEW_NAME);
        List<WorkTime> w = getByQuery(query, String.valueOf(id));

        WorkTime workTime = null;
        if(!w.isEmpty()) {
            workTime = w.get(0);
        }

        return workTime;
    }

    public List<WorkTime> getSinceStartOfWeek() {
        return getByQuery(getSinceStartOfWeekQuery(VIEW_NAME));
    }

    public List<WorkTime> getSinceStartOfMonth() {
        return getByQuery(getSinceStartOfMonthQuery(VIEW_NAME));
    }

    public List<WorkTime> getSinceStartOfYear() {
        return getByQuery(getSinceStartOfYearQuery(VIEW_NAME));
    }

    public List<WorkTime> getBetween(Date starttime, Date endtime) {
        return getByQuery(getBetweenQuery(VIEW_NAME),
                          DateConversion.formatDateTime(starttime),
                          DateConversion.formatDateTime(endtime));
    }

    public WorkTime getCurrentWorkTime() {
        List<WorkTime> w = getByQuery(getCurrentWorkTimeQuery(VIEW_NAME));

        WorkTime workTime = null;
        if(!w.isEmpty()) {
            workTime = w.get(0);
        }

        return workTime;
    }

    public List<WorkTime> getActualWorkTimes() {
        return getByQuery(getActualWorkTimesQuery(VIEW_NAME));
    }

    public List<WorkTime> getAll() {
        String query = String.format("SELECT * FROM %s", VIEW_NAME);
        return getByQuery(query);
    }

    private  List<WorkTime> getByQuery(String query, String ... params) {
        List<WorkTime> objects = new ArrayList<WorkTime>();
        Cursor c = db.rawQuery(query, params);
        if(c.moveToFirst()) {
            do {
                buildObjectWithDependencies(c, objects);
            } while(c.moveToNext());
        }
        if(!c.isClosed()) {
            c.close();
        }
        return objects;
    }

    protected WorkTime buildObjectWithDependencies(Cursor c, List<WorkTime> objects) {

        WorkTime wt = WorkTimeDao.buildWorkTimeFromCursor(c, 0);
        Note n = NoteDao.buildNoteFromCursor(c, 5);
        int tId = c.getInt(4);

        boolean added = false;
        for(WorkTime w: objects) {
            if (w.equals(wt)) {
                added = true;
                wt = w;
                break;
            }
        }
        if (!added) {
            objects.add(wt);
        }

        if(tId > -1) {
            wt.addTransport(Transport.values()[tId]);
        }
        wt.addNote(n);
        return wt;
    }

}
