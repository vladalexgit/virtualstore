package guistore.backend;

import java.util.ListIterator;
import java.util.Vector;

public class Customer implements Observer{

    private String name;
    private ShoppingCart shoppingCart;
    private WishList wishList;
    private Vector<Notification> notifications;

    public Customer(String name) {
        this.name = name;
        this.notifications = new Vector<>();
    }

    @Override
    public void update(Notification notification) {

        //a fost primita o notificare:

        notifications.add(notification);

        if(notification.getType().equals(Notification.NotificationType.REMOVE)){
            handleDeleteNotification(notification);
        }else if(notification.getType().equals(Notification.NotificationType.MODIFY)){
            handleModifyNotification(notification);
        }

    }

    public void giveBackCartItem(Item item){

        //elimina produsul din shopping cart

        ListIterator iter = shoppingCart.listIterator();
        Item i;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getId() == item.getId() && i.getParentDepartment()== item.getParentDepartment()){

                shoppingCart.remove(i);

            }

        }

    }

    public void replaceCartItem(Item item){

        ListIterator iter = shoppingCart.listIterator();
        Item i;

        //modifica produsul din shopping cart pentru a fi conform cu noile specificatii
        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getId() == item.getId() && i.getParentDepartment()== item.getParentDepartment()){

                shoppingCart.remove(i);
                if(!shoppingCart.add(item)){
                    System.out.println("^ " + this.name + " can no longer buy item: " + i.getId());
                }

            }

        }

    }

    public void handleDeleteNotification(Notification notification){
        //elimina produsul din wishlist

        ListIterator iter = wishList.listIterator();
        Item i;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getId() == notification.getProductID() &&
                    i.getParentDepartment().getId() == notification.getDepartmentID()){
                //trebuie sa facem remove-ul prin department pentru ca acesta sa ne elimine din lista sa de observatori
                i.getParentDepartment().handleCustomerRemoveWish(this,i.getId());
            }
        }

    }

    private void handleModifyNotification(Notification notification) {

        //updateaza produsul din wishlist

        ListIterator iter = wishList.listIterator();
        Item i;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getId() == notification.getProductID() &&
                    i.getParentDepartment().getId() == notification.getDepartmentID()){

                wishList.remove(i);
                if(!wishList.add(i.getParentDepartment().getItem(notification.getProductID()))){
                    System.out.println("^ You can no longer wish for this item :( " + notification.getProductID());
                }

            }
        }

    }

    public String getName() {
        return name;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public Vector<Notification> getNotifications() {
        return notifications;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
