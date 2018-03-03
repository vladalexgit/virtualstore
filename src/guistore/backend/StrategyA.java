package guistore.backend;

import java.util.ListIterator;

public class StrategyA implements Strategy {

    @Override
    public Item execute(WishList wishList) {

        ListIterator iter = wishList.listIterator();
        Item i;
        double lowestPrice = Double.MAX_VALUE;
        Item cheapestItem = null;

        while(iter.hasNext()){
            i = (Item)iter.next();
            if(i.getPrice() < lowestPrice){
                cheapestItem = i;
                lowestPrice = i.getPrice();
            }
        }

        return cheapestItem;

    }

    @Override
    public String getStrategyName() {
        return "A - cel mai ieftin";
    }

}
