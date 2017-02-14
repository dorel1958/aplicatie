package dorel.aplicatie.actiuni;

import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.aplicatie.interfaces.CommonInterface;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
//import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public final class ActiuneTabela extends AbstractAction {

    JFrame frame;
    CommonInterface common;
    String numeTabela;

    public ActiuneTabela(JFrame frame, CommonInterface common, String denumire, String descriere, ImageIcon icon, String numeTabela) {
        super(denumire, icon);
        this.frame=frame;
        this.common = common;
        this.numeTabela=numeTabela;
        putValue(SHORT_DESCRIPTION, descriere);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FerDialogTabela ferUser = new FerDialogTabela(frame, common, numeTabela, false, 0);
    }

}
