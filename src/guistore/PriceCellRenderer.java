package guistore;

import guistore.backend.Store;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class PriceCellRenderer implements TableCellRenderer {

    //Vom randa separat celulele ce contin preturi, pentru a seta preciazia afisarii la 2 zecimale

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        //extragem componenta default
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        Component defaultComponent = dtcr.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);

        //modificam campurile care ne intreseaza
        ((JLabel)defaultComponent).setText(Store.getInstance().getPriceFormat().format(value));
        ((JLabel)defaultComponent).setHorizontalAlignment(SwingConstants.RIGHT);

        //returnam componenta modificata
        return defaultComponent;

    }
}
