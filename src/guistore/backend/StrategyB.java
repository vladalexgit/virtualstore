package guistore.backend;

public class StrategyB implements Strategy {

    @Override
    public Item execute(WishList wishList) {
        return wishList.getItem(0);
    }

    @Override
    public String getStrategyName() {
        return "B - primul produs(alfabetic)";
    }

}
