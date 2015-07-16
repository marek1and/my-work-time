package pl.marek1and.myworktime.db.structure;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Constraint {

    public static enum ConstraintType {
        PRIMARY_KEY("PRIMARY KEY"),
        FOREIGN_KEY("FOREIGN KEY");

        private String keywords;

        private ConstraintType(String keywords) {
            this.keywords = keywords;
        }

        public String getKeywords() {
            return keywords;
        }
    }

    private String name;
    private ConstraintType type;
    private Table table;
    private Map<Column, Column> columns;
    private Table refTable;

    public Constraint(String name, ConstraintType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConstraintType getType() {
        return type;
    }

    public void setType(ConstraintType type) {
        this.type = type;
    }

    public void putColumn(Column local, Column reference) {
        if(columns == null) {
            columns = new LinkedHashMap<Column, Column>();
        }
        columns.put(local, reference);
    }

    public void putColumn(Column local) {
        putColumn(local, null);
    }

    public Table getRefTable() {
        return refTable;
    }

    public void setRefTable(Table refTable) {
        this.refTable = refTable;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getDefinition() {
        StringBuilder sb = new StringBuilder();
        if(name != null) {
            sb.append(String.format("CONSTRAINT %S ", name));
        }
        sb.append(type.getKeywords());
        if(columns != null && !columns.isEmpty()) {
            sb.append("(");
            Iterator<Column> iter = columns.keySet().iterator();
            while(iter.hasNext()) {
                Column c = iter.next();
                sb.append(c.getName().toUpperCase());
                if(iter.hasNext()) {
                    sb.append(", ");
                }
            }
            sb.append(")");
            if(type.equals(ConstraintType.FOREIGN_KEY)) {
                sb.append(String.format(" REFERENCES %S (", getRefTable().getName()));
                iter = columns.values().iterator();
                while(iter.hasNext()) {
                    Column c = iter.next();
                    sb.append(c.getName().toUpperCase());
                    if(iter.hasNext()) {
                        sb.append(", ");
                    }
                }
                sb.append(")");
            }
        }
        return sb.toString();
    }
}
