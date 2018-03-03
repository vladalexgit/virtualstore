package guistore.backend;

import java.util.ListIterator;
import java.util.Vector;

public abstract class Department implements Subject{

    private String departmentName;
    private ItemStock availableItems;
    private Vector<Customer> buyingCustomers;
    private Vector<Customer> wishingCustomers;
    private int departmentID;

    public Department(String departmentName, int departmentID) {

        this.departmentName = departmentName;
        this.departmentID = departmentID;
        this.buyingCustomers =  new Vector<>();
        this.wishingCustomers = new Vector<>();
        this.availableItems = new ItemStock();

    }

    public void enter(Customer customer){
        buyingCustomers.add(customer);
    }

    public void exit(Customer customer){
        buyingCustomers.remove(customer);
    }

    public Vector<Customer> getCustomers(){
        return buyingCustomers;
    }

    public int getId(){
        return departmentID;
    }

    public boolean addItem(Item item){

        //returneaza false daca produsul deja exista

        if(this.getItem(item.getId()) == null) {
            item.setParentDepartment(this);
            availableItems.add(item);
            notifyAllObservers(new Notification(Notification.NotificationType.ADD, this.getId(), item.getId()));
            return true;
        }

        System.out.println("Produsul exista deja!");

        return false;

    }

    public boolean removeItem(Item item) {

        if (item != null) {
            availableItems.remove(item);
            notifyAllObservers(new Notification(Notification.NotificationType.REMOVE, this.getId(), item.getId()));
            updateAllCartsRemove(item);
            return true;
        }

        System.out.println("Produsul pentru care se cere stergerea nu exista");

        return false;

    }

    public boolean removeItem(int itemID){

        Item i = getItem(itemID);
        return this.removeItem(i);

    }

    public boolean setItemPrice(int itemID, double price){

        Item i = getItem(itemID);

        if(i!=null){

            i.setPrice(price);
            notifyAllObservers(new Notification(Notification.NotificationType.MODIFY,this.getId(),i.getId()));
            updateAllCartsModify(i);

            return true;

        }

        return false;

    }

    public Item getItem(int itemID){

        //returneaza null daca nu s-a gasit item-ul

        ListIterator iter = availableItems.listIterator();
        Item i = null;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getId() == itemID) {
                break;
            }

            i = null;

        }

        return i;

    }

    public ItemList getItems(){
        return availableItems;
    }

    public Vector<Customer> getObservers() {
        return wishingCustomers;
    }

    @Override
    public void addObserver(Customer customer) {
        //un client a adaugat la wishlist un item din departament
        if(!wishingCustomers.contains(customer)) {
            wishingCustomers.add(customer);
        }
    }
    @Override
    public void removeObserver(Customer customer) {
        //un client a scos din wishlist toate produsele din acest departament
        wishingCustomers.remove(customer);
    }
    @Override
    public void notifyAllObservers(Notification notification){
        //anunta clientii ca a fost modificat/eliminat un produs din departament
        Vector<Customer> notificationMembers = new Vector<>();
        notificationMembers.addAll(wishingCustomers);

        for(Customer c: notificationMembers){
            c.update(notification);
        }
    }

    public void updateAllCartsRemove(Item item){
        //sterge produsul din toate cosurile de cumparaturi
        for(Customer c: buyingCustomers) {
            c.giveBackCartItem(item);
        }
    }

    public void updateAllCartsModify(Item item){
        //inlocuieste produsul in toate cosurile de cumparaturi
        for(Customer c: buyingCustomers) {
            c.replaceCartItem(item);
        }
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public boolean handleCustomerPurchase(Customer c, int productID){

        //ajuta clientul c sa cumpere produsul

        Item i = getItem(productID);

        if(i!=null) {
            this.enter(c);
            c.getShoppingCart().add(new Item(i));
            return true;
        }

        return false;

    }

    public boolean handleCustomerWish(Customer c, int productID){

        //ajuta clientul c sa adauge la wishlist produsul

        Item i = getItem(productID);

        if(i!=null) {
            this.addObserver(c);
            c.getWishList().add(new Item(i));
            return true;
        }

        return false;

    }

    public boolean handleCustomerRemovePurchase(Customer c, int productID){

        //ajuta clientul c sa stearga din cos produsul

        ShoppingCart cart = c.getShoppingCart();

        ListIterator iter = cart.listIterator();
        Item i;
        boolean removeSuccess = false;

        while(iter.hasNext()){
            i = (Item)iter.next();

            if(i.getId() == productID){
                removeSuccess = cart.remove(i);
                break;
            }

        }

        if(!removeSuccess){
            return false;
        }

        iter = cart.listIterator();
        boolean hasOtherProducts = false;

        //verifica daca mai are produse din acest departament

        while(iter.hasNext()){
            i = (Item)iter.next();

            if(i.getParentDepartment() == this){
                hasOtherProducts = true;
                break;
            }

        }

        if(!hasOtherProducts){
            this.exit(c);
        }

        return true;

    }

    public boolean handleCustomerRemoveWish(Customer c, int productID){

        //cauta si sterge produsul din wishlistul clientului c

        WishList wl = c.getWishList();

        ListIterator iter = wl.listIterator();
        Item i;
        boolean removeSuccess = false;

        while(iter.hasNext()){
            i = (Item)iter.next();

            if(i.getId() == productID){
                removeSuccess = wl.remove(i);
                break;
            }

        }

        if(!removeSuccess){
            return false;
        }

        iter = wl.listIterator();
        boolean hasOtherProducts = false;

        //verifica daca mai are produse din acest departament

        while(iter.hasNext()){
            i = (Item)iter.next();

            if(i.getParentDepartment() == this){
                hasOtherProducts = true;
                break;
            }

        }

        if(!hasOtherProducts){
            this.removeObserver(c);
        }

        return true;

    }

    //metoda pentru aplicarea reducerilor, va fi implementata separat de catre fiecare departament
    public abstract void accept(ShoppingCart shoppingCart);

    @Override
    public String toString() {
        return this.departmentName;
    }

}
