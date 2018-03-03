package guistore.backend;

public class SoftwareDepartment extends Department {
    public SoftwareDepartment(String departmentName, int departmentID) {
        super(departmentName, departmentID);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        shoppingCart.visit(this);
    }
}
