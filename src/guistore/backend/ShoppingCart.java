package guistore.backend;

import java.util.Collection;
import java.util.Comparator;
import java.util.ListIterator;

public class ShoppingCart extends ItemList implements Visitor {

    private double budget;

    public ShoppingCart(double budget) {

        super(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Item i1 = (Item)o1;
                Item i2 = (Item)o2;

                if(i1.getPrice() > i2.getPrice()){
                    return 1;
                }else if(i1.getPrice() == i2.getPrice()){
                    return i1.getName().compareTo(i2.getName());
                }else{
                    return -1;
                }

            }
        });

        this.budget = budget;

    }

    @Override
    public boolean add(Item element) {
        if((budget - this.getTotalPrice() - element.getPrice()) >= 0){
            return super.add(element);
        }else{
            return false;
        }
    }

    @Override
    public boolean addAll(Collection<? extends Item> c) {
        double newItemsCost = 0;
        for (Item i: c) {
            newItemsCost += i.getPrice();
        }
        if((budget - this.getTotalPrice() - newItemsCost) >= 0){
            return super.addAll(c);
        }else{
            return false;
        }
    }

    @Override
    public Double getTotalPrice() {

        if(this.isEmpty()){
            return 0d;
        }else {
            ListIterator iter = this.listIterator();
            double totalPrice = 0;

            while (iter.hasNext()) {
                Item nextItem = (Item) iter.next();
                totalPrice += nextItem.getPrice();
            }

            return totalPrice;
        }

    }

    public Double getRemainingBudget() {
        return this.budget - this.getTotalPrice();
    }

    @Override
    public void visit(BookDepartment bookDepartment) {

        ListIterator iter = this.listIterator();
        Item i;

        //aplica reducerea pe produsele ce apartin de bookDepartment
        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getParentDepartment() == bookDepartment){

                this.remove(i);
                i.setPrice(i.getPrice() - i.getPrice()*0.1);
                this.add(i);

            }

        }

    }

    @Override
    public void visit(MusicDepartment musicDepartment) {

        ListIterator iter = this.listIterator();
        Item i;
        double bonus = 0d;

        //aplica reducerea pe produsele ce apartin de musicDepartment
        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getParentDepartment() == musicDepartment){

                bonus+= i.getPrice();

            }

        }

        bonus = bonus * 0.1;
        this.budget+=bonus;

    }

    @Override
    public void visit(SoftwareDepartment softwareDepartment) {

        ItemList sdItems = softwareDepartment.getItems();
        ListIterator iter = sdItems.listIterator();

        if(sdItems.isEmpty()){
            System.out.println("eroare lista este vida");
            return;
        }

        Item cheapestItem = (Item)iter.next();
        Item i;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getPrice() < cheapestItem.getPrice()){
                cheapestItem = i;
            }

        }

        if(cheapestItem.getPrice() > this.getRemainingBudget()){
            //atunci aplica 20% bonus
            iter = this.listIterator();

            //aplica reducerea pe produsele ce apartin de softwareDepartment
            while(iter.hasNext()){

                i = (Item)iter.next();

                if(i.getParentDepartment() == softwareDepartment){

                    this.remove(i);
                    i.setPrice(i.getPrice() - i.getPrice() * 0.2);
                    this.add(i);

                }

            }
        }

    }

    @Override
    public void visit(VideoDepartment videoDepartment) {

        ItemList sdItems = videoDepartment.getItems();
        ListIterator iter = sdItems.listIterator();

        if(sdItems.isEmpty()){
            System.out.println("eroare lista este vida");
            return;
        }

        Item mostExpensiveItem = (Item)iter.next();
        Item i;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getPrice() > mostExpensiveItem.getPrice()){
                mostExpensiveItem = i;
            }

        }

        //determina totalul produselor ce apartin de videoDepartment

        iter = this.listIterator();
        double totalVideo = 0d;

        while(iter.hasNext()){

            i = (Item)iter.next();

            if(i.getParentDepartment() == videoDepartment){
                totalVideo += i.getPrice();
            }

        }

        if(totalVideo > mostExpensiveItem.getPrice()){
            //aplica 15% bonus

            iter = this.listIterator();

            while(iter.hasNext()){

                i = (Item)iter.next();

                if(i.getParentDepartment() == videoDepartment){

                    this.remove(i);
                    i.setPrice(i.getPrice() - i.getPrice() * 0.15);
                    this.add(i);

                }

            }
        }

        this.budget += totalVideo * 0.05;

    }

}
