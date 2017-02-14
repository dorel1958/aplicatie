package dorel.aplicatie.tabele.clase;

import dorel.aplicatie.actiuni.ActiuneButonComanda;
import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.TabelaSqlFactoryInterface;
import dorel.aplicatie.swing.PatraticaControl;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import dorel.basicopp.sumecontrol.CalculMD5;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class TabelaSqlFactoryHelper implements TabelaSqlFactoryInterface {

    @Override
    public TabelaSqlInterface getTabelaUser(CommonInterface common, boolean eAdmin) {
        TabelaSqlInterface tabela;
        Coloane coloane = new Coloane();
        ColoanaTabela coloana;
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("user");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 255;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 30;
        coloana.labelControl = "Utilizator";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.EMPTY_STRING;
        coloana.mesajInCorect = "Nu ati introdus userul.";
        if (eAdmin) {
            coloana.valoare = "admin";
        }
        coloana.uniqueValues = true;
        coloane.addColoana(coloana);
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("password");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 255;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.PASSWORD;
        coloana.dimX = 30;
        coloana.labelControl = "Parola";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.EMPTY_STRING;
        coloana.mesajInCorect = "Nu ati introdus Parola.";
        if (eAdmin) {
            coloana.valoare = CalculMD5.getStringMD5("admin");
            coloana.modificParola = true;
        }
        coloana.skipLaListare = true;
        coloane.addColoana(coloana);
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("nume");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 255;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 30;
        coloana.labelControl = "Nume";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.EMPTY_STRING;
        coloana.mesajInCorect = "Nu ati introdus numele.";
        if (eAdmin) {
            coloana.valoare = "Administrator";
        }
        coloane.addColoana(coloana);
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("drepturi");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 255;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 30;
        coloana.labelControl = "Drepturi";
        //
        //coloana.tipTestCorect = ColoanaTabela.TipTestCorect.EMPTY_STRING;
        //coloana.mesajInCorect = "Nu ati introdus drepturile.";
        if (eAdmin) {
            coloana.valoare = "A";
        }
        coloane.addColoana(coloana);
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("anc");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.INTEGER;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.INTEGER;
        //
        coloana.setValoareImplicita("0");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 5;
        coloana.labelControl = "An curent";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.NU_TESTEZ_CORECT;
        coloane.addColoana(coloana);
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("lunac");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.INTEGER;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.INTEGER;
        //
        coloana.setValoareImplicita("0");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 5;
        coloana.labelControl = "Luna curentă";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.NU_TESTEZ_CORECT;
        coloane.addColoana(coloana);
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("ziuac");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.INTEGER;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.INTEGER;
        //
        coloana.setValoareImplicita("0");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 5;
        coloana.labelControl = "Ziua curentă";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.NU_TESTEZ_CORECT;
        coloane.addColoana(coloana);
        // ------------------
        coloana = new ColoanaTabela();
        coloana.setNume("datac_fixa");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 1;
        //coloana.precizieSql = 0;
        //
        coloana.setValoareImplicita("F");
        //
        coloana.tipControl = PatraticaControl.TipControl.CHECKBOX;
        //coloana.dimX =5;
        coloana.labelControl = "Data curentă rămâne fixă";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.NU_TESTEZ_CORECT;
        //coloana.mesajInCorect = "Nu ați introdus data curenta.";
        coloana.uniqueValues = false;
        coloane.addColoana(coloana);
        // -------------------
        //List<InfoIR> listaInfoIRu = new ArrayList<>();
        //InfoIR infoIRu;
        //infoIRu = new InfoIR("users", "firma_id=", "Aveți grupuri de mașini administrate de acest utilizator. Mai întâi ștergeți grupurile de mașini de la utilizator.");
        //listaInfoIRu.add(infoIRu);
        List<ActiuneButonComanda> listaActiuniUsers = new ArrayList<>();
        tabela = new TabelaSqlHelper("users", "Utilizatori", "user", "user", "user", coloane, common, null, "user", "", "", "", 20, new Dimension(150, 300), 0, listaActiuniUsers);
        return tabela;
    }

    @Override
    public TabelaSqlInterface getTabelaSetari(CommonInterface common) {
        TabelaSqlInterface tabela;
        Coloane coloane = new Coloane();
        ColoanaTabela coloana;
        // -------------------
        coloana = new ColoanaTabela();
        coloana.setNume("denumire");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 20;
        coloana.precizieSql = 0;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 20;
        coloana.labelControl = "Denumire";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.EMPTY_STRING;
        coloana.mesajInCorect = "Nu ați introdus denumirea.";
        coloana.uniqueValues = true;
        coloane.addColoana(coloana);
        // ------------------
        coloana = new ColoanaTabela();
        coloana.setNume("valoare");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 255;
        coloana.precizieSql = 0;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 30;
        coloana.labelControl = "Valoare";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.EMPTY_STRING;
        coloana.mesajInCorect = "Nu ați introdus valoarea.";
        //coloana.uniqueValues = true;
        coloane.addColoana(coloana);
        // ------------------
        coloana = new ColoanaTabela();
        coloana.setNume("explicatii");
        coloana.tipDataJava = ColoanaTabela.TipDataJava.STRING;
        //
        coloana.tipDataSql = ColoanaTabela.TipDataSql.CHAR;
        coloana.lungimeSql = 255;
        coloana.precizieSql = 0;
        //
        coloana.setValoareImplicita("");
        //
        coloana.tipControl = PatraticaControl.TipControl.TEXT;
        coloana.dimX = 30;
        coloana.labelControl = "Explicatii";
        //
        coloana.tipTestCorect = ColoanaTabela.TipTestCorect.NU_TESTEZ_CORECT;
        coloane.addColoana(coloana);
        // ------------------
        List<ActiuneButonComanda> listaActiuniSetari = new ArrayList<>();
        tabela = new TabelaSqlHelper("setari", "Setari", "denumire", "denumire", "denumire", coloane, common, null, "denumire", "", "", "", 20, new Dimension(150, 300), 0, listaActiuniSetari);
        return tabela;
    }

    @Override
    public TabelaSqlInterface getTabela(String numeTabela, CommonInterface common) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
