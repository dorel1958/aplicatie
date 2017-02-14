package dorel.aplicatie.listeners;

import dorel.aplicatie.ferestre.FerDialogTabela;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MyPropertyChangeListener implements PropertyChangeListener {

    // Neutilizat
    FerDialogTabela ferDialogTabela;

    public MyPropertyChangeListener(FerDialogTabela ferDialogTabela) {
        this.ferDialogTabela = ferDialogTabela;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        //ferDialogTabela.setStare(FerDialogTabela.Stare.EDITARE);
        System.out.println("MyPropertyChangeListener.propertyChange - PropertyName:" + evt.getPropertyName() + " numeClasa:" + evt.getSource().getClass().getSimpleName() + " new value:" + evt.getNewValue());
    }

}
