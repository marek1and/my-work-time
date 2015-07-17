package pl.marek1and.myworktime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.List;

import pl.marek1and.myworktime.db.beans.Note;
import pl.marek1and.myworktime.db.beans.WorkTime;
import pl.marek1and.myworktime.db.dao.FetchType;
import pl.marek1and.myworktime.db.dao.NoteDao;
import pl.marek1and.myworktime.db.dao.WorkTimeDao;

public class DatabaseManager {

    private Context context;
    private SQLiteDatabase db;
    private SQLiteOpenHelper openHelper;

    private WorkTimeDao workTimeDao;
    private NoteDao noteDao;

    public DatabaseManager(Context context) {
        this.context = context;
        open();
    }

    public void open() {
        openHelper = new SQLiteDBHelper(context);
        db = openHelper.getWritableDatabase();
        workTimeDao = new WorkTimeDao(db);
        noteDao = new NoteDao(db);
    }

    public void setLazyFetchType() {
        workTimeDao.setFetchType(FetchType.LAZY);
    }

    public void setEagerFetchType() {
        workTimeDao.setFetchType(FetchType.EAGER);
    }

    public void close() {
        openHelper.close();
    }

    public boolean isOpen(){
        return db.isOpen();
    }

    protected SQLiteDatabase getDatabase() {
        return db;
    }

    public void addWorkTime(WorkTime wt) {
        workTimeDao.save(wt);
    }

    public boolean deleteWorkTime(WorkTime wt) {
        return workTimeDao.delete(wt);
    }

    public boolean modifyWorkTime(WorkTime wt) {
        return workTimeDao.update(wt);
    }

    public WorkTime getWorkTime(Object id) {
        return workTimeDao.get(id);
    }

    public WorkTime getCurrentWorkTime() {
        return workTimeDao.getCurrentWorkTime();
    }

    public List<WorkTime> getActualWorkTimes() {
        return workTimeDao.getActualWorkTimes();
    }

    public List<WorkTime> getWorkTimes() {
        return workTimeDao.getAll();
    }

    public List<WorkTime> getSinceStartOfWeek() {
        return workTimeDao.getSinceStartOfWeek();
    }

    public List<WorkTime> getSinceStartOfMonth() {
        return workTimeDao.getSinceStartOfMonth();
    }

    public List<WorkTime> getSinceStartOfYear() {
        return workTimeDao.getSinceStartOfYear();
    }

    public List<WorkTime> getBetween(Date starttime, Date endtime) {
        return workTimeDao.getBetween(starttime, endtime);
    }

    public void addNote(Note note) {
        noteDao.save(note);
    }

    public boolean deleteNote(Note note) {
        return noteDao.delete(note);
    }

    public boolean deleteAllWorkTimeNotes(WorkTime workTime) {
        return noteDao.deleteAllWorkTimeNotes(workTime);
    }

    public boolean modifyNote(Note note) {
        return noteDao.update(note);
    }

    public Note getNote(long id) {
        return noteDao.get(id);
    }

    public List<Note> getNotes() {
        return noteDao.getAll();
    }

    public List<Note> getNotes(WorkTime workTime) {
        return noteDao.getWorkTimeNotes(workTime);
    }

}
