package dorel.aplicatie.actiuni;

import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;

public class Actiuni {

    private Map<String, AbstractAction> mapActiuni;

    public Actiuni() {
        mapActiuni = new HashMap<>();
        mapActiuni.put("exit", new ActiuneExit(new javax.swing.ImageIcon(getClass().getResource("/dorel/aplicatie/resources/exit.png"))));
    }

    public void addActiune(String nume, AbstractAction action) {
        mapActiuni.put(nume, action);
    }

    public AbstractAction getActiune(String nume) {
        return mapActiuni.get(nume);
    }
}
