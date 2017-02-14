package dorel.aplicatie.swing;

import dorel.aplicatie.ferestre.FerDialogTabela;
import dorel.basicopp.sumecontrol.CalculMD5;
import dorel.basicopp.swing.PanelFactory;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class MyPanel {

    List<LinieControale> liniiControale;
    List<Component> componente;

    public MyPanel() {
        liniiControale = new ArrayList<>();
        componente = new ArrayList<>();
    }

    public void addLinie(LinieControale linie) {
        // utilizata de TabelaSqlHelper pentru a adauga linii de controale
        liniiControale.add(linie);
    }

    //public JPanel getPanel(int nrMaxLinii, int nrColoane, PropertyChangeListener listener) {
    public JPanel getPanel(int nrMaxLinii, int nrColoane) {
        if (nrMaxLinii == 0) {
            nrMaxLinii = 30;
        }
        int nrLiniiControale = liniiControale.size();
        int rap_int = nrLiniiControale / nrMaxLinii;
        double fract = nrLiniiControale % nrMaxLinii;
        int nrColoaneControale = rap_int;
        if (fract != 0) {
            nrColoaneControale += 1;
        }
        if (nrColoaneControale == 1) {  // nu umplu toate nrMaxLinii -> numai pana la nrLiniiControale
            nrMaxLinii = nrLiniiControale;
        }
        // populeaza Array-ul cu pozitiile pentru componente
        Component[] line_comp;
        List<Component[]> lCenter = new ArrayList<>();
        for (int i = 0; i < nrMaxLinii; i++) {
            line_comp = new Component[nrColoane * nrColoaneControale];
            lCenter.add(line_comp);
        }
        // incepe umplerea casutelor din Array-ul componentelor cu componentele din liniiControale
        int x_coloana = 0;
        int x;
        int y = -1;
        for (LinieControale linie : liniiControale) {
            y += 1;
            if (y == nrMaxLinii) {
                y = 0;
                x_coloana += nrColoane;
            }
            x = x_coloana;  // se repune pe pozitia initiala in coloana in curs
            for (PatraticaControl patratica : linie.patratele) {
                switch (patratica.getTip()) {
                    case LABEL:
                        JLabel label = new JLabel(patratica.getLabel());
                        label.setName(patratica.getName());
                        componente.add(label);
                        lCenter.get(y)[x] = label;
                        break;
                    case TEXT:
                        JTextField text = new JTextField(patratica.getDimX());
                        text.setName(patratica.getName());
                        text.setText(patratica.getValoare());
                        text.setDisabledTextColor(Color.black);
                        //text.addPropertyChangeListener(listener);
                        componente.add(text);
                        lCenter.get(y)[x] = text;
                        break;
                    case PASSWORD:
                        JPasswordField pass = new JPasswordField(patratica.getDimX());
                        pass.setName(patratica.getName());
                        pass.setDisabledTextColor(Color.black);
                        //pass.addPropertyChangeListener(listener);
                        componente.add(pass);
                        lCenter.get(y)[x] = pass;
                        //
                        JCheckBox checp = new JCheckBox(patratica.getLabel());
                        checp.setName("checkp_" + patratica.getName());
                        checp.setText("salvez parola");
                        checp.setSelected(true);
                        componente.add(checp);
                        x += 1;
                        lCenter.get(y)[x] = checp;
                        //
                        break;
                    case CHECKBOX:
                        JCheckBox chec = new JCheckBox(patratica.getLabel());
                        chec.setName(patratica.getName());
                        chec.setText((patratica.getLabel()));
                        //chec.setSelected(patratica.getValoare().equals("T"));
                        //CheckBox.background
                        //CheckBox.disabledText
                        //CheckBox.foreground
                        //CheckBox.select
                        //
                        //Label.disabledForeground
                        //UIManager.put("CheckBox.disabledText", Color.black);
                        chec.setSelected(false);
                        //chec.addPropertyChangeListener(listener);
                        componente.add(chec);
                        lCenter.get(y)[x] = chec;
                        break;
                    case COMBOBOX:
                        JComboBox combo = new JComboBox(patratica.getComboBoxModel());
                        combo.setName(patratica.getName());
                        //combo.addPropertyChangeListener(listener);
                        //((JTextField)combo.getComponent(1)).setDisabledTextColor(Color.red);
                        UIManager.put("ComboBox.disabledForeground", Color.black);
                        componente.add(combo);
                        lCenter.get(y)[x] = combo;
                        break;
                    case REFERINTA:
                        Referinta referinta = new Referinta(patratica.getCommon(), patratica.getNumeTabela(), 20);
                        referinta.setName(patratica.getName());
                        //referinta.addPropertyChangeListener(listener);
                        componente.add(referinta);
                        lCenter.get(y)[x] = referinta;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "MyPanel.getPanel - componenta necunoscuta." + patratica.getTip().name());
                }
                x += 1;
            }
        }
        return PanelFactory.createComponentArray(lCenter);
    }

    public void puneValoareInControl(String numeControl, String valoare) {
        for (Component component : componente) {
            if (component.getName().equals(numeControl)) {
                switch (component.getClass().getSimpleName()) {
                    case "JLabel":
                        ((JLabel) component).setText(valoare);
                        break;
                    case "JTextField":
                        ((JTextField) component).setText(valoare);
                        break;
                    case "JPasswordField":
                        ((JPasswordField) component).setText("");
                        break;
                    case "JCheckBox":
                        ((JCheckBox) component).setSelected(valoare.equals("T"));
                        break;
                    case "JComboBox":
                        ((JComboBox) component).setSelectedItem(valoare);
                        ((JComboBox) component).repaint();
                        break;
                    case "JList":
                        ((JList) component).setSelectedIndex(0);
                        ((JList) component).repaint();
                        break;
                    case "Referinta":
                        if (valoare.isEmpty()) {
                            ((Referinta) component).setIdSelectat(0);
                        } else {
                            ((Referinta) component).setIdSelectat(Integer.parseInt(valoare));
                        }
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "MyPanel.puneValoareInControl - componenta necunoscuta.");
                }
            }
            if (component.getName().equals("checkp_" + numeControl)) {
                if (valoare.isEmpty()) {
                    ((JCheckBox) component).setSelected(true);
                } else {
                    ((JCheckBox) component).setSelected(false);
                }
            }
        }
    }

    public String getValoareDinControl(String numeControl) {
        boolean iaPassword = false;
        for (Component component : componente) {
            if (component.getName().equals("checkp_" + numeControl)) {
                if (((JCheckBox) component).isSelected()) {
                    iaPassword = true;
                }
            }
        }
        for (Component component : componente) {
            if (component.getName().equals(numeControl)) {
                switch (component.getClass().getSimpleName()) {
                    case "JLabel":
                        return ((JLabel) component).getText();
                    case "JTextField":
                        return ((JTextField) component).getText();
                    case "JPasswordField":
                        if (iaPassword) {
                            char[] input = ((JPasswordField) component).getPassword();
                            return CalculMD5.getStringMD5(String.valueOf(input));
                        } else {
                            return "";
                        }
                    case "JCheckBox":
                        if (((JCheckBox) component).isSelected()) {
                            return "T";
                        } else {
                            return "F";
                        }
                    case "JComboBox":
                        return (String) ((JComboBox) component).getSelectedItem();
                    case "Referinta":
                        return String.valueOf(((Referinta) component).getIdSelectat());
                    default:
                        JOptionPane.showMessageDialog(null, "MyPanel.getValoareDinControl - componenta necunoscuta.");
                }
            }
        }
        return "";
    }

    public void setStare(FerDialogTabela.Stare stare, String numeControlFocus) {
        switch (stare) {
            case VIZUALIZARE:
                for (Component component : componente) {
                    switch (component.getClass().getSimpleName()) {
                        case "JTextField":
                            component.setEnabled(false);
                            break;
                        case "JPasswordField":
                            component.setEnabled(false);
                            break;
                        case "JCheckBox":
                            component.setEnabled(false);
                            break;
                        case "JComboBox":
                            component.setEnabled(false);
                            break;
                        case "Referinta":
                            ((Referinta) component).setStare(stare);
                            component.setEnabled(false);
                            break;
                    }
                }
                break;
            case EDITARE:
                for (Component component : componente) {
                    switch (component.getClass().getSimpleName()) {
                        case "JTextField":
                            component.setEnabled(true);
                            break;
                        case "JPasswordField":
                            component.setEnabled(true);
                            //component.requestFocus();
                            break;
                        case "JCheckBox":
                            component.setEnabled(true);
                            break;
                        case "JComboBox":
                            component.setEnabled(true);
                            break;
                        case "Referinta":
                            ((Referinta) component).setStare(stare);
                            component.setEnabled(true);
                            break;
                    }
                }
                setFocus(numeControlFocus);
                break;
        }
    }

    private void setFocus(String numeControl) {
        for (Component component : componente) {
            if (component.getName().equals(numeControl)) {
                component.requestFocus();
            }
        }
    }

}
