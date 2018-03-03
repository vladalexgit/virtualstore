package guistore;

import guistore.backend.Item;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

public class ItemTableModel extends AbstractTableModel {

    //Model custom pentru a reprezenta un tabel de Item

    private Vector<Item> items;

    public ItemTableModel(Vector<Item> items) {
        this.items = items;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Item i = items.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return i.getId();
            case 1:
                return i.getParentDepartment().getDepartmentName();
            case 2:
                return i.getName();
            case 3:
                return i.getPrice();
        }

        return null;

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {

        //returneaza clasa corespunzatoare, astfel numerele vor fi sortate diferit fata de stringuri, etc.

        if(items.isEmpty()) {
            return String.class;
        }

        return this.getValueAt(0, columnIndex).getClass();

    }

    @Override
    public String getColumnName(int columnIndex) {

        switch (columnIndex) {
            case 0:
                return "ID";
            case 1:
                return "Departament";
            case 2:
                return "Nume";
            case 3:
                return "Pret";
        }

        return "Default";

    }

    public void removeRow(int row) {
        items.removeElementAt(row);
    }

}
