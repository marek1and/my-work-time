package pl.marek1and.myworktime.db.structure;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class View implements DBObject {

    private String name;
    private String query;

    public View(String name) {
        this.name = name;
    }

    public View(String name, String query) {
        this(name);
        this.query = query;
    }

    public String getCreateQuery() {
        return query;
    }

    public void setCreateQuery(String query) {
        this.query = query;
    }

    public String getDropQuery() {
        return String.format("DROP VIEW %S;", name);
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
