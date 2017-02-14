package dorel.aplicatie.interfaces;

import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.swing.ListaCopii;
import dorel.aplicatie.swing.MyPanel;
import dorel.aplicatie.swing.PanelButoane;
import dorel.aplicatie.tabele.ParametruIR;
import dorel.aplicatie.tabele.ParametruUnique;
import dorel.aplicatie.tabele.clase.Coloane;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

public interface TabelaSqlInterface {

    public String getWindowTitle();

    public void setId(int id);

    public int getId();

    public String getComandaCreate();

    public String getComandaSave();

    public String getComandaDelete();

    public List<ParametruUnique> getParametriUnique();

    public List<ParametruIR> getListaParametriIR();

    public String getActualFilter(String filter, int idFiltruReferinta, boolean eCopil);

    public void puneValoriInControale();

    public void iaValoriDinControale();

    public JPanel getPanel();

    public MyPanel getMyPanel();

    public int getNrMaxLiniiPanelCenter();

    public String getNumeTabela();

    public String getOrderBy();

    public void loadFromRs(ResultSet rs);

    public void setValuesFromTabela(TabelaSqlInterface tabela);

    public void puneValoriImplicite();

    public void puneValoriDinMap(Map<String, String> mapValori);

    public boolean testCorect();

    public String getNumeControlFocus();   // numele controlului pe care se pune focusul la Adaugare sau Editare

    public Coloane getColoane();

    public boolean genLista(String numeFis);

    public void setPanelEast(ListaCopii panelEast);

    public ListaCopii getPanelEast();

    public String getNumeTabelaFiltruReferinta();

    public int getWidthFiltruReferunta();

    public Dimension getDimListaW();

    public PanelButoane getPanelNorth(FerDialogTabela ferDialogTabela);
}
