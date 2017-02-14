package dorel.aplicatie.interfaces;

public interface TabelaSqlFactoryInterface {

    public TabelaSqlInterface getTabela(String numeTabela, CommonInterface common);

    public TabelaSqlInterface getTabelaUser(CommonInterface common, boolean eAdmin);

    public TabelaSqlInterface getTabelaSetari(CommonInterface common);
}
