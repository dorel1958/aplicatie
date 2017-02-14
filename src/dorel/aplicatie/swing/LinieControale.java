package dorel.aplicatie.swing;

import java.util.ArrayList;
import java.util.List;

public class LinieControale {

    public List<PatraticaControl> patratele;

    public LinieControale() {
        patratele = new ArrayList<>();
    }

    public void addPatratica(PatraticaControl patratica) {
        patratele.add(patratica);
    }
}
