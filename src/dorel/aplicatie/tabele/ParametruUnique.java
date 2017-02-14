package dorel.aplicatie.tabele;

public class ParametruUnique {

    private final String tableName;
    private final String conditia;
    private final int id;
    private final String mesajEroare;

    public String getTableName() {
        return tableName;
    }

    public String getConditia() {
        return conditia;
    }

    public int getId() {
        return id;
    }
    
    public String getMesajEroare(){
        return mesajEroare;
    }

    public ParametruUnique(String tableName, String conditia, int id, String mesajEroare) {
        this.tableName = tableName;
        this.conditia = conditia;
        this.id = id;
        this.mesajEroare=mesajEroare;
    }

}
