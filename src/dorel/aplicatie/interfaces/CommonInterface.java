package dorel.aplicatie.interfaces;

import dorel.aplicatie.tabele.clase.UserCurent;
import dorel.basicopp.ConfigLocalInterface;
import java.util.List;

public interface CommonInterface {

    public List<TabelaSqlInterface> getListaTabele();

    public ConfigLocalInterface getConfigLocal();

    public UserCurent getUserCurent();

    public DataSourceInterface getDataSource();

    public TabelaSqlFactoryInterface getTabelaSqlFactory();

    public TabelaSqlInterface getTabela(String numeTabela);

    public String getSetare(String denumire);

}
