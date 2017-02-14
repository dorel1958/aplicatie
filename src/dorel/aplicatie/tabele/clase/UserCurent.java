package dorel.aplicatie.tabele.clase;

// datele userului curent logat in sistem
public class UserCurent {

    private int id;
    private String user;
    private String drepturi;
    private String nume;

    private int anc;
    private int lunac;
    private int ziuac;

    private String filtru_id;
    private String filtru_string;

    //<editor-fold defaultstate="collapsed" desc="Get Set">
    public String getFiltru_id() {
        return filtru_id;
    }

    public void setFiltru_id(String filtru_id) {
        this.filtru_id = filtru_id;
    }


    public String getFiltru_string() {
        return filtru_string;
    }

    public void setFiltru_string(String filtru_string) {
        this.filtru_string = filtru_string;
    }

    public int getZiuac() {
        return ziuac;
    }

    public void setZiuac(int ziuac) {
        this.ziuac = ziuac;
    }

    public int getAnc() {
        return anc;
    }

    public void setAnc(int anc) {
        this.anc = anc;
    }


    public int getLunac() {
        return lunac;
    }

    public void setLunac(int lunac) {
        this.lunac = lunac;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public void setDrepturi(String drepturi) {
        this.drepturi = drepturi;
    }

    public String getUser() {
        return user;
    }

    public String getNume() {
        return nume;
    }
    //</editor-fold>

    public UserCurent(String user, String nume, String drepturi) {
        this.user = user;
        this.drepturi = drepturi;
        this.nume = nume;
    }

    public boolean areDreptul(String x) {
        return drepturi.indexOf(x) > 0;
    }

}
