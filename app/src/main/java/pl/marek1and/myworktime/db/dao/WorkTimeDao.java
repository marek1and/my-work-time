package pl.marek1and.myworktime.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import pl.marek1and.myworktime.db.DateConversion;
import pl.marek1and.myworktime.db.beans.Note;
import pl.marek1and.myworktime.db.beans.Transport;
import pl.marek1and.myworktime.db.beans.WorkTime;
import pl.marek1and.myworktime.db.schema.WorkTimeTable.Columns;

import static pl.marek1and.myworktime.db.dao.FetchType.EAGER;
import static pl.marek1and.myworktime.db.dao.FetchType.LAZY;
import static pl.marek1and.myworktime.db.schema.WorkTimeTable.Columns._ID;
import static pl.marek1and.myworktime.db.schema.WorkTimeTable.TABLE_NAME;
import static pl.marek1and.myworktime.db.schema.WorkTimeView.VIEW_NAME;

public class WorkTimeDao extends AbstractDao<WorkTime> {

    private WorkTimeViewDao workTimeViewDao;
    private TransportMappingDao transportMappingDao;
    private NoteDao noteDao;

    private FetchType fetchType = EAGER;

    public WorkTimeDao(SQLiteDatabase db) {
        super(db);
        workTimeViewDao = new WorkTimeViewDao(db);
        transportMappingDao = new TransportMappingDao(db);
        noteDao = new NoteDao(db);
    }

    public WorkTimeDao(SQLiteDatabase db, FetchType fetchType) {
        this(db);
        this.fetchType = fetchType;
    }

    public void setFetchType(FetchType fetchType) {
        this.fetchType = fetchType;
    }

    @Override
    public long save(WorkTime obj) {
        db.beginTransaction();
        long id = 0;
        try {
            id = super.save(obj);
            obj.setId(id);
            transportMappingDao.updateMappings(obj);
            for(Note n: obj.getNotes()){
                noteDao.save(n);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return id;
    }

    @Override
    public boolean update(WorkTime obj) {
        int updatedRows = 0;
        db.beginTransaction();
        try {
            super.update(obj);
            transportMappingDao.updateMappings(obj);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return updatedRows > 0;
    }

    @Override
    public boolean delete(WorkTime workTime) {
        boolean deleted = false;
        db.beginTransaction();
        try {
            noteDao.deleteAllWorkTimeNotes(workTime);
            transportMappingDao.delete(workTime);
            deleted = super.delete(workTime);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return deleted;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getDefaultColumn() {
        return _ID;
    }

    @Override
    protected String getDefaultColumnValue(WorkTime workTime) {
        return String.valueOf(workTime.getId());
    }

    @Override
    protected ContentValues getContentValues(WorkTime workTime) {
        final ContentValues values = new ContentValues();
        values.put(Columns.STARTTIME, DateConversion.formatDateTime(workTime.getStartTime()));
        values.put(Columns.ENDTIME, DateConversion.formatDateTime(workTime.getEndTime()));
        values.put(Columns.TYPE, workTime.getType().name());
        return values;
    }

    @Override
    protected WorkTime buildObject(Cursor c) {
        return buildWorkTimeFromCursor(c, 0);
    }

    public static WorkTime buildWorkTimeFromCursor(Cursor c, int colOffset) {
        WorkTime workTime = null;
        if(c != null) {
            workTime = new WorkTime();
            workTime.setId(c.getLong(0 + colOffset));
            workTime.setStartTime(DateConversion.parseDate(c.getString(1 + colOffset)));
            workTime.setEndTime(DateConversion.parseDate(c.getString(2 + colOffset)));
            workTime.setType(WorkTime.Type.valueOf(c.getString(3 + colOffset)));
        }
        return workTime;
    }

    public List<WorkTime> getTransportWorkTimes(Transport transport) {

        String query =
                "SELECT " +
                        "W._ID AS W_ID, " +
                        "W.STARTTIME, " +
                        "W.ENDTIME, " +
                        "W.TYPE " +
                "FROM WORKTIME W " +
                "JOIN TRANSPORT_MAPPING TM ON W._ID = TM.W_ID " +
                "WHERE TM.T_ID = ?";
        return getByQuery(query, String.valueOf(transport.getId()));
    }

    @Override
    public WorkTime get(Object id) {
        if(LAZY.equals(fetchType)) {
            return super.get(id);
        } else {
            return workTimeViewDao.get(id);
        }
    }

    @Override
    public List<WorkTime> getAll() {
        if(LAZY.equals(fetchType)) {
            return super.getAll();
        } else {
            return workTimeViewDao.getAll();
        }
    }


    public List<WorkTime> getSinceStartOfWeek() {
        if(LAZY.equals(fetchType)) {
            return getByQuery(WorkTimeViewDao.getSinceStartOfWeekQuery(TABLE_NAME));
        } else {
            return workTimeViewDao.getSinceStartOfWeek();
        }
    }

    public List<WorkTime> getSinceStartOfMonth() {
        if(LAZY.equals(fetchType)) {
            return getByQuery(WorkTimeViewDao.getSinceStartOfMonthQuery(TABLE_NAME));
        } else {
            return workTimeViewDao.getSinceStartOfMonth();
        }
    }

    public List<WorkTime> getSinceStartOfYear() {
        if (LAZY.equals(fetchType)) {
            return getByQuery(WorkTimeViewDao.getSinceStartOfYearQuery(TABLE_NAME));
        } else {
            return workTimeViewDao.getSinceStartOfYear();
        }
    }

    public List<WorkTime> getBetween(Date starttime, Date endtime) {
        if (LAZY.equals(fetchType)) {
            return getByQuery(WorkTimeViewDao.getBetweenQuery(TABLE_NAME),
                    DateConversion.formatDateTime(starttime),
                    DateConversion.formatDateTime(endtime));
        } else {
            return workTimeViewDao.getBetween(starttime, endtime);
        }
    }

    public WorkTime getCurrentWorkTime() {
        if (LAZY.equals(fetchType)) {
            List<WorkTime> w = getByQuery(WorkTimeViewDao.getCurrentWorkTimeQuery(TABLE_NAME));

            WorkTime workTime = null;
            if (!w.isEmpty()) {
                workTime = w.get(0);
            }
            return workTime;
        } else {
            return workTimeViewDao.getCurrentWorkTime();
        }
    }

    public List<WorkTime> getActualWorkTimes() {
        if (LAZY.equals(fetchType)) {
            return getByQuery(WorkTimeViewDao.getActualWorkTimesQuery(TABLE_NAME));
        } else {
            return workTimeViewDao.getActualWorkTimes();
        }
    }

}
