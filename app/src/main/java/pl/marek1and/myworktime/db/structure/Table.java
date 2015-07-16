package pl.marek1and.myworktime.db.structure;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Table implements DBObject {

    private String name;
    private List<Column> columns;
    private List<Constraint> constraints;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, List<Column> columns) {
        this(name);
        this.columns = columns;
    }

    public Table(String name, List<Column> columns, List<Constraint> constraints) {
        this(name, columns);
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public void addColumn(Column column) {
        if(columns == null) {
            columns = new ArrayList<Column>();
        }
        if (!isColumnExist(column)) {
            columns.add(column);
            column.setTable(this);
        }
    }

    public boolean isColumnExist(Column column) {
        boolean exist = false;
        if(columns != null && column != null) {
            for(Column c: columns) {
                if(c.equals(column)) {
                    exist = true;
                    break;
                }
            }
        }
        return exist;
    }

    public void addConstraint(Constraint constraint) {
        if(constraints == null) {
            constraints = new ArrayList<Constraint>();
        }
        constraints.add(constraint);
        constraint.setTable(this);
    }

    public String getCreateQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(getName().toUpperCase());
        sb.append(" (");
        Iterator<Column> iterCol = columns.iterator();
        while(iterCol.hasNext()) {
            Column c = iterCol.next();
            sb.append(c.getDefinition());
            if(iterCol.hasNext() || (constraints !=null && !constraints.isEmpty())) {
                sb.append(",\n");
            }
        }

        if(constraints != null) {
            Iterator<Constraint> iterCon = constraints.iterator();
            while (iterCon.hasNext()) {
                Constraint c = iterCon.next();
                sb.append(c.getDefinition());
                if (iterCon.hasNext()) {
                    sb.append(",\n");
                }
            }
        }
        sb.append(");");
        return sb.toString();
    }

    public String getDropQuery() {
        return String.format("DROP TABLE IF EXISTS %S;", getName());
    }

    public String getClearTableDataQuery() {
        return String.format("TRUNCATE TABLE IF EXISTS %S;", getName());
    }

    @Override
    public void create(SQLiteDatabase db){
        db.execSQL(getCreateQuery());
        Log.d("DB", getCreateQuery());
    }

    @Override
    public void upgrade(SQLiteDatabase db){
        db.execSQL(getDropQuery());
        Log.d("DB", getDropQuery());
        create(db);
    }

}
