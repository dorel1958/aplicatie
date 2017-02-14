package dorel.aplicatie.back;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.basicopp.datatypes.Numere;
import dorel.basicopp.excelreport.FisExcel;
import dorel.basicopp.excelreport.RandString;
import dorel.basicopp.server.ServerTools;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class BackRaport {

    CommonInterface common;
    String numeBD_Tabela;

    public BackRaport(CommonInterface common) {
        this.common = common;
        numeBD_Tabela = common.getConfigLocal().getValue("Database") + "_back.comenzi";
    }

    public void executa(String tabela, String id, String user_id) {
        String comanda;
        ResultSet rs;
        String filtruWhere = "";

        id = id.trim();
        if (!id.isEmpty()) {
            if (!Numere.isInteger(id)) {
                JOptionPane.showMessageDialog(null, "ID nu este un numar întreg.");
                id = "";
            }
        }

        user_id = user_id.trim();
        if (!user_id.isEmpty()) {
            if (!Numere.isInteger(user_id)) {
                JOptionPane.showMessageDialog(null, "User ID nu este un numar întreg.");
                user_id = "";
            }
        }

        boolean ePrima = true;
        if (!tabela.isEmpty()) {
            if (ePrima) {
                ePrima = false;
            } else {
                filtruWhere += " AND";
            }
            filtruWhere += " tabela=" + ServerTools.sqlString(tabela);
        }
        if (!id.isEmpty()) {
            if (ePrima) {
                ePrima = false;
            } else {
                filtruWhere += " AND";
            }
            filtruWhere += " (inserted_id=" + id + " OR updated_id=" + id + " OR deleted_id=" + id + ")";
        }
        if (!user_id.isEmpty()) {
            if (ePrima) {
                ePrima = false;
            } else {
                filtruWhere += " AND";
            }
            filtruWhere += " user_id=" + user_id;
        }

        comanda = "SELECT * FROM " + numeBD_Tabela;
        if (!filtruWhere.isEmpty()) {
            comanda += " WHERE";
            comanda += filtruWhere;
        }
        comanda += ";";
        if (common.getDataSource().executaComandaRs(comanda)) {
            rs = common.getDataSource().getResultSet();
            try {
                String numeFisExcel = "D:\\Lucru\\Java\\CRM\\rap_back.xls";
                FisExcel fe = new FisExcel(numeFisExcel, FisExcel.TipFisExcel.xls);
                fe.genExcel("Raport");
                fe.startNew();
                fe.setCurrentSheet("Raport", false);
                RandString rand;
                //
                // titlu coloane
                rand = new RandString();
                rand.addColoana("Data");
                rand.addColoana("User");
                rand.addColoana("Tabela");
                rand.addColoana("Inserted ID");
                rand.addColoana("Updated ID");
                rand.addColoana("Deleted ID");
                rand.addColoana("Comanda");

                fe.addRow(2, 0, rand.getRandul(), true);
                //
                // continut
                while (rs.next()) {
                    rand = new RandString();
                    rand.addColoana(rs.getString("data"));
                    rand.addColoana(rs.getString("user_id"));
                    rand.addColoana(rs.getString("tabela"));
                    rand.addColoana(rs.getString("inserted_id"));
                    rand.addColoana(rs.getString("updated_id"));
                    rand.addColoana(rs.getString("deleted_id"));
                    rand.addColoana(rs.getString("comanda"));
                    fe.addRow(0, 0, rand.getRandul(), false);
                }
                fe.autoSizeColumns(0, 7);
                fe.writeToFile();
                //
                fe.viewFisExcel();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "BackRaport.executa 2 - SQLException:" + ex.getLocalizedMessage());
            }
        }

    }
}
