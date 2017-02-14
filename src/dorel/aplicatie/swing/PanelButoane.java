package dorel.aplicatie.swing;

import dorel.aplicatie.actiuni.ActiuneButonComanda;
import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.ferestre.FerDialogTabela.Stare;
import java.awt.Component;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public final class PanelButoane extends JPanel {

    public PanelButoane(List<ActiuneButonComanda> listaActiuni, FerDialogTabela ferDialogTabela) {
        for (ActiuneButonComanda action : listaActiuni) {
            action.setFerDialogTabela(ferDialogTabela);
            JButton butonul = new JButton(action);
            this.add(butonul);
        }
    }
    
    public void setStare(Stare stare){
        Component[] lComp;
        lComp = this.getComponents();
        
        switch (stare){
            case EDITARE:
                for(Component component: this.getComponents()){
                    if(component.getClass().getName().equals("javax.swing.JButton")){
                        component.setEnabled(false);
                    }
                }
                break;
            case VIZUALIZARE:
                for(Component component: this.getComponents()){
                    if(component.getClass().getName().equals("javax.swing.JButton")){
                        component.setEnabled(true);
                    }
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "PanelButoane.setStare - Stare necunoscuta:"+stare.name());
        }
    }
}
