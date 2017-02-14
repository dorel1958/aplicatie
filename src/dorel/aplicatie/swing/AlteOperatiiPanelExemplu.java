package dorel.aplicatie.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlteOperatiiPanelExemplu implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("e.getActionCommand()=" + e.getActionCommand() + " e.getSource().getClass().getName()=" + e.getSource().getClass().getName());
    }

}
