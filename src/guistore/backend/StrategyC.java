package guistore.backend;

public class StrategyC implements Strategy {

    @Override
    public Item execute(WishList wishList) {
        return wishList.getLastAdded();
    }

    @Override
    public String getStrategyName() {
        return "C - cel mai recent";
    }

}
