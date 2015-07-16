package pl.marek1and.myworktime.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDao<T> implements Dao<T> {

    protected SQLiteDatabase db;

    protected AbstractDao(SQLiteDatabase db) {
        this.db = db;
    }

    @Override
    public long save(T obj) {
        return db.insert(getTableName(), null, getContentValues(obj));
    }

    @Override
    public boolean update(T obj) {
        int updatedRows = db.update(getTableName(),
                                    getContentValues(obj),
                                    getDefaultColumn() + " = ?",
                                    new String[]{getDefaultColumnValue(obj)});
        return updatedRows > 0;
    }

    @Override
    public boolean delete(T obj) {
        int deletedRows = db.delete(getTableName(),
                                    getDefaultColumn() + " = ?",
                                    new String[]{getDefaultColumnValue(obj)});
        return deletedRows > 0;
    }

    @Override
    public T get(Object id) {
        T obj = null;
        Cursor c = db.query(getTableName(),
                            null,
                            getDefaultColumn() + " = ?",
                            new String[]{String.valueOf(id)},
                            null, null ,null, "1");
        if(c.moveToFirst()) {
            obj = buildObject(c);
        }
        if(!c.isClosed()){
            c.close();
        }
        return obj;
    }

    @Override
    public List<T> getAll() {
        return getByQuery(String.format("SELECT * FROM %S;", getTableName()));
    }

    public List<T> getByQuery(String query) {
        return getByQuery(query, new String[]{});
    }

    public List<T> getByQuery(String query, String ... params) {
        List<T> objects = new ArrayList<T>();
        Cursor c = db.rawQuery(query, params);
        if(c.moveToFirst()) {
            do {
                T o = buildObject(c);
                if(o != null) {
                    objects.add(o);
                }
            } while(c.moveToNext());
        }
        if(!c.isClosed()) {
            c.close();
        }
        return objects;
    }

    public List<T> getByColumnValue(String column, Object value) {
        List<T> objects = new ArrayList<T>();
        Cursor c = db.query(getTableName(),
                null,
                column + " = ?",
                new String[]{String.valueOf(value)},
                null, null ,null, null);
        if(c.moveToFirst()) {
            do {
                T o = buildObject(c);
                if(o != null) {
                    objects.add(o);
                }
            } while(c.moveToNext());
        }
        if(!c.isClosed()){
            c.close();
        }
        return objects;
    }

    protected abstract String getTableName();
    protected abstract String getDefaultColumn();
    protected abstract String getDefaultColumnValue(T obj);
    protected abstract ContentValues getContentValues(T obj);
    protected abstract T buildObject(Cursor c);

}
