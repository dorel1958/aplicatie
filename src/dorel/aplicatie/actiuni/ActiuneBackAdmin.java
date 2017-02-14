package dorel.aplicatie.actiuni;

import dorel.aplicatie.back.BackAdmin;
import dorel.aplicatie.interfaces.CommonInterface;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class ActiuneBackAdmin extends AbstractAction {

    CommonInterface common;

    public ActiuneBackAdmin(CommonInterface common, ImageIcon icon) {
        super("Back admin", icon);
        this.common = common;
        putValue(SHORT_DESCRIPTION, "Back admin");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BackAdmin ba = new BackAdmin(common);
    }

}
