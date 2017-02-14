package dorel.aplicatie;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.DataSourceInterface;
import dorel.aplicatie.interfaces.TabelaSqlFactoryInterface;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import dorel.aplicatie.tabele.clase.UserCurent;
import dorel.basicopp.ConfigLocalHelper;
import dorel.basicopp.ConfigLocalInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CommonHelper implements CommonInterface {

    protected List<TabelaSqlInterface> tabele;
    protected ConfigLocalInterface setLocal;
    protected UserCurent userCurent;
    protected DataSourceInterface dataSource;
    protected TabelaSqlFactoryInterface tabelaSqlFactory;

    public CommonHelper() {
        setLocal = new ConfigLocalHelper("MySQL");
        setLocal.readSettings();
        tabele = new ArrayList<>();
        // Nu populez AICI lista tabelelor
        userCurent = new UserCurent("", "", "");
        // NU aici dataSource = new DataSourceHelper(this, true); // AICI creaza BD daca nu este + tabela users
        // NU initializez aici tabelaSqlFactory;
    }

    @Override
    public List<TabelaSqlInterface> getListaTabele() {
        return tabele;
    }

    @Override
    public ConfigLocalInterface getConfigLocal() {
        return setLocal;
    }

    @Override
    public UserCurent getUserCurent() {
        return userCurent;
    }

    @Override
    public DataSourceInterface getDataSource() {
        return dataSource;
    }

    @Override
    public TabelaSqlFactoryInterface getTabelaSqlFactory() {
        if (tabelaSqlFactory == null) {
            JOptionPane.showMessageDialog(null, "CommonHelper.TabelaSqlFactoryInterface NU a fost initializata in ACEASTA aplicatie");
        }
        return tabelaSqlFactory;
    }

    @Override
    public TabelaSqlInterface getTabela(String numeTabela) {
        for (TabelaSqlInterface tabela : tabele) {
            if (tabela.getNumeTabela().equals(numeTabela)) {
                return tabela;
            }
        }
        JOptionPane.showMessageDialog(null, "CommonHelper.getTabela - Tabela cu nume necunoscut:" + numeTabela);
        return null;
    }

    @Override
    public String getSetare(String denumire) {
        // exemplu: String cotaTVA = common.getSetare("cota_tva");
        List<TabelaSqlInterface> listaValori = dataSource.listInreg("setari", 0, "", "");
        for (TabelaSqlInterface rand : listaValori) {
            if (rand.getColoane().getValoare("denumire").equals(denumire)) {
                return rand.getColoane().getValoare("valoare");
            }
        }
        return "";
    }
}
