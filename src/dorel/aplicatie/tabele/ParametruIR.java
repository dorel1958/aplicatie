package dorel.aplicatie.tabele;

public class ParametruIR {
    
    private final String tabela;
    private final String conditia;
    private final String mesajEroare;

    public String getTabela() {
        return tabela;
    }

    public String getConditia() {
        return conditia;
    }

    public String getMesajEroare() {
        return mesajEroare;
    }
    
    public ParametruIR(String tabela, String conditia, String mesajEroare) {
        this.tabela = tabela;
        this.conditia = conditia;
        this.mesajEroare=mesajEroare;
    }

}
