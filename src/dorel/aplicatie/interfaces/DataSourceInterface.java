package dorel.aplicatie.interfaces;

import dorel.aplicatie.tabele.clase.UserCurent;
import java.sql.ResultSet;
import java.util.List;

public interface DataSourceInterface {

    public boolean isEroare();

    public String getMesajEroare();

    public boolean saveInreg(TabelaSqlInterface tabela);

    public boolean deleteInreg(TabelaSqlInterface tabela);

    public List<TabelaSqlInterface> listInreg(String numeTabela, int id, String orderBy, String filter);

    public TabelaSqlInterface getInreg(String numeTabela, int id);

    public void genereazaTabele(PopuleazaInterface populeaza);

    public boolean testParola(String user, String password, UserCurent userCurent);

    public ResultSet getResultSet();

    public boolean executaComanda(String comanda);

    public boolean executaComandaRs(String comanda);
}
