package dorel.aplicatie.back;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.basicopp.swing.PanelFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class BackAdmin extends JFrame implements ActionListener {

    CommonInterface common;
    String numeBD_Tabela;
    JComboBox<String> comboTabela;
    JTextField id;
    JTextField user_id;

    public BackAdmin(CommonInterface common) {
        this.common = common;
        numeBD_Tabela = common.getConfigLocal().getValue("Database") + "_back.comenzi";
        initComponents();
    }

    private void initComponents() {
        setTitle("Back admin");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //<editor-fold defaultstate="collapsed" desc="Panel Center">
        List<Component[]> lCenter = new ArrayList<>();
        Component[] line_comp;
        //
        //  Tabela
        line_comp = new Component[2];
        JLabel labelTabela = new JLabel("Tabela");
        line_comp[0] = labelTabela;
        comboTabela = new JComboBox<>();
        String comanda = "SELECT DISTINCT tabela FROM " + numeBD_Tabela + " ORDER BY tabela;";
        if (common.getDataSource().executaComandaRs(comanda)) {
            ResultSet rs = common.getDataSource().getResultSet();
            try {
                while (rs.next()) {
                    comboTabela.addItem(rs.getString("tabela"));
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "BackAdmin.initComponents - SQLException = " + ex.getLocalizedMessage());
            }
        }
        line_comp[1] = comboTabela;
        lCenter.add(line_comp);
        //
        // inreg_ID
        line_comp = new Component[2];
        JLabel labelId = new JLabel("ID");
        line_comp[0] = labelId;
        id = new JTextField(10);
        line_comp[1] = id;
        lCenter.add(line_comp);
        //
        // user_id
        line_comp = new Component[2];
        JLabel labelUser_id = new JLabel("User ID");
        line_comp[0] = labelUser_id;
        user_id = new JTextField(10);
        line_comp[1] = user_id;
        lCenter.add(line_comp);
        //
        add(PanelFactory.createComponentArray(lCenter), BorderLayout.CENTER);
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Panel South">
        List<Component> lButoane = new ArrayList<>();
        //
        JButton butonExecuta = new JButton("Execută raport");
        butonExecuta.addActionListener(this);
        lButoane.add(butonExecuta);
        //
        JButton butonIesire = new JButton("Ieșire");
        butonIesire.addActionListener(this);
        lButoane.add(butonIesire);
        //
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);
        pack();
        //</editor-fold>

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

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Execută raport":
                //JOptionPane.showMessageDialog(this, "BackAdmin.actionPerformed - Executa tabela:" + comboTabela.getSelectedItem());
                BackRaport br = new BackRaport(common);
                br.executa((String)comboTabela.getSelectedItem(), id.getText(), user_id.getText());
                break;
            case "Ieșire":
                dispose();
                break;
            default:
                JOptionPane.showMessageDialog(this, "BackAdmin.actionPerformed - Comanda necunoscuta: " + e.getActionCommand());
        }
    }
}
