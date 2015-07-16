package pl.marek1and.myworktime.db.dao;

import java.util.List;

public interface Dao<T> {

    public long save(T obj);
    public boolean update(T obj);
    public boolean delete(T obj);
    public T get(Object id);
    public List<T> getAll();

}
