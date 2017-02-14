package dorel.aplicatie.ferestre;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import dorel.aplicatie.listeners.MyPropertyChangeListener;
import dorel.aplicatie.swing.ListaCopii;
import dorel.aplicatie.swing.MyListModel;
import dorel.aplicatie.swing.PanelButoane;
import dorel.aplicatie.swing.Referinta;
import dorel.basicopp.swing.PanelFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FerDialogTabela extends JDialog implements ActionListener, ListSelectionListener, MouseListener, KeyListener {

    public enum Stare {

        EDITARE,
        VIZUALIZARE
    }
    CommonInterface common;
    //
    boolean isSelect;  // fereastra e utilizata pentru a selecta o valoare din lista
    //
    int indexSelectat;  // indexul selectat in lista W
    int idSelectatInitial;  // id primit la initializare pt a se pozitiona pe inreg cu acest id
    TabelaSqlInterface tabela;  // utilizata a extrage configurari si a gestiona valorile din controale
    TabelaSqlInterface tabelaInitiala;  // tablea cu valorile initiale in controale (utilizata la comanda 'AnulatOperatieSelectie')
    TabelaSqlInterface tabelaTempo;  // tabela pentru a tine valorile afisate in controale (utilizata la comanda 'Adauga' si 'Anulat')
    //
    PanelButoane panelN;  // lista butoane aplcatie
    //
    // filreu si lista selectie inregistrari - W
    JPanel panelLista;
    JTextField filter;
    Referinta referintaFilter;
    MyListModel lModel;
    JList lista;
    //
    // ListaCopii - E
    ListaCopii panelE;
    //
    // setul de butoane - S
    JButton butAddNew;
    JButton butDelete;
    JButton butEdit;
    JButton butSave;
    JButton butAnulat;
    JButton butListeaza;
    JButton butAnulatOperatieSelectie;
    JButton butSelectieNula;
    JButton butExit;
    //
    String oldFilter = "";
    int oldIdRef = 0;
    Stare stare;

    private int getIdRef() {
        int idRef = 0;
        if (referintaFilter != null) {
            idRef = referintaFilter.getIdSelectat();
        }
        return idRef;
    }

    public int getIdSelectat() {
        if (indexSelectat == -1) {
            return 0;
        }
        return tabela.getId();
    }

    public FerDialogTabela(JFrame frame, CommonInterface common, String numeTabela, boolean isSelect, int idSelectatInitial) {
        super(frame, true);
        this.tabela = common.getTabelaSqlFactory().getTabela(numeTabela, common);
        this.isSelect = isSelect;
        this.idSelectatInitial = idSelectatInitial;
        this.common = common;
        //this.idSelectat = idSelectat;
        indexSelectat = -1;
        tabelaTempo = common.getTabelaSqlFactory().getTabela(numeTabela, common);
        tabela.setId(idSelectatInitial);
        this.initComponents();  // va fi ultima instructiune!!
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        setLayout(new BorderLayout());
        this.setTitle(tabela.getWindowTitle());

        //<editor-fold defaultstate="collapsed" desc="North PanelButoane">
        panelN = tabela.getPanelNorth(this);
        add(panelN, BorderLayout.NORTH);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Left Lista">
        lModel = new MyListModel(common, tabela);

        Component[] line;
        List<Component[]> lLeft = new ArrayList<>();
        //
        if (!tabela.getNumeTabelaFiltruReferinta().isEmpty()) {
            line = new Component[1];
            referintaFilter = new Referinta(common, tabela.getNumeTabelaFiltruReferinta(), tabela.getWidthFiltruReferunta());
            referintaFilter.addActionListener(this);
            line[0] = referintaFilter;
            lLeft.add(line);
        }
        //
        line = new Component[1];
        filter = new JTextField(10);
        filter.setActionCommand("modifFilter");
        filter.addActionListener(this);
        line[0] = filter;
        lLeft.add(line);
        // pus dupa initializarea filter !!!
        //
        line = new Component[1];
        lista = new JList(lModel);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.addListSelectionListener(this);
        if (isSelect) {
            lista.addMouseListener(this);
        }
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(tabela.getDimListaW());
        line[0] = scroll;
        lLeft.add(line);
        //
        panelLista = PanelFactory.createComponentArray(lLeft);
        add(panelLista, BorderLayout.WEST);
        ((GroupLayout) panelLista.getLayout()).linkSize(SwingConstants.HORIZONTAL, filter, scroll);  // le face pe toate la fel de late
        //</editor-fold> 

        // <editor-fold defaultstate="collapsed" desc="Center MyPanel">
        add(tabela.getPanel(), BorderLayout.CENTER);
        //</editor-fold> 

        //<editor-fold defaultstate="collapsed" desc="Right ListaCopii">
        panelE = tabela.getPanelEast();
        if (panelE != null) {
            add(panelE, BorderLayout.EAST);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Bottom Buttons">
        List<Component> lButoane = new ArrayList<>();
        butAddNew = new JButton("Adaugă");
        butAddNew.addActionListener(this);
        butAddNew.addKeyListener(this);
        lButoane.add(butAddNew);
        butDelete = new JButton("Şterge");
        butDelete.addActionListener(this);
        butDelete.addKeyListener(this);
        lButoane.add(butDelete);
        butEdit = new JButton("Editează");
        butEdit.addActionListener(this);
        butEdit.addKeyListener(this);
        lButoane.add(butEdit);
        //
        Component spatiu = Box.createRigidArea(new Dimension(40, 0));
        lButoane.add(spatiu);
        //
        butSave = new JButton("Salvează");
        butSave.addActionListener(this);
        butSave.addKeyListener(this);
        lButoane.add(butSave);
        butAnulat = new JButton("Anulat");
        butAnulat.addActionListener(this);
        butAnulat.addKeyListener(this);
        lButoane.add(butAnulat);
        //
        Component spatiu1 = Box.createRigidArea(new Dimension(40, 0));
        lButoane.add(spatiu1);
        //
        butListeaza = new JButton("Listează");
        butListeaza.addActionListener(this);
        butListeaza.addKeyListener(this);
        lButoane.add(butListeaza);
        //
        Component spatiu2 = Box.createRigidArea(new Dimension(40, 0));
        lButoane.add(spatiu2);

        if (isSelect) {
            butAnulatOperatieSelectie = new JButton("Anulare selecție");
            butAnulatOperatieSelectie.setActionCommand("butAnulatOperatieSelectie");
            butAnulatOperatieSelectie.addActionListener(this);
            butAnulatOperatieSelectie.addKeyListener(this);
            lButoane.add(butAnulatOperatieSelectie);
            //
            butSelectieNula = new JButton("Selectez nul");
            butSelectieNula.setActionCommand("butSelectieNula");
            butSelectieNula.addActionListener(this);
            butSelectieNula.addKeyListener(this);
            lButoane.add(butSelectieNula);
            //
            butExit = new JButton("Selectez");
        } else {
            butExit = new JButton("Ieşire");
        }
        butExit.setActionCommand("Exit");
        butExit.addActionListener(this);
        butExit.addKeyListener(this);
        lButoane.add(butExit);
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);
        //</editor-fold>        

        indexSelectat = lModel.refresh("");
        lista.setSelectedIndex(indexSelectat);
        tabelaInitiala = lModel.get(indexSelectat);
        setStare(Stare.VIZUALIZARE);

        pack();

        //<editor-fold defaultstate="collapsed" desc="Center in Screen">
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = 0;
        if (dim.width > w) {
            x = (dim.width - w) / 2;
        }
        int y = 0;
        if (dim.height > h) {
            y = (dim.height - h) / 2;
        }
        // Move the window
        this.setLocation(x, y);
        //</editor-fold>

        setVisible(true);
    }

    public void setStare(Stare stare) {
        tabela.getMyPanel().setStare(stare, tabela.getNumeControlFocus());
        if (panelN != null) {
            panelN.setStare(stare);
        }
        if (panelE != null) {
            panelE.setStare(stare);
        }
        switch (stare) {
            case VIZUALIZARE:
                butAddNew.setEnabled(true);
                if (indexSelectat == -1) {
                    butDelete.setEnabled(false);
                    butEdit.setEnabled(false);
                    filter.setEnabled(false);
                    lista.setEnabled(false);
                    if (referintaFilter != null) {
                        referintaFilter.setEnabled(false);
                    }
//                    if (panelE != null) {
//                        panelE.setEnabled(false);
//                    }
                } else {
                    butDelete.setEnabled(true);
                    butEdit.setEnabled(true);
                    filter.setEnabled(true);
                    lista.setEnabled(true);
                    if (referintaFilter != null) {
                        referintaFilter.setEnabled(true);
                    }
//                    if (panelE != null) {
//                        panelE.setEnabled(true);
//                    }
                }
                //
                butSave.setEnabled(false);
                butAnulat.setEnabled(false);
                butListeaza.setEnabled(true);
                butExit.setEnabled(true);
                break;
            case EDITARE:
                butAddNew.setEnabled(false);
                butDelete.setEnabled(false);
                butEdit.setEnabled(false);
                butSave.setEnabled(true);
                butAnulat.setEnabled(true);
                butListeaza.setEnabled(false);
                butExit.setEnabled(false);
                filter.setEnabled(false);
                lista.setEnabled(false);
                if (referintaFilter != null) {
                    referintaFilter.setEnabled(false);
                }
//                if (panelE != null) {
//                    panelE.setEnabled(false);
//                }
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        // reactia la click pe butoane
        switch (ae.getActionCommand()) {
            case "Adaugă":
                executaComandaAdauga();
                break;
            case "Şterge":
                executaComandaSterge();
                break;
            case "Editează":
                setStare(Stare.EDITARE);
                break;
            case "Salvează":
                executaComandaSave();
                break;
            case "Anulat":
                executaComandaAnulat();
                break;
            case "Listează":
                executaComandaListeaza();
                break;
            case "butSelectieNula":
                executaComandaSelectieNula();
                break;
            case "butAnulatOperatieSelectie":
                executaComandaAnulatOperatieSelectie();
                break;
            case "Exit":
                this.setVisible(false);
                break;
            case "Generează raportul":
                JOptionPane.showMessageDialog(this, "raport");
                break;
            case "modifFilter":
            case "setIdSelectat":
                // reactia la enter pe filter sau modificare
                String newFilter = filter.getText();
                int idRef = getIdRef();
                if (!newFilter.equals(oldFilter) || oldIdRef != idRef) {
                    String actualFilter = tabela.getActualFilter(newFilter, idRef, false);
                    indexSelectat = lModel.refresh(actualFilter);
                    lista.setSelectedIndex(indexSelectat);
                    oldFilter = newFilter;
                    oldIdRef = idRef;
                    //
                    // fara codul de mai jos NU schimba datele de pe randul selectat cate o data la modificarea filtrului
                    if (indexSelectat > -1) {
                        ListSelectionEvent lse = new ListSelectionEvent(lista, 0, 0, false);
                        this.valueChanged(lse);
                    } else {
                        // pentru a nu se bloca atunci cand trece de la lista principala cu elemente la lista principala goala
                        tabela.setValuesFromTabela(lModel.get(indexSelectat));
                        tabela.puneValoriInControale();
                    }
                }
                break;
            default:
                break;
        }
    }

    // <editor-fold defaultstate="collapsed" desc="ListSelectionListener">
    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getSource() == lista && !lse.getValueIsAdjusting()) {
            indexSelectat = lista.getSelectedIndex();
            if (indexSelectat > -1) {
                tabela.setValuesFromTabela(lModel.get(indexSelectat));
                tabela.puneValoriInControale();
            }
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Mouse Listener">
    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            this.setVisible(false);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="KeyListener">
    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ENTER:
                if (e.getComponent().equals(butAddNew)) {
                    executaComandaAdauga();
                }
                if (e.getComponent().equals(butDelete)) {
                    executaComandaSterge();
                }
                if (e.getComponent().equals(butEdit)) {
                    setStare(Stare.EDITARE);
                }
                if (e.getComponent().equals(butSave)) {
                    executaComandaSave();
                }
                if (e.getComponent().equals(butAnulat)) {
                    executaComandaAnulat();
                }
                if (e.getComponent().equals(butSelectieNula)) {
                    executaComandaSelectieNula();
                }
                if (e.getComponent().equals(butAnulatOperatieSelectie)) {
                    executaComandaAnulatOperatieSelectie();
                }
                if (e.getComponent().equals(butExit)) {
                    this.setVisible(false);
                }
                break;
            default:

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Comenzi">
    private void executaComandaAdauga() {
        tabelaTempo.setValuesFromTabela(tabela);
        indexSelectat = -1;
        tabela.puneValoriImplicite();  // id se pune 0
        tabela.puneValoriInControale();
        lista.setSelectedIndex(-1);
        setStare(Stare.EDITARE);
    }

    private void executaComandaSterge() {
        String newFilter = filter.getText();
        String actualFilter = tabela.getActualFilter(newFilter, getIdRef(), false);
        if (indexSelectat != -1) {
            if (common.getDataSource().deleteInreg(((TabelaSqlInterface) lModel.getElementAt(indexSelectat)))) {
                lModel.removeElement(indexSelectat);
                // pozitionare dupa stergere
                int newIndexSelectat;
                if (indexSelectat > 0) {
                    newIndexSelectat = indexSelectat - 1;
                } else {
                    if (lModel.getSize() == 0) {
                        newIndexSelectat = -1;
                    } else {
                        newIndexSelectat = 0;
                    }
                }
                tabela.setValuesFromTabela(lModel.get(newIndexSelectat));
                indexSelectat = lModel.refresh(actualFilter);
                lista.setSelectedIndex(indexSelectat);
                //if (indexSelectat == -1) {
                tabela.puneValoriInControale();
                //}
            }
            if (indexSelectat == -1) {
                setStare(Stare.VIZUALIZARE);  // inca o data pt butoanele Sterge si Editeaza la newIndexSelectat = -1
            }
        } else {
            JOptionPane.showMessageDialog(this, "Nu aveţi înregistrări de şters.");
        }
    }

    private void executaComandaAnulat() {
        String newFilter = filter.getText();
        String actualFilter = tabela.getActualFilter(newFilter, getIdRef(), false);
        if (indexSelectat == -1) {
            tabela.setValuesFromTabela(tabelaTempo);
            indexSelectat = lModel.refresh(actualFilter);
            lista.setSelectedIndex(indexSelectat);
        }
        tabela.puneValoriInControale();
        setStare(Stare.VIZUALIZARE);
    }

    private void executaComandaSave() {
        tabela.iaValoriDinControale();
        if (tabela.testCorect()) {
            if (common.getDataSource().saveInreg(tabela)) {
                String newFilter = filter.getText();
                String actualFilter = tabela.getActualFilter(newFilter, getIdRef(), false);
                indexSelectat = lModel.refresh(actualFilter);
                lista.setSelectedIndex(indexSelectat);
                setStare(Stare.VIZUALIZARE);
            } else {
                //tabela.puneValoriInControale(); //  NU - pierd parola la cond de unicitate
            }
        }
    }

    private void executaComandaListeaza() {
        String numeFis = "list.html";
        tabela.genLista(numeFis);
        try {
            Runtime rt = Runtime.getRuntime();
            String path = new java.io.File(".").getCanonicalPath();
            Process pr = rt.exec("C:\\Program Files\\Internet Explorer\\iexplore.exe " + path + "\\" + numeFis);
        } catch (HeadlessException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Eroare.afisazaFisierErori -" + ex.getLocalizedMessage());
        }
    }

    private void executaComandaSelectieNula() {
        tabela.setId(0);
        this.setVisible(false);
    }

    private void executaComandaAnulatOperatieSelectie() {
        tabela = tabelaInitiala;
        this.setVisible(false);
    }
    //</editor-fold>

}
