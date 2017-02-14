package dorel.aplicatie.tabele.clase;

import dorel.aplicatie.swing.MyListModel;
import dorel.aplicatie.swing.PatraticaControl;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.ComboBoxModel;

public class ColoanaTabela {

    public enum TipDataJava {

        STRING,
        INTEGER,
        DOUBLE,
        DATE,
        BOOLEAN,
    }

    public enum TipDataSql {

        CHAR,
        VARCHAR,
        INTEGER,
        DECIMAL,
        DATE,
    }

    // TabelaSqlHelper.testCorect()
    public enum TipTestCorect {

        EMPTY_STRING,
        EQUAL_ZERO,
        VALID_DATE,
        VALID_DATE_OR_NULL,
        VALID_INTEGER,
        VALID_DOUBLE,
        NU_TESTEZ_CORECT;
    }
    DateFormat dfm;
    DateFormat dfmRo;

    public String numeJava;
    public TipDataJava tipDataJava;
    //
    public String numeSql;
    public TipDataSql tipDataSql;
    public int lungimeSql;
    public int precizieSql;
    //
    public PatraticaControl.TipControl tipControl;
    public String valoareImplicita;
    public String valoare;
    //
    public String numeControl;
    public String labelControl;
    //
    // pt text
    public int dimX;
    // pt referinta
    public String numeTabelaReferinta;
    // pt JComboBox
    public ComboBoxModel comboBoxModel;
    // pt JList
    public MyListModel listModel;
    // pt password
    public boolean modificParola;
    //
    public TipTestCorect tipTestCorect = TipTestCorect.NU_TESTEZ_CORECT;
    public String mesajInCorect = "";
    //
    public boolean uniqueValues = false;
    //
    // pt listare HTML
    public boolean skipLaListare = false;

    //
    public void setNume(String nume) {
        numeJava = nume;
        numeSql = nume;
        numeControl = nume;
    }

    public void setValoareImplicita(String valoareImplicita) {
        this.valoareImplicita = valoareImplicita;
        valoare = valoareImplicita;
    }

    public String getValoareImplicita() {
        return valoareImplicita;
    }

    public ColoanaTabela(String numeJava, TipDataJava tipDataJava, String numeSql, TipDataSql tipDataSql, int lungimeSql, int precizieSql, String valoareImplicita, String numeControl, PatraticaControl.TipControl tipControl, int dimX, String labelControl, String numeTabelaReferinta, ComboBoxModel comboBoxModel, MyListModel listModel, TipTestCorect tipTestCorect, String mesajInCorect) {
        //
        this.numeJava = numeJava;
        this.tipDataJava = tipDataJava;
        //
        this.numeSql = numeSql;
        this.tipDataSql = tipDataSql;
        this.lungimeSql = lungimeSql;
        this.precizieSql = precizieSql;
        //
        this.valoareImplicita = valoareImplicita;
        valoare = valoareImplicita;
        //
        this.numeControl = numeControl;
        this.tipControl = tipControl;
        this.dimX = dimX;
        this.labelControl = labelControl;
        this.numeTabelaReferinta = numeTabelaReferinta;
        this.comboBoxModel = comboBoxModel;
        this.listModel = listModel;
        //
        this.tipTestCorect = tipTestCorect;
        this.mesajInCorect = mesajInCorect;

        dfm = new SimpleDateFormat("yyyy-MM-dd");
        dfmRo = new SimpleDateFormat("dd.MM.yyyy");
        dfm.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
        dfmRo.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
    }

    public ColoanaTabela() {
        dfm = new SimpleDateFormat("yyyy-MM-dd");
        dfmRo = new SimpleDateFormat("dd.MM.yyyy");
        dfm.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
        dfmRo.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
        numeJava = "";
        tipDataJava = TipDataJava.STRING;
        //
        numeSql = "";
        tipDataSql = TipDataSql.CHAR;
        lungimeSql = 255;
        precizieSql = 0;
        //
        tipControl = PatraticaControl.TipControl.TEXT;
        valoareImplicita = "";
        valoare = "";
        //
        numeControl = "";
        labelControl = "";
        //
        // pt text
        dimX = 20;
        // pt referinta
        numeTabelaReferinta = "";
        // pt JComboBox
        comboBoxModel = null;
        // pt JList
        listModel = null;
        //
        tipTestCorect = TipTestCorect.NU_TESTEZ_CORECT;
        mesajInCorect = "";
    }

    public String getValoareSQL() {
        switch (tipDataSql) {
            case CHAR:
            case VARCHAR:
                if (valoare == null) {
                    return "''";
                } else {
                    CharSequence cs1 = "'";
                    CharSequence cs2 = "''";
                    return "'" + valoare.replace(cs1, cs2) + "'";
                }
            case DATE: {
                try {
                    if (valoare == null) {
                        valoare = "0.0.0";
                    }
                    Date xdata = dfmRo.parse(valoare);
                    return "'" + dfm.format(xdata) + "'";
                } catch (ParseException ex) {
                    return "'0000-00-00'";
                }
            }
            default:
                return valoare;
        }
    }

    public String getStringCreateSQL() {
        String raspuns = numeSql;
        switch (tipDataSql) {
            case CHAR:
                raspuns += " char(" + lungimeSql + ")";
                break;
            case VARCHAR:
                raspuns += " varchar(" + lungimeSql + ")";
                break;
            case DATE:
                raspuns += " date";
                break;
            case DECIMAL:
                raspuns += " decimal(" + lungimeSql + ", " + precizieSql + ")";
                break;
            case INTEGER:
                raspuns += " int";
                break;
        }
        return raspuns;
    }
}
