package dorel.aplicatie;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.DataSourceInterface;
import dorel.aplicatie.interfaces.PopuleazaInterface;
import dorel.aplicatie.tabele.ParametruIR;
import dorel.aplicatie.tabele.ParametruUnique;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import dorel.aplicatie.tabele.clase.UserCurent;
import dorel.basicopp.io.TextWriter;
import dorel.basicopp.server.ServerHelper;
import dorel.basicopp.server.ServerInterface;
import dorel.basicopp.server.ServerTools;
import dorel.basicopp.sumecontrol.CalculMD5;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import javax.swing.JOptionPane;

public class DataSourceHelper implements DataSourceInterface {

    private final ServerInterface server;
    private final boolean eDesktop;
    private boolean isEroare;
    private String mesajEroare;
    private final CommonInterface common;
    //
    private final boolean backToFile = false;
    private final String numeFis;
    //
    private final boolean backToBD = true;
    private String numeBD_Tabela = "";
    //

    @Override
    public ResultSet getResultSet() {
        return server.getResultSet();
    }

    @Override
    public boolean isEroare() {
        return isEroare;
    }

    @Override
    public String getMesajEroare() {
        return mesajEroare;
    }

    public DataSourceHelper(CommonInterface common, PopuleazaInterface populeaza, boolean eDesktop) {
        DateFormat dfm = new SimpleDateFormat("yyyy_MM_dd");
        dfm.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest")); // EET, Eastern Eeuropean Time

        numeFis = dfm.format(new Date()) + "_back.txt";
        numeBD_Tabela = common.getConfigLocal().getValue("Database") + "_back.comenzi";

        isEroare = false;
        this.common = common;
        this.eDesktop = eDesktop;
        server = new ServerHelper(common.getConfigLocal());
        // test conexiune
        if (server.openConnection()) {
            // se conecteaza - e bine
            if (!server.tableExists("users")) {
                if (eDesktop) {
                    boolean doDelete;
                    Object[] options = {"Da, şterge", "Nu"};
                    doDelete = (JOptionPane.showOptionDialog(null, "Tabela 'users' lipseste din BD. Ştergeţi şi recreaţi TOATE tabelele?", "Confirmare", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == JOptionPane.YES_OPTION);
                    if (doDelete) {
                        stergeTabele();
                        genereazaTabele(populeaza);
                    }
                }
                genereazaTabelaUsers();
                genereazaTabelaSetari();
            }
            testUserAdmin();
            server.closeConnection();
        } else {
            // incearca conectarea pe 'test'
            server.setDatabase("test");
            if (server.openConnection()) {
                //genereaza database
                if (databaseExists(common.getConfigLocal().getValue("Database"))) {
                    eroare("DataSource - BD '" + common.getConfigLocal().getValue("Database") + "' exista, dar nu se poate conecta la ea.");
                } else {
                    if (eDesktop) {
                        int raspuns = JOptionPane.showConfirmDialog(null, "Baza de date nu exista. O creati acum?", "Confirmare", JOptionPane.YES_NO_OPTION);
                        if (raspuns == JOptionPane.YES_OPTION) {
                            genereazaDatabase();
                            testUserAdmin();
                        } else {
                            eroare("Baza de date nu a fost creata.");
                        }
                    } else {
                        genereazaDatabase();  // aici, dupa creare, revine la denumirea corecta a bazei de date
                        testUserAdmin();
                    }
                }
                server.closeConnection();
            } else {
                eroare("DataSource - nu se poate conecta la MySQL server.");
                isEroare = true;
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="save, delete, list">
    @Override
    public boolean saveInreg(TabelaSqlInterface tabela) {
        boolean raspuns = false;
        if (tabela == null) {
            return false;
        }
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            if (testUnic(tabela.getParametriUnique())) {
                int initId = tabela.getId();
                int newId = 0;  // id linie inserata
                String comanda = tabela.getComandaSave();
                if (server.execQuery(comanda)) {
                    if (initId == 0) {
                        newId = server.getLastInsertId();
                        if (newId > 0) {
                            tabela.setId(newId);
                            raspuns = true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Insert cu autonumber id=0");
                            raspuns = false;
                        }
                    } else {
                        raspuns = true;
                    }
                    writeBack(tabela.getNumeTabela(), comanda, newId, initId, 0);
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
        return raspuns;
    }

    @Override
    public boolean deleteInreg(TabelaSqlInterface tabela) {
        boolean connDeschisaAici = false;
        boolean raspuns = false;
        String comanda = "";
        if (tabela == null) {
            return false;
        }
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            if (testIntegritateReferentiala(tabela.getListaParametriIR())) {
                comanda = tabela.getComandaDelete();
                raspuns = server.execQuery(comanda);
                if (raspuns) {
                    writeBack(tabela.getNumeTabela(), comanda, 0, 0, tabela.getId());
                }
            } else {
                comanda = "UPDATE " + tabela.getNumeTabela() + " SET viz='F' WHERE id=" + tabela.getId();
                raspuns = server.execQuery(comanda);
                if (raspuns) {
                    writeBack(tabela.getNumeTabela(), comanda, 0, tabela.getId(), 0);
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
        return raspuns;
    }

    private boolean testUnic(List<ParametruUnique> lPu) {
        if (lPu == null) {
            return true;
        }
        if (lPu.isEmpty()) {
            return true;
        }
        String mesaj = "";
        boolean eroare = false;
        //if (server.openConnection(setLocal.getMySQLhost(), String.valueOf(setLocal.getMySQLport()), setLocal.getMySQLdatabase(), setLocal.getMySQLuserName(), setLocal.getMySQLuserPass())) {
        String comanda;
        for (ParametruUnique pu : lPu) {
            int nrInreg = -1;
            comanda = "SELECT id FROM " + pu.getTableName();
            comanda += " WHERE ";
            comanda += pu.getConditia();
            if (server.execQueryRs(comanda)) {
                ResultSet rs = server.getResultSet();
                nrInreg++;  // ajunge la 0
                try {
                    while (rs.next()) {
                        if (rs.getInt("id") == pu.getId()) {
                            // inreg mea are acest nume
                        } else {
                            // alta inreg cu acest nume
                            nrInreg++;
                        }
                    }
                    rs.close();
                    if (nrInreg > 0) {
                        mesaj += pu.getMesajEroare() + "\n\r";
                        eroare = true;
                    }
                } catch (SQLException ex) {
                    eroare("SQLException: " + ex.getLocalizedMessage());
                }
            } else {
                eroare = true;
            }
        }
        //server.closeConnection();
        //} else {
        //    eroare(server.getMesajEroare());
        //}
        if (eroare) {
            //eroare("DataSource.testUnic\n\r" + mesaj + " nu e îndeplinită condiția de unicitate.");
            eroare("DataSource.testUnicitate\n\n" + mesaj + "\n\nNu este îndeplinită condiția de unicitate.");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<TabelaSqlInterface> listInreg(String numeTabela, int id, String orderBy, String filter) {
        List<TabelaSqlInterface> lista = new ArrayList<>();
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda = "SELECT * FROM " + numeTabela;
            if (id > 0) {
                comanda += " WHERE";
                comanda += " id=" + id;
            } else {
                comanda += " WHERE";
                if (!filter.isEmpty()) {
                    comanda += " " + filter + " AND";
                }
                comanda += " viz='T'";
            }
            if (orderBy != null) {
                if (!orderBy.isEmpty()) {
                    comanda += " ORDER BY ";
                    comanda += orderBy;
                }
            }
            //System.out.println(comanda);
            if (server.execQueryRs(comanda)) {
                ResultSet rs = server.getResultSet();
                try {
                    //TabelaSql elementLista = null;
                    while (rs.next()) {
                        //TabelaSqlInterface elementLista = tabela.getNew();
                        TabelaSqlInterface elementLista = common.getTabelaSqlFactory().getTabela(numeTabela, common);
                        elementLista.loadFromRs(rs);
                        lista.add(elementLista);
                    }
                    rs.close();
                } catch (SQLException ex) {
                    eroare("SQLException: " + ex.getLocalizedMessage());
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
        return lista;
    }

    @Override
    public TabelaSqlInterface getInreg(String numeTabela, int id) {
        List<TabelaSqlInterface> lista = listInreg(numeTabela, id, "", "");
        if (lista.isEmpty()) {
            return null;
        } else {
            return lista.get(0);
        }
    }

    private boolean testIntegritateReferentiala(List<ParametruIR> lParametriIR) {
        // comanda se da cu servarul conectat
        boolean response = true;
        String mesaj = "";
        if (lParametriIR == null) {
            return response;
        }
        if (lParametriIR.isEmpty()) {
            return response;
        }
//        if (server.openConnection(setLocal.getMySQLhost(), String.valueOf(setLocal.getMySQLport()), setLocal.getMySQLdatabase(), setLocal.getMySQLuserName(), setLocal.getMySQLuserPass())) {
        String comanda;
        for (ParametruIR parametriIR : lParametriIR) {
            if (parametriIR.getTabela().isEmpty() || parametriIR.getConditia().isEmpty()) {
                // nu are conditie
            } else {
                comanda = "SELECT count(*) AS nr_inr FROM " + parametriIR.getTabela();
                comanda += " WHERE ";
                comanda += parametriIR.getConditia();
                if (server.execQueryRs(comanda)) {
                    ResultSet rs = server.getResultSet();
                    try {
                        if (rs.next()) {
                            int nrInr = rs.getInt("nr_inr");
                            if (nrInr > 0) {
                                mesaj += parametriIR.getMesajEroare() + "\n\r";
                                response = false;
                                break;
                            }
                        }
                    } catch (SQLException ex) {
                        eroare("SQLException: " + ex.getLocalizedMessage());
                    }
                }
            }
            //server.closeConnection();
            //}
        }
        if (!response) {
            eroare("DataSource.testIntegritateReferentiala\n\r" + mesaj + "\n\nAceastă înregistrare nu poate fi ștearsă.");
        }
        return response;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Structura date">
    private void genereazaDatabase() {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            if (backToBD) {
                String numeBD_back = common.getConfigLocal().getValue("Database") + "_back";
                if (databaseExists(numeBD_back)) {
                    dropDatabase(numeBD_back);
                }
                // creazaBD crm_back
                comanda = "CREATE DATABASE " + numeBD_back + ";";
                server.execQuery(comanda);
                comanda = "CREATE TABLE " + numeBD_Tabela + " (id int AUTO_INCREMENT PRIMARY KEY, data datetime, time BIGINT, user_id int, tabela varchar(100), inserted_id int, updated_id int, deleted_id int, comanda text)";
                server.execQuery(comanda);
            }
            comanda = "CREATE DATABASE " + common.getConfigLocal().getValue("Database") + ";";
            if (server.execQuery(comanda)) {
                writeBack("", comanda, 0, 0, 0);
                writeBack("", "USE " + common.getConfigLocal().getValue("Database") + ";", 0, 0, 0);
                server.closeConnection();
                server.setDatabase(common.getConfigLocal().getValue("Database"));
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
        genereazaTabelaUsers();
    }

    private void genereazaTabelaUsers() {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            //User user = new User(0, "admin", "admin", true, "Administrator", "1111111111");
            TabelaSqlInterface user = common.getTabelaSqlFactory().getTabelaUser(common, false);
            comanda = user.getComandaCreate();
            server.execQuery(comanda);
            writeBack("", comanda, 0, 0, 0);
            // NU adauga admin
            //saveInreg(user);
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
    }

    private void genereazaTabelaSetari() {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            TabelaSqlInterface user = common.getTabelaSqlFactory().getTabelaSetari(common);
            comanda = user.getComandaCreate();
            server.execQuery(comanda);
            writeBack("", comanda, 0, 0, 0);
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
    }

    @Override
    public final void genereazaTabele(PopuleazaInterface populeaza) {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            for (TabelaSqlInterface tabela : common.getListaTabele()) {
                if (!tableExists(tabela.getNumeTabela())) {
                    writeBack("", "# tabela " + tabela.getNumeTabela() + " nu exista", 0, 0, 0);
                    comanda = tabela.getComandaCreate();
                    server.execQuery(comanda);
                    writeBack("", comanda, 0, 0, 0);
                    populeaza.populeazaTabela(tabela.getNumeTabela());
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
    }

    private void stergeTabele() {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            comanda = "SHOW TABLES;";
            if (server.execQueryRs(comanda)) {
                ResultSet rs = server.getResultSet();
                try {
                    while (rs.next()) {
                        comanda = "DROP TABLE `" + rs.getString("tables_in_" + common.getConfigLocal().getValue("Database")) + "`;";
                        server.execQuery(comanda);
                        writeBack("", comanda, 0, 0, 0);
                    }
                    rs.close();
                } catch (SQLException ex) {
                    eroare("SQLException: " + ex.getLocalizedMessage());
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
    }

    private boolean tableExists(String tableName) {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            comanda = "SHOW TABLES;";
            if (server.execQueryRs(comanda)) {
                ResultSet rs = server.getResultSet();
                try {
                    while (rs.next()) {
                        if (rs.getString(1).equals(tableName)) {
                            rs.close();
                            return true;
                        }
                    }
                    rs.close();
                } catch (SQLException ex) {
                    eroare("SQLException: " + ex.getLocalizedMessage());
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
        return false;
    }

    private boolean databaseExists(String databaseName) {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            comanda = "SHOW DATABASES;";
            if (server.execQueryRs(comanda)) {
                ResultSet rs = server.getResultSet();
                try {
                    while (rs.next()) {
                        if (rs.getString("database").equals(databaseName)) {
                            rs.close();
                            return true;
                        }
                    }
                    rs.close();
                } catch (SQLException ex) {
                    eroare("SQLException: " + ex.getLocalizedMessage());
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
        return false;
    }

    private void dropDatabase(String nameBD) {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            if (nameBD.isEmpty()) {
                comanda = "DROP DATABASE " + common.getConfigLocal().getValue("Database") + ";";
            } else {
                comanda = "DROP DATABASE " + nameBD + ";";
            }
            server.execQuery(comanda);
            if (connDeschisaAici) {
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
    }

    private void testUserAdmin() {
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            String comanda;
            comanda = "SELECT count(*) AS nr_inr FROM users;";
            if (server.execQueryRs(comanda)) {
                ResultSet rs = server.getResultSet();
                try {
                    if (rs.next()) {
                        int nrInr = rs.getInt("nr_inr");
                        if (nrInr == 0) {
                            //TabelaSqlInterface user = new User(0, "admin", "admin", true, "Administrator", "a", common);
                            TabelaSqlInterface user = common.getTabelaSqlFactory().getTabelaUser(common, true);
                            saveInreg(user);
                        }
                    }
                } catch (SQLException ex) {
                    eroare("SQLException: " + ex.getLocalizedMessage());
                }
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
    }
    //</editor-fold>

    @Override
    public boolean executaComanda(String comanda) {
        boolean raspuns = false;
        boolean connDeschisaAici = false;
        if (!server.isConnected()) {
            server.openConnection();
            connDeschisaAici = true;
        }
        if (server.isConnected()) {
            if (server.execQuery(comanda)) {
                raspuns = true;
                writeBack("", comanda, 0, 0, 0);
            }
            if (connDeschisaAici) {
                server.closeConnection();
            }
        }
        return raspuns;
    }

    @Override
    public boolean executaComandaRs(String comanda) {
        boolean raspuns = false;
        if (!server.isConnected()) {
            server.openConnection();
        }
        if (server.isConnected()) {
            if (server.execQueryRs(comanda)) {
                //this.rs = server.getResultSet();
                raspuns = true;
                writeBack("", comanda, 0, 0, 0);
            }
            // NU inchid AICI conexiunea deoarece nu mai pot executa rs.next();
        }
        return raspuns;
    }

    private void eroare(String mesaj) {
        isEroare = true;
        mesajEroare = mesaj;
        System.out.println(mesaj);
        if (eDesktop) {
            JOptionPane.showMessageDialog(null, mesajEroare);
        }
    }

    @Override
    public boolean testParola(String user, String password, UserCurent userCurent) {
        boolean response = false;
        if (server.openConnection()) {
            //if (!server.isConnected()) {
            //    server.openConnection();
            //}
            if (server.isConnected()) {
                String comanda = "SELECT * FROM users WHERE user=" + ServerTools.sqlString(user) + " AND password=" + ServerTools.sqlString(CalculMD5.getStringMD5(password));
                if (server.execQueryRs(comanda)) {
                    ResultSet rs = server.getResultSet();
                    String datac_fixa = "T";
                    int anc = 0;
                    int lunac = 0;
                    int ziuac = 0;
                    try {
                        if (rs.next()) {
                            datac_fixa = rs.getString("datac_fixa");
                            anc = rs.getInt("anc");
                            lunac = rs.getInt("lunac");
                            ziuac = rs.getInt("ziuac");
                            if (datac_fixa.equals("F")) {
                                Date datac = new Date();
                                Calendar cal = new GregorianCalendar();
                                cal.setTime(datac);
                                cal.setTimeZone(TimeZone.getTimeZone("Europe/Bucharest"));
                                anc = cal.get(Calendar.YEAR);
                                lunac = cal.get(Calendar.MONTH) + 1;  // month pleaca de la 0
                                ziuac = cal.get(Calendar.DAY_OF_MONTH);
                            }
                            userCurent.setAnc(anc);
                            userCurent.setLunac(lunac);
                            userCurent.setZiuac(ziuac);
                            //
                            userCurent.setId(rs.getInt("id"));
                            userCurent.setUser(rs.getString("user"));
                            userCurent.setNume(rs.getString("nume"));
                            userCurent.setDrepturi(rs.getString("drepturi"));
                            response = true;
                        }
                        rs.close();
                        if (datac_fixa.equals("F")) {
                            comanda = "UPDATE users SET anc=" + anc + ", lunac=" + lunac + ", ziuac=" + ziuac;
                            server.execQuery(comanda);
                        }
                    } catch (SQLException ex) {
                        eroare("SQLException: " + ex.getLocalizedMessage());
                    }
                }
                server.closeConnection();
            }
        } else {
            eroare(server.getMesajEroare());
        }
        return response;
    }

    private void writeBack(String tabela, String comanda, int idInsert, int idUpdate, int idDelete) {
        if (!comanda.startsWith("SELECT ")) {
            if (backToFile) {
                String linie1 = "# " + new Date().getTime() + "|user_id=" + common.getUserCurent().getId();
                if (idInsert > 0) {
                    linie1 += "|inserted_id=" + idInsert;
                }
                TextWriter tw = new TextWriter(numeFis, true);
                tw.writeLine(linie1);
                tw.writeLine(comanda);
                tw.close();
            }
            if (backToBD) {
                String cmd = "INSERT INTO " + numeBD_Tabela + " (data, time, user_id, tabela, inserted_id, updated_id, deleted_id, comanda) VALUES (";
                cmd += " NOW()";
                cmd += "," + new Date().getTime();
                cmd += "," + common.getUserCurent().getId();
                cmd += "," + ServerTools.sqlString(tabela);
                cmd += "," + idInsert;
                cmd += "," + idUpdate;
                cmd += "," + idDelete;
                cmd += "," + ServerTools.sqlString(comanda);
                cmd += ")";
                if (server.isConnected()) {
                    server.execQuery(cmd);
                }
            }
        }
    }
}
