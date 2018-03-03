package guistore;

import guistore.backend.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ListIterator;
import java.util.Vector;

public class AdminWindow {

    //Clasa ce defineste comportamentul ferestrei de administrare

    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JList listClienti;
    private JLabel labelWelcome;
    private JButton removeButton;
    private JButton adaugaButton;
    private JButton modificaButton;
    private JButton buttonLogout;
    private JTable tableProduse;
    private JScrollPane scrollPaneProduse;
    private JTextField textFieldID;
    private JTextField textFieldDep;
    private JTextField textFieldIDDEP;
    private JTextField textFieldNumeProd;
    private JTextField textFieldPretProd;
    private JButton buttonCancel;
    private JLabel labelStatActiveCustomers;
    private JLabel labelStatMostBought;
    private JLabel labelStatMostWished;
    private JLabel labelStatActiveDepartments;
    private JLabel labelStatMostExpensive;
    private JButton buttonRefreshStats;
    private JCheckBox clientiActiviCheckBox;
    private JLabel logoLabel;
    private JTable tableWishlist;
    private JTable tableCart;
    private JComboBox comboBoxDepartments;
    private JButton aplicaReducereButton;

    private Store store;
    private int selectedItemId = -1;
    private int selectedDepartmentId = -1;
    private boolean pendingOperation = false;

    private Color defaultTextBg;

    public AdminWindow() {

        //Retine culoarea initiala a backgroundului pentru a reveni la aceasta
        defaultTextBg = textFieldID.getBackground();

        store = Store.getInstance();

        //initializare panou statistici
        refreshStats();

        //stare initiala butoane
        modificaButton.setEnabled(false);
        removeButton.setEnabled(false);
        adaugaButton.setEnabled(true);
        buttonCancel.setVisible(false);

        logoLabel.setText("Administrare "+Store.getInstance().getName());

        //adauga elementele in listele de clienti/produse
        populateProductTable();
        populateCustomerList("all");
        populateDepartmentsComboBox();

        //listeneri:

        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowManager.switchWindow(getPanel(),"login");
            }
        });

        tableProduse.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            @Override
            public void valueChanged(ListSelectionEvent event) {

                if(tableProduse.getSelectedRow()!=-1) {

                    //daca a fost selectat un produs din tabel:

                    selectedItemId = (int) tableProduse.getValueAt(tableProduse.getSelectedRow(), 0);
                    selectedDepartmentId = store.getProductDepartment(selectedItemId);

                    Item i = store.getDepartment(selectedDepartmentId).getItem(selectedItemId);

                    //umple textfield-urile cu datele produsului selectat
                    populateDetails(i);

                    modificaButton.setEnabled(true);
                    removeButton.setEnabled(true);

                }

            }
        });

        adaugaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!pendingOperation) {

                    //daca nu este o operatie de adaugare in curs

                    pendingOperation = true;

                    clearDetails();
                    adaugaButton.setText("Confirma");
                    modificaButton.setEnabled(false);
                    removeButton.setEnabled(false);

                    highlightRequired();

                }else {

                    //daca nu este o operatie de adaugare in curs
                    //verifica daca datele introduse sunt valide

                    if (checkProductInputForErrors()) {

                        //verifica daca se poate adauga prosusul in magazin
                        if(!store.addProduct(Integer.parseInt(textFieldIDDEP.getText()),
                                            Integer.parseInt(textFieldID.getText()),
                                            Double.parseDouble(textFieldPretProd.getText()),
                                            textFieldNumeProd.getText())){

                            JOptionPane.showMessageDialog(WindowManager.getCrtFrame(), "Produsul "
                                            +textFieldID.getText()+" exista deja!",
                                            "EroareAdaugare", JOptionPane.WARNING_MESSAGE);

                        }

                        //reimprospateaza tabelul cu produse
                        populateProductTable();

                    } else {

                        JOptionPane.showMessageDialog(WindowManager.getCrtFrame(), "Ati introdus gresit datele, incercati din nou!",
                                "EroareAdaugare", JOptionPane.WARNING_MESSAGE);

                    }

                    //reseteaza elementele la starea initiala
                    cancelOperation();

                }
            }
        });

        modificaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!pendingOperation) {

                    //daca nu este deja in curs o operatie de modificare

                    pendingOperation = true;

                    modificaButton.setText("Confirma");
                    adaugaButton.setEnabled(false);
                    removeButton.setEnabled(false);

                    highlightPrice();

                } else {

                    //daca este deja in curs o operatie de modificare
                    //verifica corectitudinea datelor introduse

                    if (checkProductInputForErrors()) {

                        if (selectedItemId != -1 && selectedDepartmentId != -1) {
                            store.modifyProduct(selectedDepartmentId, selectedItemId, Double.parseDouble(textFieldPretProd.getText()));
                            populateProductTable();
                        }

                    } else {

                        JOptionPane.showMessageDialog(WindowManager.getCrtFrame(), "Ati introdus gresit datele, incercati din nou!",
                                "EroareModificare", JOptionPane.WARNING_MESSAGE);

                    }

                    //reseteaza elementele la starea initiala
                    cancelOperation();

                }
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(tableProduse.getSelectedRow()!=-1){

                    //daca este un produs din tabel selectat

                    //cere confirmarea pentru stergere
                    int retval = JOptionPane.showConfirmDialog(WindowManager.getCrtFrame(),
                            "Stergeti produsul "+selectedItemId+" ?",
                            "Confirmare", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);

                    //daca raspunsul este afirmativ
                    if(retval == JOptionPane.YES_OPTION){

                        store.removeProduct((Integer) tableProduse.getValueAt(tableProduse.getSelectedRow(), 0));//lastClickedItemId);
                        populateProductTable();

                    }

                    //reseteaza elementele la starea initiala
                    cancelOperation();

                }else{

                    JOptionPane.showMessageDialog(WindowManager.getCrtFrame(),"Nu ati selectat un produs!",
                            "EroareStergere",JOptionPane.WARNING_MESSAGE);

                }

            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //reseteaza elementele la starea initiala
                cancelOperation();
            }
        });

        buttonRefreshStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //reimprospateaza pagina de statistici
                refreshStats();
            }
        });

        clientiActiviCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if(e.getStateChange() == ItemEvent.SELECTED){

                    listClienti.clearSelection();
                    populateCustomerList("active");

                }
                if(e.getStateChange() == ItemEvent.DESELECTED){

                    listClienti.clearSelection();
                    populateCustomerList("all");

                }

            }
        });

        listClienti.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if(listClienti.getSelectedIndex()!=-1) {

                    //daca a fost selectat un client actualizeaza panoul din dreapta cu continutul cosului sau

                    populateShoppingCart((String) listClienti.getSelectedValue());
                    populateWishList((String) listClienti.getSelectedValue());

                }

            }
        });

        aplicaReducereButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(listClienti.getSelectedIndex()!=-1) {

                    Customer crtCustomer = store.getCustomerByName((String) listClienti.getSelectedValue());
                    Department crtDepartment = (Department) comboBoxDepartments.getSelectedItem();

                    //aplica reducerea
                    crtDepartment.accept(crtCustomer.getShoppingCart());

                    //actualizeaza cosul cu noul pret
                    populateWishList(crtCustomer.getName());
                    populateShoppingCart(crtCustomer.getName());

                }

            }
        });
    }

    public JPanel getPanel() {
        //returneaza panoul principal folosit de window manager pentru a afisa fereastra
        return panel1;
    }

    private void populateProductTable(){

        //extrage produsele din fiecare departament si le adauga la tabel

        Vector<Item> items = new Vector<Item>();

        Vector<Department> departments = store.getDepartments();

        for(Department d : departments){

            ItemList departmentItems = d.getItems();
            ListIterator iter = departmentItems.listIterator();

            while(iter.hasNext()){
                items.add((Item) iter.next());
            }

        }

        ItemTableModel mdl = new ItemTableModel(items);
        tableProduse.setModel(mdl);
        tableProduse.setAutoCreateRowSorter(true);

        TableColumnModel tcm = tableProduse.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new PriceCellRenderer());

    }

    private void populateDetails(Item item){

        modificaButton.setEnabled(true);
        removeButton.setEnabled(true);

        textFieldID.setText(Integer.toString(item.getId()));
        textFieldDep.setText(item.getParentDepartment().getDepartmentName());
        textFieldIDDEP.setText(Integer.toString(selectedDepartmentId));
        textFieldNumeProd.setText(item.getName());
        textFieldPretProd.setText(Double.toString(item.getPrice()));

    }

    private void clearDetails(){

        selectedItemId = -1;
        selectedDepartmentId = -1;

        textFieldID.setText("");
        textFieldDep.setText("");
        textFieldIDDEP.setText("");
        textFieldNumeProd.setText("");
        textFieldPretProd.setText("");

    }

    private void highlightRequired(){

        textFieldID.setBackground(Color.ORANGE);
        textFieldDep.setEnabled(false);
        textFieldIDDEP.setBackground(Color.ORANGE);
        textFieldPretProd.setBackground(Color.ORANGE);
        textFieldNumeProd.setBackground(Color.ORANGE);

        textFieldID.setEditable(true);
        textFieldDep.setEditable(true);
        textFieldIDDEP.setEditable(true);
        textFieldPretProd.setEditable(true);
        textFieldNumeProd.setEditable(true);

        buttonCancel.setVisible(true);
        buttonCancel.setEnabled(true);

    }

    private void highlightPrice(){

        textFieldPretProd.setBackground(Color.ORANGE);
        textFieldPretProd.setEditable(true);
        buttonCancel.setVisible(true);
        buttonCancel.setEnabled(true);

    }

    private void undoHighlightRequired(){

        textFieldID.setBackground(defaultTextBg);
        textFieldDep.setEnabled(true);
        textFieldIDDEP.setBackground(defaultTextBg);
        textFieldPretProd.setBackground(defaultTextBg);
        textFieldNumeProd.setBackground(defaultTextBg);

        if(textFieldID.isEnabled()) {
            textFieldID.setEditable(false);
        }
        textFieldDep.setEditable(false);
        textFieldIDDEP.setEditable(false);
        textFieldPretProd.setEditable(false);
        textFieldNumeProd.setEditable(false);

    }

    private void cancelOperation(){

        //reseteaza toate elementele de UI la starea lor initiala

        tableProduse.clearSelection();
        modificaButton.setEnabled(false);
        removeButton.setEnabled(false);
        adaugaButton.setEnabled(true);
        pendingOperation = false;
        adaugaButton.setText("Adauga");
        modificaButton.setText("Modifica");
        clearDetails();
        undoHighlightRequired();

        buttonCancel.setVisible(false);
        buttonCancel.setEnabled(false);

    }

    private boolean checkProductInputForErrors(){

        //verifica daca au fost introduse date valide

        if(!textFieldID.getText().matches("[0-9]+")){
            return false;
        }

        if(!textFieldIDDEP.getText().matches("[0-9]+")){
            return false;
        }

        if(textFieldPretProd.getText().isEmpty()){
            return false;
        }

        if(textFieldNumeProd.getText().isEmpty()){
            return false;
        }

        try {
            Double.parseDouble(textFieldPretProd.getText());
            Integer.parseInt(textFieldIDDEP.getText());
            Integer.parseInt(textFieldID.getText());
        }catch(NumberFormatException e) {
            return false;
        }

        return true;

    }

    private void refreshStats(){

        //actualizeaza label-urile din pagina de statistici

        labelWelcome.setText("Bun venit in pagina de administrare "+store.getName()+" !");
        labelStatActiveCustomers.setText("Clienti activi: " + this.getActiveCustomers().size());
        labelStatActiveDepartments.setText("Departamente active: "+ this.activeDepartmentsNumber());
        labelStatMostBought.setText("Cel mai cumparat produs: " + this.mostBought());
        labelStatMostWished.setText("Cel mai dorit produs: " + this.mostWished());
        labelStatMostExpensive.setText("Cel mai scump produs: " + this.mostExpensive());

    }

    private int activeDepartmentsNumber(){

        Vector<Department> departments = store.getDepartments();

        int retval = 0;

        for(Department d : departments){
            if(!d.getCustomers().isEmpty()||!d.getObservers().isEmpty()){
                retval++;
            }
        }

        return retval;

    }

    private Vector<Customer> getActiveCustomers(){

        //returneaza clientii cu cel putin un item in wishlist sau cart

        Vector<Department> departments = store.getDepartments();
        Vector<Customer> activeCustomers = new Vector<Customer>();
        Vector<Customer> crtDepartmentCustomers;

        for(Department d : departments){
            crtDepartmentCustomers = d.getCustomers();
            if(!crtDepartmentCustomers.isEmpty()){
                for(Customer c: crtDepartmentCustomers){
                    if(!activeCustomers.contains(c)){
                        activeCustomers.add(c);
                    }
                }
            }
            crtDepartmentCustomers = d.getObservers();
            if(!crtDepartmentCustomers.isEmpty()){
                for(Customer c: crtDepartmentCustomers){
                    if(!activeCustomers.contains(c)){
                        activeCustomers.add(c);
                    }
                }
            }
        }

        return activeCustomers;

    }

    private boolean vectorContainsItemId(Vector<Item> vector, int itemId){

        for(Item i:vector){
            if(i.getId() == itemId){
                return true;
            }
        }

        return false;

    }

    private Vector<Item> listToVector(ItemList il){

        ListIterator iter = il.listIterator();
        Vector<Item> ret = new Vector<Item>();

        while(iter.hasNext()){
            ret.add((Item)iter.next());
        }

        return ret;

    }

    private String mostBought(){

        //returneaza detaliile celui mai cumparat produs

        Vector<Customer> activeCustomers = getActiveCustomers();
        Vector<Item> boughtItems = new Vector<Item>();
        Vector<Item> temp;

        int buyerNumber = -1;
        Item mostBought = null;

        ShoppingCart crtCart;
        ListIterator crtIter;
        Item crtItem;

        for(Customer c : activeCustomers){

            crtCart = c.getShoppingCart();
            crtIter = crtCart.listIterator();

            while(crtIter.hasNext()){
                crtItem = (Item) crtIter.next();

                if(!vectorContainsItemId(boughtItems, crtItem.getId())){
                    boughtItems.add(crtItem);
                }

            }

        }

        for(Item i: boughtItems){
            int crtItemBuyers = 0;
            for(Customer c : activeCustomers){
                crtCart = c.getShoppingCart();
                temp = listToVector(crtCart);
                if(vectorContainsItemId(temp,i.getId())){
                    crtItemBuyers++;
                }
            }
            if(crtItemBuyers>buyerNumber){
                mostBought = i;
                buyerNumber = crtItemBuyers;
            }
        }

        if(buyerNumber == -1){
            return "";
        }else {
            return buyerNumber + " x " + mostBought.getName() + " - " + mostBought.getPrice()+ " - " + mostBought.getParentDepartment();
        }

    }

    private String mostWished(){

        //returneaza detaliile celui mai dorit produs

        Vector<Customer> activeCustomers = getActiveCustomers();
        Vector<Item> wishedItems = new Vector<Item>();
        Vector<Item> temp;

        int wisherNumber = -1;
        Item mostWished = null;

        WishList crtWishList;
        ListIterator crtIter;
        Item crtItem;

        for(Customer c : activeCustomers){

            crtWishList = c.getWishList();
            crtIter = crtWishList.listIterator();

            while(crtIter.hasNext()){

                crtItem = (Item) crtIter.next();

                if(!vectorContainsItemId(wishedItems, crtItem.getId())){
                    wishedItems.add(crtItem);
                }

            }

        }

        for(Item i: wishedItems){

            int crtItemWishers = 0;

            for(Customer c : activeCustomers){
                crtWishList = c.getWishList();
                temp = listToVector(crtWishList);

                if(vectorContainsItemId(temp,i.getId())){
                    crtItemWishers++;
                }

            }

            if(crtItemWishers>wisherNumber){

                mostWished = i;
                wisherNumber = crtItemWishers;

            }

        }

        if(wisherNumber == -1){
            return "";
        }else {
            return wisherNumber + " x " + mostWished.getName() + " - " + mostWished.getPrice() + " - " + mostWished.getParentDepartment();
        }

    }

    private String mostExpensive(){

        //returneaza detaliile celui mai scump produs

        double maxPrice = -1.0;
        Item mostExpensive = null;

        Vector<Item> items = new Vector<Item>();
        Vector<Department> departments = store.getDepartments();

        for(Department d : departments){

            ItemList departmentItems = d.getItems();
            ListIterator iter = departmentItems.listIterator();

            while(iter.hasNext()){
                items.add((Item) iter.next());
            }

        }

        for(Item i:items){
            if(i.getPrice()>maxPrice){
                maxPrice = i.getPrice();
                mostExpensive = i;
            }
        }

        if(maxPrice == -1.0){
            return "";
        }else{
            return mostExpensive.getName() + " - " + mostExpensive.getPrice()+ " - " + mostExpensive.getParentDepartment();
        }

    }

    private void populateCustomerList(String type){

        //adauga clientii activi/toti clientii in lista

        DefaultListModel<String> lm = new DefaultListModel<>();

        Vector<Customer> customers = Store.getInstance().getCustomers();

        switch (type){
            case "all":
                for(Customer c: customers){
                    lm.addElement(c.toString());
                }
                break;
            case "active":
                for(Customer c: customers){
                    if(!c.getShoppingCart().isEmpty() || !c.getWishList().isEmpty()) {
                        lm.addElement(c.toString());
                    }
                }
                break;
        }

        listClienti.setModel(lm);

    }

    private void populateShoppingCart(String customerName){

        Customer selectedCustomer = Store.getInstance().getCustomerByName(customerName);

        Vector<Item> items = new Vector<Item>();

        ShoppingCart sc = selectedCustomer.getShoppingCart();
        ListIterator iter = sc.listIterator();

        while (iter.hasNext()) {
            items.add((Item) iter.next());
        }

        ItemTableModel mdl = new ItemTableModel(items);
        tableCart.setModel(mdl);
        tableCart.setAutoCreateRowSorter(true);

        TableColumnModel tcm = tableCart.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new PriceCellRenderer());

    }

    private void populateWishList(String customerName){

        Customer selectedCustomer = Store.getInstance().getCustomerByName(customerName);

        Vector<Item> items = new Vector<Item>();

        WishList wl = selectedCustomer.getWishList();
        ListIterator iter = wl.listIterator();

        while (iter.hasNext()) {
            items.add((Item) iter.next());
        }

        ItemTableModel mdl = new ItemTableModel(items);
        tableWishlist.setModel(mdl);
        tableWishlist.setAutoCreateRowSorter(true);

        TableColumnModel tcm = tableWishlist.getColumnModel();
        tcm.getColumn(3).setCellRenderer(new PriceCellRenderer());

    }

    private void populateDepartmentsComboBox(){

        Vector<Department> departments = store.getDepartments();

        for(Department d : departments){
            comboBoxDepartments.addItem(d);
        }

    }

}
