package dorel.aplicatie.swing;

import dorel.aplicatie.interfaces.CommonInterface;
import javax.swing.ComboBoxModel;

public class PatraticaControl {

    public enum TipControl {

        LABEL,
        TEXT,
        PASSWORD,
        CHECKBOX,
        COMBOBOX,
        REFERINTA
    }

    private TipControl tip;
    private String name;
    private String valoare;
    //
    private String label;
    private int dimX;
    private int dimY;
    //
    // pt referinta
    private CommonInterface common;
    private String numeTabela;

    // pt JComboBox
    private ComboBoxModel comboBoxModel;
    private MyListModel listModel;

    //<editor-fold defaultstate="collapsed" desc="Set Get">
    public int getDimY() {
        return dimY;
    }

    public void setDimY(int dimY) {
        this.dimY = dimY;
    }

    public int getDimX() {
        return dimX;
    }

    public void setDimX(int dimX) {
        this.dimX = dimX;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TipControl getTip() {
        return tip;
    }

    public void setTip(TipControl tip) {
        this.tip = tip;
    }

    public String getValoare() {
        return valoare;
    }

    public void setValoare(String valoare) {
        this.valoare = valoare;
    }

    public String getNumeTabela() {
        return numeTabela;
    }

    public void setNumeTabela(String numeTabela) {
        this.numeTabela = numeTabela;
    }

    public CommonInterface getCommon() {
        return common;
    }

    public void setCommon(CommonInterface common) {
        this.common = common;
    }

    public ComboBoxModel getComboBoxModel() {
        return comboBoxModel;
    }

    public void setComboBoxModel(ComboBoxModel comboBoxModel) {
        this.comboBoxModel = comboBoxModel;
    }

    public void setListModel(MyListModel listModel) {
        this.listModel = listModel;
    }

    public MyListModel getListModel() {
        return listModel;
    }
    //</editor-fold>
}
