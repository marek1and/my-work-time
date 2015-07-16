package pl.marek1and.myworktime.db.schema;

import android.provider.BaseColumns;

import pl.marek1and.myworktime.db.structure.Column;
import pl.marek1and.myworktime.db.structure.Constraint;
import pl.marek1and.myworktime.db.structure.Table;

public class TransportsMappingTable extends Table {

    public static class Columns implements BaseColumns {
        public static final String W_ID = "W_ID";
        public static final String T_ID = "T_ID";
    }

    public static final String TABLE_NAME = "transport_mapping";

    public TransportsMappingTable() {
        super(TABLE_NAME);

        Column workTimeId = new Column(Columns.W_ID, "INTEGER");
        Column workTimeIdRef = new Column(WorkTimeTable.Columns._ID);
        addColumn(workTimeId);

        Column transportId = new Column(Columns.T_ID, "INTEGER");
        Column transportIdRef = new Column(Columns._ID);
        addColumn(transportId);

        Constraint pk = new Constraint(TABLE_NAME + "_PK", Constraint.ConstraintType.PRIMARY_KEY);
        pk.putColumn(workTimeId, workTimeIdRef);
        pk.putColumn(transportId, transportIdRef);
        addConstraint(pk);

//        Table transportRefTable = new Table(TransportTable.TABLE_NAME);
        Table worktimeRefTable = new Table(WorkTimeTable.TABLE_NAME);

        Constraint fk1 = new Constraint(TABLE_NAME + "_FK1", Constraint.ConstraintType.FOREIGN_KEY);
        fk1.putColumn(workTimeId, workTimeIdRef);
        fk1.setRefTable(worktimeRefTable);
        addConstraint(fk1);

//        Constraint fk2 = new Constraint(TABLE_NAME + "_FK2", Constraint.ConstraintType.FOREIGN_KEY);
//        fk2.putColumn(transportId, transportIdRef);
//        fk2.setRefTable(transportRefTable);
//        addConstraint(fk2);
    }

}
