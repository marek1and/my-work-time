package pl.marek1and.myworktime.db.schema;

import android.provider.BaseColumns;

import pl.marek1and.myworktime.db.structure.Column;
import pl.marek1and.myworktime.db.structure.Constraint;
import pl.marek1and.myworktime.db.structure.Table;

public class NotesTable extends Table {

    public static class Columns implements BaseColumns {
        public static final String W_ID = "W_ID";
        public static final String TITLE = "TITLE";
        public static final String NOTE = "NOTE";
        public static final String MTIME = "MTIME";
    }

    public static final String TABLE_NAME = "notes";

    public NotesTable() {
        super(TABLE_NAME);
        addColumn(new Column(Columns._ID, "INTEGER PRIMARY KEY AUTOINCREMENT"));
        addColumn(new Column(Columns.TITLE, "TEXT"));
        addColumn(new Column(Columns.NOTE, "TEXT"));
        addColumn(new Column(Columns.MTIME, "TIMESTAMP DEFAULT (datetime('now','localtime'))"));

        Column workTimeId = new Column(Columns.W_ID, "INTEGER");
        Column workTimeIdRef = new Column(WorkTimeTable.Columns._ID);
        addColumn(workTimeId);

        Table worktimeRefTable = new Table(WorkTimeTable.TABLE_NAME);

        Constraint fk = new Constraint(TABLE_NAME + "_FK1", Constraint.ConstraintType.FOREIGN_KEY);
        fk.putColumn(workTimeId, workTimeIdRef);
        fk.setRefTable(worktimeRefTable);
        addConstraint(fk);
    }

}
