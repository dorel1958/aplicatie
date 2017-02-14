package dorel.aplicatie.ferestre;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.basicopp.swing.PanelFactory;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public final class FerTestParola extends JDialog implements ActionListener, KeyListener {

    private JTextField textUser;
    private JPasswordField textPassword;
    private JButton butCorect;
    private JButton butAnulat;
    private boolean response = false;
    CommonInterface common;

    public boolean getResponse() {
        return response;
    }

    public FerTestParola(CommonInterface common) {
        setModal(true);
        this.common = common;
        initComponents();
    }

    private void initComponents() {
        this.setResizable(false);

        Component[] line;
        List<Component[]> lCenter = new ArrayList<>();
        //
        line = new Component[2];
        JLabel lUser = new JLabel("Utilizator:");
        line[0] = lUser;
        textUser = new JTextField(10);
        line[1] = textUser;
        lCenter.add(line);
        //
        line = new Component[2];
        JLabel lParola = new JLabel("Parola:");
        line[0] = lParola;
        textPassword = new JPasswordField(20);
        textPassword.addActionListener(this);
        line[1] = textPassword;
        lCenter.add(line);
        //
        JPanel panel = PanelFactory.createComponentArray(lCenter);
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, textUser, textPassword);

        add(panel, BorderLayout.CENTER);

        // <editor-fold defaultstate="collapsed" desc="Bottom Buttons">      
        List<Component> lButoane = new ArrayList<>();
        butCorect = new JButton("Corect");
        butCorect.addActionListener(this);
        butCorect.addKeyListener(this);
        lButoane.add(butCorect);
        //
        butAnulat = new JButton("Anulat");
        butAnulat.addActionListener(this);
        butAnulat.addKeyListener(this);
        lButoane.add(butAnulat);
        //
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);
        //</editor-fold>        

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
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Corect":
                test();
                break;
            case "Anulat":
                this.setVisible(false);
                break;
            default:
                test();
        }
    }

    private boolean test() {
        char[] input = textPassword.getPassword();
        String str = String.valueOf(input);
        response = common.getDataSource().testParola(textUser.getText(), str, common.getUserCurent());
        Arrays.fill(input, '0');
        if (response) {
            this.setVisible(false);
            return true;
        } else {
            JOptionPane.showMessageDialog(this,
                    "Parola este greşită. Mai încercaţi.",
                    "Mesaj eroare",
                    JOptionPane.ERROR_MESSAGE);
            textPassword.setText("");
            textPassword.requestFocus();
            return false;
        }
    }

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
                if (e.getComponent().equals(butCorect)) {
                    test();
                }
                if (e.getComponent().equals(butAnulat)) {
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
}
