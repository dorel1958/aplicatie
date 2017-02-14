package dorel.aplicatie.swing;

import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListaCopii extends JPanel implements ListSelectionListener, MouseListener {

    CommonInterface common;
    TabelaSqlInterface tabela;
    MyListModel listModel;
    JList lista;
    int indexSelectat;

    public ListaCopii(CommonInterface common, TabelaSqlInterface tabela) {
        this.common = common;
        this.tabela = tabela;
        initComponents();
        // init values
    }

    public void populeazaLista(int idParinte) {
        String newFilter = String.valueOf(idParinte);
        String actualFilter = tabela.getActualFilter(newFilter, 0, true);
        indexSelectat = listModel.refresh(actualFilter);
        lista.setSelectedIndex(indexSelectat);
    }

    private void initComponents() {
        listModel = new MyListModel(common, tabela);
        lista = new JList(listModel);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.addListSelectionListener(this);
//        if (isSelect) {
        lista.addMouseListener(this);
//        }
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setPreferredSize(new Dimension(200, 400));
        this.add(scroll);
    }

    public void setStare(FerDialogTabela.Stare stare) {
        switch (stare) {
            case EDITARE:
                lista.setEnabled(false);
                break;
            case VIZUALIZARE:
                lista.setEnabled(true);
                break;
            default:
                JOptionPane.showMessageDialog(null, "ListaCopii.setStare - Stare necunoscuta:" + stare.name());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="ListSelectionListener">
    @Override
    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getSource() == lista && !lse.getValueIsAdjusting()) {
            indexSelectat = lista.getSelectedIndex();
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Mouse Listener">
    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getClickCount() == 2) {
            //this.setVisible(false);
            int id = ((TabelaSqlInterface) (listModel.getElementAt(indexSelectat))).getId();
            FerDialogTabela fd = new FerDialogTabela(null, common, tabela.getNumeTabela(), false, id);
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
}
