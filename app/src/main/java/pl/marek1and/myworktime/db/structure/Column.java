package pl.marek1and.myworktime.db.structure;

public class Column {

    private String name;
    private String declaration;
    private Table table;

    public Column(String name) {
        this.name = name;
    }

    public Column(String name, String declaration) {
        this(name);
        this.declaration = declaration;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeclaration() {
        return declaration;
    }

    public void setDeclaration(String declaration) {
        this.declaration = declaration;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getDefinition() {
        return String.format("%S %S", getName(), getDeclaration());
    }

    @Override
    public boolean equals(Object o) {
        Column col = (Column)o;
        return getName().equals(col.getName());
    }

}
