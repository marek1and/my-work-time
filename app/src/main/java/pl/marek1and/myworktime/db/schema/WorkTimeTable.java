package pl.marek1and.myworktime.db.schema;

import android.provider.BaseColumns;

import pl.marek1and.myworktime.db.structure.Column;
import pl.marek1and.myworktime.db.structure.Table;

public class WorkTimeTable extends Table {

    public static class Columns implements BaseColumns {
        public static final String STARTTIME = "starttime";
        public static final String ENDTIME = "endtime";
        public static final String TYPE = "type";
    }

    public static final String TABLE_NAME = "worktime";

    public WorkTimeTable() {
        super(TABLE_NAME);
        addColumn(new Column(Columns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT"));
        addColumn(new Column(Columns.STARTTIME, "TIMESTAMP NOT NULL"));
        addColumn(new Column(Columns.ENDTIME, "TIMESTAMP"));
        addColumn(new Column(Columns.TYPE, "TEXT"));
    }

}
