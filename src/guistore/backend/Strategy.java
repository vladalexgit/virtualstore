package guistore.backend;

public interface Strategy {
    Item execute(WishList wishList);
    String getStrategyName();
}
