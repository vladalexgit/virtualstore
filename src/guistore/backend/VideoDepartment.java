package guistore.backend;

public class VideoDepartment extends Department{

    public VideoDepartment(String departmentName, int departmentID) {
        super(departmentName, departmentID);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        shoppingCart.visit(this);
    }

}
