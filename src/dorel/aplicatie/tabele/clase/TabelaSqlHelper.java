package dorel.aplicatie.tabele.clase;

import dorel.aplicatie.actiuni.ActiuneButonComanda;
import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.swing.LinieControale;
import dorel.aplicatie.swing.MyPanel;
import dorel.aplicatie.swing.PatraticaControl;
import dorel.aplicatie.tabele.ParametruIR;
import dorel.aplicatie.tabele.ParametruUnique;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import dorel.aplicatie.swing.ListaCopii;
import dorel.aplicatie.swing.PanelButoane;
import dorel.aplicatie.tabele.InfoIR;
import dorel.basicopp.datatypes.MyDate;
import dorel.basicopp.datatypes.Numere;
import dorel.basicopp.io.TextWriter;
import dorel.basicopp.server.ServerTools;
import dorel.htmlgenerator.HtmlElementHelper;
import dorel.htmlgenerator.HtmlElementInterface;
import dorel.htmlgenerator.StyleRule;
import dorel.htmlgenerator.StyleSheet;
import dorel.htmlgenerator.div.HtmlDivT2Helper;
import dorel.htmlgenerator.table.HtmlTableHelper;
import dorel.htmlgenerator.table.HtmlTableRowInterface;
import dorel.htmlgenerator.tags.HtmlDiv;
import dorel.htmlgenerator.tags.Page;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TabelaSqlHelper implements TabelaSqlInterface, HtmlTableRowInterface {

    protected int id;
    //
    protected MyPanel myPanel;
    protected ListaCopii myPanelEast;
    public String numeTabela;
    public String windowTitle;
    public String orderBy;
    public String textSql;  // String echivalent cu textView, dat format cu numele colanelor tabelei (este posibil ca parametrii sa nu aiba acelasi nume cu coloanele)
    public String textView;  // String format cu parametrii din clasa (Ex: numar_doc+" "+dfm.format(data))
    //
    public Coloane coloane;
    public List<InfoIR> listaInfoIR;
    DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat dfmRo = new SimpleDateFormat("dd.MM.yyyy");
    CommonInterface common;
    private String numeControlFocus = "denumire";

    private String numeTabelaFiltruReferinta = "";
    private String numeColoanaFiltruReferinta = "";
    private int widthFiltruReferinta = 20;
    private final Dimension dimListaW;

    private final String numeColoanaCopiiId;  // pentru tabelele care au copii IN LOCUL filtrului se pune parinte_id=filtru (filtru este un ID !!!)
    private int nrMaxLiniiPanelCenter = 0;

    private List<ActiuneButonComanda> listaActiuni;

    //<editor-fold defaultstate="collapsed" desc="Set Get">
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Coloane getColoane() {
        return coloane;
    }

    @Override
    public String getWindowTitle() {
        return windowTitle;
    }

    @Override
    public MyPanel getMyPanel() {
        return myPanel;
    }

    @Override
    public String getNumeTabela() {
        return numeTabela;
    }

    @Override
    public String getOrderBy() {
        return orderBy;
    }
    //</editor-fold>

    public TabelaSqlHelper(String numeTabela, String windowTitle, String orderBy, String textSql, String textView, Coloane coloane, CommonInterface common, List<InfoIR> listaInfoIR, String numeControlFocus, String numeColoanaCopiiId, String numeTabelaFiltruReferinta, String numeColoanaFiltruReferinta, int widthFiltruReferinta, Dimension dimListaW, int nrMaxLiniiPanelCenter, List<ActiuneButonComanda> listaActiuni) {
        this.numeTabela = numeTabela;
        this.windowTitle = windowTitle;
        this.orderBy = orderBy;
        this.textSql = textSql;
        this.textView = textView;
        this.coloane = coloane;
        this.common = common;
        if (listaInfoIR == null) {
            listaInfoIR = new ArrayList<>();
        }
        this.listaInfoIR = listaInfoIR;
        this.numeControlFocus = numeControlFocus;
        this.numeColoanaCopiiId = numeColoanaCopiiId;

        this.numeTabelaFiltruReferinta = numeTabelaFiltruReferinta;
        this.numeColoanaFiltruReferinta = numeColoanaFiltruReferinta;
        this.widthFiltruReferinta = widthFiltruReferinta;
        this.dimListaW = dimListaW;
        this.nrMaxLiniiPanelCenter = nrMaxLiniiPanelCenter;
        this.listaActiuni = listaActiuni;
        id = 0;
    }

    //<editor-fold defaultstate="collapsed" desc="set Valori">
    @Override
    public void puneValoriImplicite() {
        id = 0;  // id se pune 0 pentru a salva corect
        //restul valorilor au fost puse pe valorile implicite la crearea coloanelor
        for (ColoanaTabela coloana : coloane.listaColoane) {
            coloana.valoare = coloana.valoareImplicita;
        }
    }

    @Override
    public void loadFromRs(ResultSet rs) {
        try {
            setId(rs.getInt("id"));
            for (ColoanaTabela coloana : coloane.listaColoane) {
                switch (coloana.tipDataJava) {
                    case STRING:
                        coloana.valoare = rs.getString(coloana.numeSql);
                        break;
                    case BOOLEAN:
                        coloana.valoare = String.valueOf(rs.getBoolean(coloana.numeSql));
                        break;
                    case DATE:
                        Date xdata = rs.getDate(coloana.numeSql);
                        //JOptionPane.showMessageDialog(null, "data:"+xdata);
                        if (xdata == null) {
                            coloana.valoare = "";
                        } else {
                            coloana.valoare = dfmRo.format(xdata);
                        }
                        break;
                    case DOUBLE:
                        coloana.valoare = String.valueOf(rs.getDouble(coloana.numeSql));
                        break;
                    case INTEGER:
                        coloana.valoare = String.valueOf(rs.getInt(coloana.numeSql));
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "TabelaSqlHelper.loadFromRs tipDataJava necunoscut");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "TabelaSqlHelper.loadFromRs - Exception:" + ex.getLocalizedMessage());
        }
    }

    @Override
    public void setValuesFromTabela(TabelaSqlInterface tabela) {
        if (tabela == null) {
            this.setId(0);
            for (ColoanaTabela coloana : coloane.listaColoane) {
                coloana.valoare = "";
            }
        } else {
            this.setId(tabela.getId());
            for (ColoanaTabela coloana : coloane.listaColoane) {
                coloana.valoare = tabela.getColoane().getValoare(coloana.numeJava);
            }
        }
    }

    @Override
    public void puneValoriDinMap(Map<String, String> mapValori) {
        for (ColoanaTabela coloana : coloane.listaColoane) {
            coloana.valoare = mapValori.get(coloana.numeJava);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SQL">
    @Override
    public String getComandaCreate() {
        String cmd = "CREATE TABLE " + this.getNumeTabela() + " (id int AUTO_INCREMENT PRIMARY KEY";
        cmd += coloane.getListaColoaneCreateSQL();
        cmd += ", viz char(1)";
        cmd += ");";
        return cmd;
    }

    @Override
    public String getComandaSave() {
        if (id > 0) {
            return getComandaUpdate();
        } else {
            return getComandaInsert();
        }
    }

    protected String getComandaInsert() {
        String comanda = "INSERT INTO " + this.getNumeTabela() + " (" + coloane.getListaColoaneInsertSQL() + ", viz)";
        comanda += " VALUES ";
        comanda += "(" + coloane.getListaValoriInsertSQL() + ", 'T');";
        return comanda;
    }

    protected String getComandaUpdate() {
        String comanda = "UPDATE " + this.getNumeTabela();
        comanda += " SET ";
        comanda += coloane.getListaPerechiUpdateSQL();
        comanda += " WHERE ";
        comanda += "id=" + this.getId();
        return comanda;
    }

    @Override
    public String getComandaDelete() {
        String comanda = "DELETE FROM " + numeTabela;
        comanda += " WHERE ";
        comanda += "id=" + id;
        return comanda;
    }

    @Override
    public List<ParametruUnique> getParametriUnique() {
        List<ParametruUnique> lista = new ArrayList<>();
        ParametruUnique param;
        for (ColoanaTabela coloana : coloane.listaColoane) {
            if (coloana.uniqueValues) {
                param = new ParametruUnique(this.getNumeTabela(), coloana.numeSql + "=" + ServerTools.sqlString(coloana.valoare), this.getId(), "Valoarea '" + coloana.valoare + "' din coloana " + coloana.numeSql + " mai există.");
                lista.add(param);
            }
        }
        return lista;
    }

    @Override
    public List<ParametruIR> getListaParametriIR() {
        // asigurare integritate referentiala
        List<ParametruIR> listaParametriIR = new ArrayList<>();
        ParametruIR param;
        for (InfoIR infoIR : listaInfoIR) {
            param = new ParametruIR(infoIR.numeTabela, infoIR.numeColoana + "=" + id, "Aveți înregistrări în tabela " + infoIR.numeTabela + " care fac referință la această înregistrare.");
            listaParametriIR.add(param);
        }
        return listaParametriIR;
    }

    @Override
    public String getActualFilter(String filter, int idFilterReferinta, boolean eCopil) {
        String filtru = "";
        if (eCopil) {
            filtru = numeColoanaCopiiId + "=" + filter;
        } else {
            boolean ePrima = true;
            if (!filter.isEmpty()) {
                filtru = textSql + " LIKE '%" + filter + "%'";
                ePrima = false;
            }
            if (idFilterReferinta != 0) {
                if (ePrima) {
                    ePrima = false;
                } else {
                    filtru += " AND";
                }
                filtru += " " + numeColoanaFiltruReferinta + "=" + idFilterReferinta;
            }
        }
        //System.out.println(filtru);
        return filtru;
    }

    @Override
    public String getNumeTabelaFiltruReferinta() {
        return numeTabelaFiltruReferinta;
    }

    @Override
    public int getWidthFiltruReferunta() {
        return widthFiltruReferinta;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="swing">
    @Override
    public Dimension getDimListaW() {
        return dimListaW;
    }

    @Override
    public String getNumeControlFocus() {
        return numeControlFocus;
    }

    private PatraticaControl getLabel(ColoanaTabela coloana) {
        PatraticaControl patratica = new PatraticaControl();
        patratica.setTip(PatraticaControl.TipControl.LABEL);
        patratica.setName("");
        patratica.setLabel(coloana.labelControl);
        return patratica;
    }

    @Override
    public JPanel getPanel() {
        myPanel = new MyPanel();
        LinieControale linie;
        PatraticaControl patratica;
        int nrSubCol = 2;  // numarul subcoloanelor unei coloane cu controale (label+control+check_modific_parola=3)
        //
        for (ColoanaTabela coloana : coloane.listaColoane) {
            linie = new LinieControale();
            //
            switch (coloana.tipControl) {
                case TEXT:
                    linie.addPatratica(getLabel(coloana));
                    //
                    patratica = new PatraticaControl();
                    patratica.setTip(coloana.tipControl);
                    patratica.setName(coloana.numeControl);
                    patratica.setDimX(coloana.dimX);
                    linie.addPatratica(patratica);
                    break;
                case PASSWORD:
                    linie.addPatratica(getLabel(coloana));
                    //
                    patratica = new PatraticaControl();
                    patratica.setTip(coloana.tipControl);
                    patratica.setName(coloana.numeControl);
                    patratica.setDimX(coloana.dimX);
                    linie.addPatratica(patratica);
                    nrSubCol = 3;
                    break;
                case REFERINTA:
                    linie.addPatratica(getLabel(coloana));
                    //
                    patratica = new PatraticaControl();
                    patratica.setTip(coloana.tipControl);
                    patratica.setName(coloana.numeControl);
                    patratica.setDimX(coloana.dimX);
                    //
                    patratica.setCommon(common);
                    patratica.setNumeTabela(coloana.numeTabelaReferinta);
                    linie.addPatratica(patratica);
                    break;
                case CHECKBOX:
                    patratica = new PatraticaControl();
                    patratica.setTip(coloana.tipControl);
                    patratica.setName(coloana.numeControl);
                    patratica.setDimX(coloana.dimX);
                    patratica.setLabel(coloana.labelControl);
                    linie.addPatratica(patratica);
                    break;
                case COMBOBOX:
                    linie.addPatratica(getLabel(coloana));
                    //
                    patratica = new PatraticaControl();
                    patratica.setTip(coloana.tipControl);
                    patratica.setName(coloana.numeControl);
                    patratica.setDimX(coloana.dimX);
                    patratica.setComboBoxModel(coloana.comboBoxModel);
                    linie.addPatratica(patratica);
                    //
                    break;
                case LABEL:
                    linie.addPatratica(getLabel(coloana));
                    //
                    break;
            }
            myPanel.addLinie(linie);
        }
        return myPanel.getPanel(nrMaxLiniiPanelCenter, nrSubCol);  // nrSubCol= 3- pt users, celelalte ar trebui 2
    }

    @Override
    public void setPanelEast(ListaCopii panelEast) {
        myPanelEast = panelEast;
    }

    @Override
    public ListaCopii getPanelEast() {
        //JOptionPane.showMessageDialog(null, "TabelaSqlHelper.getPanel - neimplementata");
//        if (myPanelEast == null) {
//            myPanelEast = new ListaCopii(common, this);
//        }
        return myPanelEast;
    }

    @Override
    public void puneValoriInControale() {
        for (ColoanaTabela coloana : coloane.listaColoane) {
            if (coloana.tipDataJava == ColoanaTabela.TipDataJava.DATE) {
                if (coloana.valoare.isEmpty()) {
                    myPanel.puneValoareInControl(coloana.numeControl, "");
                } else {
                    myPanel.puneValoareInControl(coloana.numeControl, coloana.valoare);
                }
            } else {
                myPanel.puneValoareInControl(coloana.numeControl, coloana.valoare);
            }
        }
        if (myPanelEast != null) {
            myPanelEast.populeazaLista(id);
        }
    }

    @Override
    public void iaValoriDinControale() {
        //JOptionPane.showMessageDialog(null, "TabelaSqlHelper.iaValoriDinControale - neimplementata");
        for (ColoanaTabela coloana : coloane.listaColoane) {
            switch (coloana.tipDataJava) {
                case DATE:
                    String valData = myPanel.getValoareDinControl(coloana.numeControl);
                    if (valData.equals("")) {
                        coloana.valoare = "";
                    } else {
                        try {
                            Date xdata = dfmRo.parse(valData);
                            coloana.valoare = valData;
                        } catch (ParseException e) {
                            coloana.valoare = "";
                        }
                    }
                    break;
                default:
                    coloana.valoare = myPanel.getValoareDinControl(coloana.numeControl);
                    if (coloana.tipControl == PatraticaControl.TipControl.PASSWORD) {
                        if (coloana.valoare.isEmpty()) {
                            coloana.modificParola = false;
                        } else {
                            coloana.modificParola = true;
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public boolean testCorect() {
        String mesajEroare = "";
        for (ColoanaTabela coloana : coloane.listaColoane) {
            switch (coloana.tipTestCorect) {
                case EMPTY_STRING:
                    if (coloana.tipControl == PatraticaControl.TipControl.PASSWORD && !coloana.modificParola) {
                        // NU modific parola
                    } else {
                        if (coloana.valoare.isEmpty()) {
                            mesajEroare += coloana.mesajInCorect + "\n";
                        }
                    }
                    break;
                case EQUAL_ZERO:
                    if (coloana.valoare.equals("0")) {
                        mesajEroare += coloana.mesajInCorect + "\n";
                    }
                    break;
                case VALID_DATE:
                    if (coloana.valoare.isEmpty()) {
                        mesajEroare += "Nu ati introdus " + coloana.labelControl + "." + "\n";
                    } else {
                        String sData = MyDate.verif_DMYData(coloana.valoare, ".");
                        if (sData.isEmpty()) {
                            mesajEroare += coloana.labelControl + " nu este valida." + "\n";
                        }
                    }
                    break;
                case VALID_DATE_OR_NULL:
                    if (!coloana.valoare.isEmpty()) {
                        String sData = MyDate.verif_DMYData(coloana.valoare, ".");
                        if (sData.isEmpty()) {
                            mesajEroare += coloana.labelControl + " nu este valida." + "\n";
                        }
                    }
                    break;
                case VALID_INTEGER:
                    if (!Numere.isInteger(coloana.valoare)) {
                        mesajEroare += "Valoarea " + coloana.labelControl + " nu este un numar intreg." + "\n";
                    }
                    break;
                case VALID_DOUBLE:
                    if (!Numere.isNumeric(coloana.valoare)) {
                        mesajEroare += "Valoarea " + coloana.labelControl + " nu este numerică." + "\n";
                    }
                    break;
                case NU_TESTEZ_CORECT:
                    break;
                default:
                    mesajEroare += "Tip test necunoscut:" + coloana.tipTestCorect.toString() + "\n";
                    break;
            }
        }
        if (mesajEroare.isEmpty()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Erori la introducerea datelor:\n\n" + mesajEroare + "\nCorectaţi erorile si salvaţi.", "Mesaj eroare", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public String toString() {
        return coloane.getValoare(this.textView);
    }

    @Override
    public int getNrMaxLiniiPanelCenter() {
        return nrMaxLiniiPanelCenter;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="List html">
    @Override
    public boolean genLista(String numeFis) {
        // head
        HtmlElementInterface head = new HtmlElementHelper("head");
        StyleSheet style = new StyleSheet();
        HtmlTableHelper.addCss(style);
        HtmlDivT2Helper.addCss(style);
        StyleRule sc;
        sc = new StyleRule(".titleFT");
        sc.addProp("padding-top", "20px");
        sc.addProp("padding-bottom", "10px");
        sc.addProp("font-family", "\"Times New Roman\", Times, serif");
        sc.addProp("font-size", "20px");
        sc.addProp("font-weight", "bold");
        sc.addProp("left", "100px");
        sc.addProp("width", "640px");
        sc.addProp("border-bottom", "2px solid darkgray");
        style.addStyleRule(sc);
        //style.addStyleRule(Antet.getStyleRule());
        head.addHtmlElement(style);
        // body
        HtmlElementInterface body = new HtmlElementHelper("body");

        body.addHtmlElement(new HtmlDiv("titleFT", "LISTA " + numeTabela));
        body.addHtmlElement(getTabel(orderBy, ""));
        //
        HtmlElementInterface page = new Page(head, body);
        TextWriter tw = new TextWriter(numeFis, false);
        tw.writeUTF8_ID();
        tw.write(page.getText());
        tw.close();
        return true;
    }

    private HtmlElementInterface getTabel(String orderBy, String filtru) {
        // pentru raportul tabel pentru mai multe inregistrari
        List<TabelaSqlInterface> rows;
        HtmlTableHelper table = new HtmlTableHelper();

        table.setColumns(coloane.getSHtmlColumns());
        rows = common.getDataSource().listInreg(numeTabela, 0, orderBy, filtru);
        for (TabelaSqlInterface tri : rows) {
            table.addRow((HtmlTableRowInterface) tri);
        }
        return table.getElement();
    }
//

    @Override
    public String getValueByName(String colName) {
        return coloane.getValoare(colName);
    }
    //</editor-fold>

    @Override
    public PanelButoane getPanelNorth(FerDialogTabela ferDialogTabela) {
        PanelButoane panelN = new PanelButoane(listaActiuni, ferDialogTabela);
        return panelN;
    }

}
