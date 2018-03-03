package guistore;

import guistore.backend.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ListIterator;
import java.util.Vector;

public class CustomerWindow {

    //Clasa ce defineste comportamentul ferestrei clientului

    private JPanel panelMain;
    private JButton logoutButton;
    private JLabel labelUserName;
    private JTabbedPane tabbedPane;
    private JButton adaugaInCosButton;
    private JButton adaugaLaWishlistButton;
    private JLabel labelLogo;
    private JLabel labelBudget;
    private JPanel panelToate;
    private JButton notificariButton;
    private JTable tableCart;
    private JTable tableWishlist;
    private JTable tableMagazin;
    private JButton sugereazaItemConformStrategieiButton;
    private JButton stergeProdusulDinCosButton;
    private JButton finalizeazaComandaButton;
    private JButton stergeDinWishlistButton;
    private JLabel crtStartegyLabel;
    private JLabel totalCartLabel;

    private Customer activeCustomer = null;
    private int cartSelectionId = -1;
    private int wishSelectionId = -1;

    public void updateBudgetLabel(){

        DecimalFormat df = Store.getInstance().getPriceFormat();

        labelBudget.setText("Buget disponibil: "+df.format(activeCustomer.getShoppingCart().getRemainingBudget()));

    }

    public CustomerWindow(final Customer customer) {

        this.activeCustomer = customer;

        //initializare elemente de UI
        labelUserName.setText(labelUserName.getText()+activeCustomer.getName());
        labelLogo.setText(Store.getInstance().getName());
        crtStartegyLabel.setText("Strategia ta: "+activeCustomer.getWishList().getStrategy().getStrategyName());
        updateBudgetLabel();
        populateMagazinTable();
        populateWishlistTable();
        populateCartTable();
        stergeProdusulDinCosButton.setEnabled(false);
        stergeDinWishlistButton.setEnabled(false);
        adaugaInCosButton.setEnabled(false);
        adaugaLaWishlistButton.setEnabled(false);

        //listeneri:

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowManager.switchWindow(getPanel(),"login");
            }
        });

        tableMagazin.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if(tableMagazin.getSelectedRow()!=-1) {
                    adaugaInCosButton.setEnabled(true);
                    adaugaLaWishlistButton.setEnabled(true);
                }

            }
        });

        adaugaLaWishlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedItem = (Integer) tableMagazin.getValueAt(tableMagazin.getSelectedRow(), 0);

                if(!wishlistHasItem(selectedItem)){

                    Store.getInstance().assistCustomerWish(activeCustomer,selectedItem);
                    populateWishlistTable();
                    updateBudgetLabel();

                }else {

                    JOptionPane.showMessageDialog(WindowManager.getCrtFrame(),
                            "Produsul " + selectedItem + " a fost deja adaugat",
                            "Eroare adaugare", JOptionPane.ERROR_MESSAGE);

                }

            }
        });

        adaugaInCosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedItem = (Integer) tableMagazin.getValueAt(tableMagazin.getSelectedRow(), 0);

                if (!cartHasItem(selectedItem)) {

                    Store.getInstance().assistCustomerPurchase(activeCustomer, selectedItem);
                    populateCartTable();
                    updateBudgetLabel();

                } else {

                    JOptionPane.showMessageDialog(WindowManager.getCrtFrame(),
                            "Produsul " + selectedItem + " a fost deja adaugat",
                            "Eroare adaugare", JOptionPane.ERROR_MESSAGE);

                }

            }
        });

        tableCart.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if(tableCart.getSelectedRow()!=-1) {
                    cartSelectionId = (int) tableCart.getValueAt(tableCart.getSelectedRow(), 0);
                    stergeProdusulDinCosButton.setEnabled(true);
                }

            }
        });

        stergeProdusulDinCosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(cartSelectionId!=-1){

                    Store.getInstance().assistCustomerRemovePurchase(activeCustomer,cartSelectionId);
                    cartSelectionId = -1;
                    stergeProdusulDinCosButton.setEnabled(false);
                    updateBudgetLabel();
                    populateCartTable();

                }

            }
        });

        finalizeazaComandaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //schimba cosul curent al clientului cu un cos avand bugetul ramas disponibil

                double remainingBudget = activeCustomer.getShoppingCart().getRemainingBudget();
                activeCustomer.setShoppingCart(new ShoppingCart(remainingBudget));
                updateBudgetLabel();
                populateCartTable();

            }
        });

        tableWishlist.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if(tableWishlist.getSelectedRow()!=-1) {

                    wishSelectionId = (int) tableWishlist.getValueAt(tableWishlist.getSelectedRow(), 0);
                    stergeDinWishlistButton.setEnabled(true);

                }

            }
        });

        stergeDinWishlistButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if(wishSelectionId!=-1){

                    Store.getInstance().assistCustomerRemoveWish(activeCustomer,wishSelectionId);
                    wishSelectionId = -1;
                    stergeDinWishlistButton.setEnabled(false);
                    populateWishlistTable();

                }

            }

        });

        sugereazaItemConformStrategieiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Item strategyOutput = activeCustomer.getWishList().executeStrategy();

                if(Store.getInstance().assistCustomerPurchase(activeCustomer,strategyOutput.getId())) {
                    //daca este prea scump ramane in wishlist, altfel, remove
                    Store.getInstance().assistCustomerRemoveWish(activeCustomer, strategyOutput.getId());
                }else {
                    JOptionPane.showMessageDialog(WindowManager.getCrtFrame(),
                            "Produsul " + strategyOutput.getName() + " nu poate fi adaugat in cos! (Verifica buget disponibil)",
                            "StrategieEsuata", JOptionPane.ERROR_MESSAGE);
                }

                wishSelectionId = -1;
                stergeDinWishlistButton.setEnabled(false);
                populateWishlistTable();
                populateCartTable();
                tableWishlist.clearSelection();

                JOptionPane.showMessageDialog(WindowManager.getCrtFrame(),"Produsul "+strategyOutput.getName()+" a fost adaugat in cos",
                        "StrategieExecutata",JOptionPane.INFORMATION_MESSAGE);

            }
        });

        notificariButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //deschide fereastra cu notificarile clientului

                JFrame frame = new JFrame("Notifications");
                frame.setContentPane(new Notifications(frame, activeCustomer).getPanel());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null); //apare in mijlocul ecranului
                frame.setVisible(true);

            }
        });

    }

    private void populateMagazinTable() {

        Vector<Item> items = new Vector<Item>();
        Vector<Department> departments = Store.getInstance().getDepartments();

        for (Department d : departments) {

            ItemList departmentItems = d.getItems();
            ListIterator iter = departmentItems.listIterator();

            while (iter.hasNext()) {
                items.add((Item) iter.next());
            }

        }

        ItemTableModel mdl = new ItemTableModel(items);
        tableMagazin.setModel(mdl);
        tableMagazin.setAutoCreateRowSorter(true);

        adaugaInCosButton.setEnabled(!items.isEmpty());
        adaugaLaWishlistButton.setEnabled(!items.isEmpty());

        TableColumnModel tcm = tableMagazin.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new PriceCellRenderer());

    }

    private void populateWishlistTable() {

        Vector<Item> items = new Vector<Item>();
        WishList wl = activeCustomer.getWishList();
        ListIterator iter = wl.listIterator();

        while (iter.hasNext()) {
            items.add((Item) iter.next());
        }

        ItemTableModel mdl = new ItemTableModel(items);
        tableWishlist.setModel(mdl);
        tableWishlist.setAutoCreateRowSorter(true);

        sugereazaItemConformStrategieiButton.setEnabled(!items.isEmpty());

        TableColumnModel tcm = tableWishlist.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new PriceCellRenderer());

    }

    private void populateCartTable() {

        Vector<Item> items = new Vector<Item>();
        ShoppingCart sc = activeCustomer.getShoppingCart();
        ListIterator iter = sc.listIterator();

        while (iter.hasNext()) {
            items.add((Item) iter.next());
        }

        ItemTableModel mdl = new ItemTableModel(items);
        tableCart.setModel(mdl);
        tableCart.setAutoCreateRowSorter(true);

        finalizeazaComandaButton.setEnabled(!items.isEmpty());

        totalCartLabel.setText("Total: "+Store.getInstance().getPriceFormat().format(activeCustomer.getShoppingCart().getTotalPrice()));

        TableColumnModel tcm = tableCart.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new PriceCellRenderer());

    }

    private boolean cartHasItem(int querryItemId){

        //cauta produs dupa id in cosul clientului

        ShoppingCart sc = activeCustomer.getShoppingCart();
        ListIterator li = sc.listIterator();
        Item i;

        while (li.hasNext()){

            i = (Item) li.next();

            if(i.getId()==querryItemId){
                return true;
            }

        }

        return false;

    }

    private boolean wishlistHasItem(int querryItemId){

        WishList wl = activeCustomer.getWishList();
        ListIterator li = wl.listIterator();
        Item i;

        while (li.hasNext()){

            i = (Item) li.next();

            if(i.getId()==querryItemId){
                return true;
            }

        }

        return false;

    }

    public JPanel getPanel() {
        //returneaza panoul principal folosit de window manager pentru a afisa fereastra
        return panelMain;
    }

}
