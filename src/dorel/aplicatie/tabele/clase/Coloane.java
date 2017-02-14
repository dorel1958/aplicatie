package dorel.aplicatie.tabele.clase;

import dorel.aplicatie.swing.PatraticaControl;
import dorel.htmlgenerator.table.HtmlTableColumn;
import java.util.ArrayList;
import java.util.List;

public class Coloane {

    public List<ColoanaTabela> listaColoane;

    public Coloane() {
        listaColoane = new ArrayList<>();
    }

    public void addColoana(ColoanaTabela coloana) {
        listaColoane.add(coloana);
    }

    public void setValoare(String numeJava, String valoare) {
        listaColoane.stream().filter((coloana) -> (coloana.numeJava.equals(numeJava))).forEach((coloana) -> {
            coloana.valoare = valoare;
        });
//        for (ColoanaTabela coloana:listaColoane){
//            if (coloana.numeJava.equals(numeJava)){
//                coloana.valoare=valoare;
//            }
//        }
    }

//    public String getValoare(String numeJava) {
//        for (ColoanaTabela coloana : listaColoane) {
//            if (coloana.numeJava.equals(numeJava)) {
//                return coloana.valoare;
//            }
//        }
//        return "";
//    }

    public String getValoare(String listaTextView) {
        String raspuns = "";
        boolean ePrima=true;
        String[] listaC = listaTextView.split(",");
        for (int i = 0; i < listaC.length; i++) {
            for (ColoanaTabela coloana : listaColoane) {
                if (coloana.numeJava.equals(listaC[i])) {
                    if (ePrima){
                        ePrima=false;
                    } else{
                        raspuns +=" ";
                    }
                    raspuns += coloana.valoare;
                }
            }
        }
        return raspuns;
    }

    // <editor-fold defaultstate="collapsed" desc="SQL">
    public String getListaColoaneCreateSQL() {
        String listCol = "";
        for (ColoanaTabela coloana : listaColoane) {
            listCol += "," + coloana.getStringCreateSQL();
        }
        return listCol;
    }

    public String getListaColoaneInsertSQL() {
        String listCol = "";
        boolean ePrima = true;
        for (ColoanaTabela coloana : listaColoane) {
            if (coloana.tipControl == PatraticaControl.TipControl.PASSWORD && !coloana.modificParola) {
                // nu o pune
            } else {
                if (ePrima) {
                    ePrima = false;
                } else {
                    listCol += ",";
                }
                listCol += coloana.numeSql;
            }
        }
        return listCol;
    }

    public String getListaValoriInsertSQL() {
        String listCol = "";
        boolean ePrima = true;
        for (ColoanaTabela coloana : listaColoane) {
            if (coloana.tipControl == PatraticaControl.TipControl.PASSWORD && !coloana.modificParola) {
                // nu o pune
            } else {
                if (ePrima) {
                    ePrima = false;
                } else {
                    listCol += ",";
                }
                listCol += coloana.getValoareSQL();
            }
        }
        return listCol;
    }

    public String getListaPerechiUpdateSQL() {
        String listCol = "";
        boolean ePrima = true;
        for (ColoanaTabela coloana : listaColoane) {
            if (coloana.tipControl == PatraticaControl.TipControl.PASSWORD && !coloana.modificParola) {
                // nu o pune
            } else {
                if (ePrima) {
                    ePrima = false;
                } else {
                    listCol += ",";
                }
                listCol += coloana.numeSql + "=" + coloana.getValoareSQL();
            }
        }
        return listCol;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Listare HTML">
    public List<HtmlTableColumn> getSHtmlColumns() {
        List<HtmlTableColumn> lista = new ArrayList<>();
        for (ColoanaTabela coloana : listaColoane) {
            if (!coloana.skipLaListare) {
                HtmlTableColumn col;
                col = new HtmlTableColumn(coloana.labelControl, coloana.numeJava, coloana.dimX);
                lista.add(col);

            }
        }
        return lista;
    }
    //</editor-fold>

}
