package pl.marek1and.myworktime.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import pl.marek1and.myworktime.db.schema.NotesTable;
import pl.marek1and.myworktime.db.schema.TransportsMappingTable;
import pl.marek1and.myworktime.db.schema.WorkTimeTable;
import pl.marek1and.myworktime.db.schema.WorkTimeView;
import pl.marek1and.myworktime.db.structure.DBObject;

public class SQLiteDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "worktime.db";
    private static final int DATABASE_VERSION = 1;

    private List<DBObject> objects = new ArrayList<DBObject>();

    public SQLiteDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        createDBTablesList();
    }

    private void createDBTablesList() {
        objects.add(new WorkTimeTable());
        objects.add(new NotesTable());
        objects.add(new TransportsMappingTable());
        objects.add(new WorkTimeView());
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
            //db.setForeignKeyConstraintsEnabled(true);
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(DBObject object: objects) {
            object.create(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(DBObject object: objects) {
            object.upgrade(db);
        }
    }
}
