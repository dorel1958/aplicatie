package dorel.aplicatie;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.tabele.clase.PopuleazaHelper;
import dorel.basicopp.swing.PanelFactory;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Test extends JFrame implements ActionListener {

    public static void main(String[] args) {
        Test main = new Test();
        main.initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Test Aplicatie");
        setLayout(new BorderLayout());

        Component[] line;
        List<Component[]> componente = new ArrayList<>();
        //
        line = new Component[3];
        JButton butTest = new JButton("Test");
        butTest.addActionListener(this);
        line[0] = butTest;
        JLabel labelAsteptati = new JLabel();
        labelAsteptati.setPreferredSize(new Dimension(150, 30));
        line[1] = labelAsteptati;
        JButton butIesire = new JButton("Iesire");
        butIesire.addActionListener(this);
        line[2] = butIesire;
        componente.add(line);
        JPanel panelL = PanelFactory.createComponentArray(componente);
        add(panelL, BorderLayout.CENTER);
        pack();

        // <editor-fold defaultstate="collapsed" desc="Center in Screen">      
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
            case "Test":
                //JOptionPane.showMessageDialog(this, "Test");
                // setare configLocal
//                ConfigLocalInterface configLocal = new ConfigLocalHelper("MySQL");
//                configLocal.setValue("Server", "MySQL");
//                configLocal.setValue("Host", "localhost");
//                configLocal.setValue("Port", "3307");
//                configLocal.setValue("Database", "hotel");
//                configLocal.setValue("User", "root");
//                configLocal.setValue("Pass", "");
//                configLocal.readSettings();  // aici daca gaseste fisierul de config pune valorile de acolo, daca nu le salveaza pe acestea.
                //
                // setare tabele (Users e pusa din oficiu)
                //List<TabelaSqlInterface> listTabele = new ArrayList<>();
                CommonInterface common = new CommonHelper();
                DataSourceHelper dataSource = new DataSourceHelper(common, new PopuleazaHelper(common), true);
                //
                // test parola
//                if (!dataSource.isEroare()) {
//                    FerTestParola ftp = new FerTestParola(dataSource);
//                    if (ftp.getResponse()) {
//                        JOptionPane.showMessageDialog(this, "Corect");
//                        //TabelaSql user = new User();
//                        //FerDialogTabela fdt = new FerDialogTabela(this, dataSource, user, false);
//                    } else {
//                        JOptionPane.showMessageDialog(this, "Gresit");
//                    }
//                }
                // users
                //TabelaSqlInterface user = new User();
                //FerDialogTabela fdt1 = new FerDialogTabela(this, common, user, false);
//
//                // in mod select
//                user.setId(256);
//                FerDialogTabela fdt2 = new FerDialogTabela(this, dataSource, user, true);
//                JOptionPane.showMessageDialog(this, "idSelectat=" + fdt2.getIdSelectat());

                break;
            case "Iesire":
                System.exit(0);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necumoscuta:" + e.getActionCommand());
        }
    }
}
