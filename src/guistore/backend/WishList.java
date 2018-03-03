package guistore.backend;

import java.util.Comparator;
import java.util.ListIterator;

public class WishList extends ItemList {

    private Strategy strategy = null;

    private Item lastAdded = null;

    public Item getLastAdded() {
        return lastAdded;
    }

    @Override
    public boolean add(Item element) {
        lastAdded = element;
        return super.add(element);
    }

    public WishList(Strategy strategy) {

        super(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Item i1 = (Item)o1;
                Item i2 = (Item)o2;

                return i1.getName().compareTo(i2.getName());
            }
        });

        this.strategy = strategy;

    }

    @Override
    public Double getTotalPrice() {

        ListIterator iter = this.listIterator();
        double totalPrice = 0;

        while(iter.hasNext()){
            Item nextItem = (Item)iter.next();
            totalPrice += nextItem.getPrice();
        }

        return totalPrice;

    }

    public Item executeStrategy(){
        return this.strategy.execute(this);
    }

    public Strategy getStrategy() {
        return strategy;
    }

}
