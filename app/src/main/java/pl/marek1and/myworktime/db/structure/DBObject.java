package pl.marek1and.myworktime.db.structure;

import android.database.sqlite.SQLiteDatabase;

public interface DBObject {

    public void create(SQLiteDatabase db);
    public void upgrade(SQLiteDatabase db);

}
