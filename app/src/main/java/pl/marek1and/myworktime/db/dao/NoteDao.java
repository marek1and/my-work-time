package pl.marek1and.myworktime.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import pl.marek1and.myworktime.db.DateConversion;
import pl.marek1and.myworktime.db.beans.Note;
import pl.marek1and.myworktime.db.beans.WorkTime;

import static pl.marek1and.myworktime.db.schema.NotesTable.Columns.MTIME;
import static pl.marek1and.myworktime.db.schema.NotesTable.Columns.NOTE;
import static pl.marek1and.myworktime.db.schema.NotesTable.Columns.TITLE;
import static pl.marek1and.myworktime.db.schema.NotesTable.Columns.W_ID;
import static pl.marek1and.myworktime.db.schema.NotesTable.Columns._ID;
import static pl.marek1and.myworktime.db.schema.NotesTable.TABLE_NAME;


public class NoteDao extends AbstractDao<Note> {

    public NoteDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public long save(Note obj) {
        long id = super.save(obj);
        obj.setId(id);
        return id;
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
    protected String getDefaultColumnValue(Note note) {
        return String.valueOf(note.getId());
    }

    @Override
    protected ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(W_ID, note.getWorkTime().getId());
        values.put(TITLE, note.getTitle());
        values.put(NOTE, note.getNote());
        values.put(MTIME, DateConversion.formatDateTime(new Date()));
        return values;
    }

    @Override
    protected Note buildObject(Cursor c) {
        return buildNoteFromCursor(c, 0);
    }

    public static Note buildNoteFromCursor(Cursor c, int colOffset) {
        Note note = null;
        if(c != null) {
            note = new Note();
            Long noteId = c.getLong(0 + colOffset);
            if(noteId != null) {
                note.setId(noteId);
                note.setTitle(c.getString(1 + colOffset));
                note.setNote(c.getString(2 + colOffset));
                note.setModifyTime(DateConversion.parseDate(c.getString(3 + colOffset)));

                if (colOffset == 0) {
                    //Creating temporary object with ID value only
                    WorkTime w = new WorkTime();
                    w.setId(c.getLong(4 + colOffset));
                    note.setWorkTime(w);
                }
            }
        }
        return note;
    }

    public List<Note> getWorkTimeNotes(WorkTime workTime) {
        List<Note> notes = getByColumnValue(W_ID, workTime.getId());
        for(Note n: notes) {
            n.setWorkTime(workTime);
        }
        return notes;
    }

    public boolean deleteAllWorkTimeNotes(WorkTime workTime) {
        int deletedRows = db.delete(TABLE_NAME,
                W_ID + " = ?",
                new String[]{String.valueOf(workTime.getId())});
        return deletedRows > 0;
    }

}
