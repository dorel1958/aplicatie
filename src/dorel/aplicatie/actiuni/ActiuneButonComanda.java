package dorel.aplicatie.actiuni;

import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.interfaces.CommonInterface;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class ActiuneButonComanda extends AbstractAction {

    protected FerDialogTabela ferDialogTabela;
    protected CommonInterface common;

    public ActiuneButonComanda(String denumire, String descriere, CommonInterface common, ImageIcon icon) {
        super(denumire, icon);
        putValue(SHORT_DESCRIPTION, descriere);
        //putValue(MNEMONIC_KEY, 0);
        this.common = common;
    }

    public void setFerDialogTabela(FerDialogTabela ferDialogTabela) {
        this.ferDialogTabela = ferDialogTabela;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //RapoarteDialog rapoarte = new RapoarteDialog(frame, common);
        JOptionPane.showMessageDialog(null, "Raport idSelectat=" + ferDialogTabela.getIdSelectat());
    }

}
