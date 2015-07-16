package pl.marek1and.myworktime.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.marek1and.myworktime.db.beans.Transport;
import pl.marek1and.myworktime.db.beans.TransportMapping;
import pl.marek1and.myworktime.db.beans.WorkTime;
import pl.marek1and.myworktime.db.schema.TransportsMappingTable;

import static pl.marek1and.myworktime.db.schema.TransportsMappingTable.Columns.T_ID;
import static pl.marek1and.myworktime.db.schema.TransportsMappingTable.Columns.W_ID;
import static pl.marek1and.myworktime.db.schema.TransportsMappingTable.TABLE_NAME;

public class TransportMappingDao extends AbstractDao<TransportMapping> {

    public TransportMappingDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public boolean update(TransportMapping obj) {
        throw new UnsupportedOperationException("Unsupported operation.");
    }

    @Override
    public boolean delete(TransportMapping tm) {
        int deletedRows = 0;
            deletedRows = db.delete(TransportsMappingTable.TABLE_NAME,
                                    TransportsMappingTable.Columns.W_ID + " = ? AND " +
                                    TransportsMappingTable.Columns.T_ID + " = ?",
                                    new String[]{String.valueOf(tm.getWorkTimeId()),
                                    String.valueOf(tm.getTransportId())});
        return deletedRows > 0;
    }

    public boolean delete(WorkTime workTime, Transport transport) {
        TransportMapping tm = new TransportMapping(workTime.getId(), transport.getId());
        return delete(tm);
    }

    public boolean delete(WorkTime workTime) {
        TransportMapping tm = new TransportMapping(workTime.getId(), -1);
        return super.delete(tm);
    }

    @Override
    public TransportMapping get(Object id) {
        throw new UnsupportedOperationException("Unsupported operation for this object.");
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getDefaultColumn() {
        return W_ID;
    }

    @Override
    protected String getDefaultColumnValue(TransportMapping obj) {
        return String.valueOf(obj.getWorkTimeId());
    }

    @Override
    protected ContentValues getContentValues(TransportMapping obj) {
        final ContentValues values = new ContentValues();
        values.put(W_ID, obj.getWorkTimeId());
        values.put(T_ID, obj.getTransportId());
        return values;
    }

    @Override
    protected TransportMapping buildObject(Cursor c) {
        TransportMapping tm = null;
        if(c != null) {
            tm = new TransportMapping();
            tm.setWorkTimeId(c.getLong(0));
            tm.setTransportId(c.getLong(1));
        }
        return tm;
    }

    public boolean updateMappings(WorkTime workTime) {

        List<TransportMapping> currentMappings = getByColumnValue(W_ID, workTime.getId());
        List<TransportMapping> updatedMappings = workTime.getTransportMapping();

        List<TransportMapping> mappingsToDelete = determineMappings(currentMappings, updatedMappings);
        List<TransportMapping> newMappings = determineMappings(updatedMappings, currentMappings);

        boolean status = true;

        for(TransportMapping tm: mappingsToDelete) {
            status &= delete(tm);
        }

        for(TransportMapping tm: newMappings) {
            status &= (save(tm) > 0);
        }

        return status;
    }

    protected List<TransportMapping> determineMappings(List<TransportMapping> currentMapping, List<TransportMapping> updatedMapping) {
        List<TransportMapping> mappings = new ArrayList<TransportMapping>();

        for(TransportMapping curr : currentMapping) {
            boolean exists = false;
            for(TransportMapping n: updatedMapping) {
                if(curr.equals(n)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                mappings.add(curr);
            }
        }
        return mappings;
    }

}
