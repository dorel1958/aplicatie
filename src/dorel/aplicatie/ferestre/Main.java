package dorel.aplicatie.ferestre;

import dorel.aplicatie.actiuni.Actiuni;
import dorel.aplicatie.interfaces.CommonInterface;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class Main extends JFrame implements ActionListener {

    protected CommonInterface common;
    public Actiuni actiuni;
    public JMenuBar mainMenu;
    public JPanel panelNorth;
    public JPanel panelWest;
    public JPanel panelCenter;
    public JPanel panelEast;
    public JPanel panelSouth;

    public Main() {
        actiuni=new Actiuni();
    }

    public void SetCommon(CommonInterface common){
        this.common=common;
    }
    
    public void initComponents() {
        setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600,400));
        //
        mainMenu = new JMenuBar();
        this.setJMenuBar(mainMenu);
        //
        panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        this.add(panelNorth, BorderLayout.NORTH);

        panelWest = new JPanel();
        panelWest.setLayout(new FlowLayout());
        this.add(panelWest, BorderLayout.WEST);

        panelCenter = new JPanel();
        panelCenter.setLayout(new FlowLayout());
        this.add(panelCenter, BorderLayout.CENTER);

        panelEast = new JPanel();
        panelEast.setLayout(new FlowLayout());
        this.add(panelEast, BorderLayout.EAST);
        
        panelSouth = new JPanel();
        panelSouth.setLayout(new FlowLayout());
        this.add(panelSouth, BorderLayout.SOUTH);

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

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected boolean testParola() {
        if (common.getDataSource().isEroare()) {
            return false;
        } else {
            FerTestParola ftp = new FerTestParola(common);
            return ftp.getResponse();
        }
    }
}
