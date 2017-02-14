package dorel.aplicatie.actiuni;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
//import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public final class ActiuneExit extends AbstractAction {

    public ActiuneExit(ImageIcon icon) {
        super("Exit", icon);
        putValue(SHORT_DESCRIPTION, "Părăsire aplicație");
        //putValue(MNEMONIC_KEY, 0);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

}
