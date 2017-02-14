package dorel.aplicatie.swing;

import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Referinta extends JPanel implements ActionListener {

    CommonInterface common;
    String numeTabela;
    TabelaSqlInterface tabela;

    int idSelectat;
    JTextField textDenumire;
    JButton butMofif;
    List<ActionListener> listaListeners;
    int width;

    public void setMyEnabled(boolean enabled) {
        butMofif.setEnabled(enabled);
    }

    public void setIdSelectat(int id) {
        this.idSelectat = id;
        if (idSelectat == 0) {
            textDenumire.setText("");
        } else {
            List<TabelaSqlInterface> lCG = common.getDataSource().listInreg(numeTabela, id, "", "");
            if (lCG.isEmpty()) {
                textDenumire.setText("");
            } else {
                textDenumire.setText(lCG.get(0).toString());
            }
        }
        notifyListeners("setIdSelectat");
    }

    public int getIdSelectat() {
        return idSelectat;
    }

    public Referinta(CommonInterface common, String numeTabela, int width) {
        this.numeTabela = numeTabela;
        this.common = common;
        this.tabela = common.getTabelaSqlFactory().getTabela(numeTabela, common);
        listaListeners=new ArrayList<>();
        this.width=width;
        initComponent();
    }

    private void initComponent() {
        setLayout(new FlowLayout());
        textDenumire = new JTextField(width);
        textDenumire.setEditable(false);
        textDenumire.setDisabledTextColor(Color.BLACK);
        this.add(textDenumire);
        butMofif = new JButton("...");
        butMofif.setPreferredSize(new Dimension(20, 20));
        butMofif.addActionListener(this);
        this.add(butMofif);
        this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "...":
                FerDialogTabela fd = new FerDialogTabela(null, common, numeTabela, true, idSelectat);
                int noulIdSelectat = fd.getIdSelectat();
                switch (noulIdSelectat) {
                    case -1:
                        // lasa asa
                        break;
                    case 0:
                        setIdSelectat(0);
                        break;
                    default:
                        setIdSelectat(noulIdSelectat);
                }
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + ae.getActionCommand());
        }
    }

    public void setStare(FerDialogTabela.Stare stare) {
        switch (stare) {
            case VIZUALIZARE:
                this.butMofif.setEnabled(false);
                break;
            case EDITARE:
                this.butMofif.setEnabled(true);
                break;
        }
    }

    //<editor-fold defaultstate="collapsed" desc="listeners">
    private void notifyListeners(String actionEventString){
        ActionEvent ae = new ActionEvent(this, idSelectat, actionEventString);
        for(ActionListener al:listaListeners){
            al.actionPerformed(ae);
        }
    }
    
    public void addActionListener(ActionListener al){
        listaListeners.add(al);
    }
    //</editor-fold>
}
