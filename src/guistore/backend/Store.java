package guistore.backend;

import java.text.DecimalFormat;
import java.util.ListIterator;
import java.util.Vector;

public class Store {

    private static Store instance = null;

    private DecimalFormat df;

    private String name;
    private Vector<Customer> customers;
    private Vector<Department> departments;

    private Store(){
        customers = new Vector<>();
        departments = new Vector<>();
        df = new DecimalFormat("0.00");
    }

    public static Store getInstance(){
        if(instance == null){
            instance = new Store();
        }
        return instance;
    }

    public void enter(Customer c){
        customers.add(c);
    }

    public void exit(Customer c){

        if(!customers.contains(c)){
            return;
        }

        ShoppingCart sc = c.getShoppingCart();
        ListIterator iter = sc.listIterator();
        Item i;

        while (iter.hasNext()){
            i = (Item)iter.next();
            i.getParentDepartment().exit(c);
        }

        customers.remove(c);
    }

    public ShoppingCart getShoppingCart(Double budget){
        return new ShoppingCart(budget);
    }

    public Vector<Customer> getCustomers() {
        return customers;
    }

    public Vector<Department> getDepartments(){
        return departments;
    }

    public void addDepartment(Department d){
        departments.add(d);
    }

    public Department getDepartment(int departmentID){
        for(Department d: departments){
            if(d.getId() == departmentID){
                return d;
            }
        }
        return null; //daca nu a fost gasit
    }

    public int getProductDepartment(int productID){
        //returneaza id-ul departamentului ce contine productID
        //sau -1 in cazul in care produsul nu a fost gasit

        for(Department d : this.getDepartments()){
            if(d.getItem(productID)!=null){
                return d.getId();
            }
        }

        return -1;

    }

    public boolean assistCustomerPurchase(Customer c, int productID){

        if(!customers.contains(c)){
            return false;
        }

        int chosenDepartmentID = getProductDepartment(productID);

        Department chosenDepartment;

        if(chosenDepartmentID != -1){
            chosenDepartment = this.getDepartment(chosenDepartmentID);
        }else {
            return false;
        }

        if(!chosenDepartment.handleCustomerPurchase(c,productID)){
            return false;
        }

        return true;

    }

    public boolean assistCustomerWish(Customer c, int productID){

        if(!customers.contains(c)){
            return false;
        }

        int chosenDepartmentID = getProductDepartment(productID);

        Department chosenDepartment;

        if(chosenDepartmentID != -1){
            chosenDepartment = this.getDepartment(chosenDepartmentID);
        }else {
            return false;
        }

        if(!chosenDepartment.handleCustomerWish(c,productID)){
            return false;
        }

        return true;

    }

    public boolean assistCustomerRemovePurchase(Customer c, int productID){

        if(!customers.contains(c)){
            return false;
        }

        int chosenDepartmentID = getProductDepartment(productID);

        Department chosenDepartment;

        if(chosenDepartmentID != -1){
            chosenDepartment = this.getDepartment(chosenDepartmentID);
        }else {
            return false;
        }

        if(!chosenDepartment.handleCustomerRemovePurchase(c,productID)){
            return false;
        }

        return true;

    }

    public boolean assistCustomerRemoveWish(Customer c, int productID){

        if(!customers.contains(c)){
            return false;
        }

        int chosenDepartmentID = getProductDepartment(productID);

        Department chosenDepartment = null;

        if(chosenDepartmentID != -1){
            chosenDepartment = this.getDepartment(chosenDepartmentID);
        }else {
            return false;
        }

        if(!chosenDepartment.handleCustomerRemoveWish(c,productID)){
            return false;
        }

        return true;

    }

    public boolean addProduct(int departmentID, int itemID, double price, String name){

        Department crtDepartment = this.getDepartment(departmentID);

        if(crtDepartment == null){
            return false;
        }

        return crtDepartment.addItem(new Item(name,itemID,price));

    }

    public boolean modifyProduct(int departmentID, int itemID, double price){

        Department crtDepartment = this.getDepartment(departmentID);

        if(crtDepartment == null){
            return false;
        }

        return crtDepartment.setItemPrice(itemID,price);

    }

    public boolean removeProduct(int itemId){

        int crtDepartmentId = this.getProductDepartment(itemId);

        if(crtDepartmentId == -1) {
            return false;
        }

        Department crtDepartment = this.getDepartment(crtDepartmentId);

        if(crtDepartment == null){
            return false;
        }

        return crtDepartment.removeItem(itemId);

    }

    public Department getDepartmentById(int departmentId){
        for(Department d : departments){
            if(d.getId() == departmentId){
                return d;
            }
        }
        return null;
    }

    public Customer getCustomerByName(String customerName){
        for(Customer c : customers){
            if(c.getName().equals(customerName)){
                return c;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DecimalFormat getPriceFormat() {
        return df;
    }


}
