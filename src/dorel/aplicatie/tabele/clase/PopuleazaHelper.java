package dorel.aplicatie.tabele.clase;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.PopuleazaInterface;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import java.util.HashMap;
import java.util.Map;

public class PopuleazaHelper implements PopuleazaInterface{
    CommonInterface common;

    public PopuleazaHelper(CommonInterface common) {
        this.common=common;
    }

    @Override
    public void populeazaTabela(String numeTabela) {
        switch(numeTabela){
            case "":
                break;
            // ...
            default:
                break;
        }
    }

/////////////////   Exemplu  //////////////////
    //<editor-fold defaultstate="collapsed" desc="judete">
//    private void populeazaJudete() {
//        saveUnJudet("01", "Alba", "AB");
//        saveUnJudet("02", "Arad", "AR");
//        saveUnJudet("03", "Argeș", "AG");
//        saveUnJudet("04", "Bacău", "BC");
//        saveUnJudet("05", "Bihor", "BH");
//        saveUnJudet("06", "Bistrița-Năsăud", "BN");
//        saveUnJudet("07", "Botoșani", "BT");
//        saveUnJudet("08", "Brașov", "BV");
//        saveUnJudet("09", "Brăila", "BR");
//        saveUnJudet("10", "Buzău", "BZ");
//        saveUnJudet("11", "Caraș-Severin", "CS");
//        saveUnJudet("12", "Călărași", "CL");
//        saveUnJudet("13", "Cluj", "CJ");
//        saveUnJudet("14", "Constanța", "CT");
//        saveUnJudet("15", "Covasna", "CV");
//        saveUnJudet("16", "Dâmbovița", "DB");
//        saveUnJudet("17", "Dolj", "DJ");
//        saveUnJudet("18", "Galați", "GL");
//        saveUnJudet("19", "Giurgiu", "GR");
//        saveUnJudet("20", "Gorj", "Gj");
//        saveUnJudet("21", "Harghita", "HG");
//        saveUnJudet("22", "Hunedoara", "HR");
//        saveUnJudet("23", "Ialomița", "IL");
//        saveUnJudet("24", "Iași", "IS");
//        saveUnJudet("25", "Ilfov", "IF");
//        saveUnJudet("26", "Maramureș", "MM");
//        saveUnJudet("27", "Mehedinți", "MH");
//        saveUnJudet("28", "Mureș", "MS");
//        saveUnJudet("29", "Neamț", "NT");
//        saveUnJudet("30", "Olt", "OT");
//        saveUnJudet("31", "Prahova", "PH");
//        saveUnJudet("32", "Satu-Mare", "SM");
//        saveUnJudet("33", "Sălaj", "SJ");
//        saveUnJudet("34", "Sibiu", "SB");
//        saveUnJudet("35", "Suceava", "SV");
//        saveUnJudet("36", "Teleorman", "TN");
//        saveUnJudet("37", "Timiș", "TM");
//        saveUnJudet("38", "Tulcea", "TL");
//        saveUnJudet("39", "Vaslui", "VS");
//        saveUnJudet("40", "Vâlcea", "VL");
//        saveUnJudet("41", "Vrancea", "VN");
//        saveUnJudet("42", "Mun. București", "B");
//    }
//
//    private void saveUnJudet(String cod, String denumire, String prescurtare) {
//        TabelaSqlInterface judet = common.getTabela("judete");
//        Map<String, String> mapValoriJudete = new HashMap<>();
//        mapValoriJudete.put("denumire", denumire);
//        mapValoriJudete.put("cod", cod);
//        mapValoriJudete.put("prescurtare", prescurtare);
//        judet.puneValoriDinMap(mapValoriJudete);
//        judet.setId(0);
//        common.getDataSource().saveInreg(judet);
//    }
    //</editor-fold>

//    @Override
//    public void populeazaTabela(String numeTabela) {
//        switch(numeTabela){
//            case "judete":
//                populeazaJudete();
//                break;
//            default:
//                
//        }
//    }
    
}
