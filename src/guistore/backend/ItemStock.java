package guistore.backend;

import java.util.Comparator;
import java.util.ListIterator;

public class ItemStock extends ItemList {

    public ItemStock() {
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
}
