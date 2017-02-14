package dorel.aplicatie.swing;

import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class MyComboBoxModel implements ComboBoxModel {

    private int selectedIndex;
    private final List<Object> data;

    public MyComboBoxModel(List data) {
        this.data = data;
    }

    public void setSelectedIndex(int indexul) {
        this.selectedIndex = indexul;
    }

    public void addItem(Object obj) {
        data.add(obj);
    }

    // proprii
    @Override
    public void setSelectedItem(Object anItem) {
        selectedIndex = data.indexOf(anItem);
    }

    @Override
    public Object getSelectedItem() {
        return data.get(selectedIndex);
    }

    // din ListModel
    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Object getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
