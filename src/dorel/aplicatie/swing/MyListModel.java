package dorel.aplicatie.swing;

import dorel.aplicatie.interfaces.CommonInterface;
import dorel.aplicatie.interfaces.TabelaSqlInterface;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class MyListModel implements ListModel {

    List<ListDataListener> listeners;
    //
    CommonInterface common;
    TabelaSqlInterface tabela;
    List<TabelaSqlInterface> listaElemente;

    public MyListModel(CommonInterface common, TabelaSqlInterface tabela) {
        this.common = common;
        this.tabela = tabela;
        listaElemente = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public final int refresh(String filter) {
        int id = tabela.getId();
        int raspuns = 0;
        int contor = 0;
        this.clear();
        List<TabelaSqlInterface> lista = common.getDataSource().listInreg(tabela.getNumeTabela(), 0, tabela.getOrderBy(), filter);
        if (lista.isEmpty()) {
            // la lista goala indexSelectat=-1
            raspuns = -1;
        } else {
            for (TabelaSqlInterface xtabela : lista) {
                if (xtabela.getId() == id) {
                    raspuns = contor;
                }
                this.addElement(xtabela);
                contor++;
            }
        }
        notifyListeners();
        return raspuns;
    }

    public void addElement(TabelaSqlInterface tabelaSql) {
        listaElemente.add(tabelaSql);
        notifyListeners();
    }

    public void removeElement(int index) {
        listaElemente.remove(index);
        notifyListeners();
    }

    public void clear() {
        listaElemente.clear();
        notifyListeners();
    }

    public TabelaSqlInterface get(int index) {
        if (index > -1) {
            return listaElemente.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getSize() {
        return listaElemente.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (index >= 0) {
            return listaElemente.get(index);
        } else {
            return null;
        }
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }

    void notifyListeners() {
        // no attempt at optimziation
        ListDataEvent le = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize());
        for (ListDataListener listener : listeners) {
            listener.contentsChanged(le);
        }
//        listeners.stream().forEach(listener -> {
//            listener.contentsChanged(le);
//        });
    }

}
