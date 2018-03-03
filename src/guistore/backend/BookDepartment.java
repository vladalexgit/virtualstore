package guistore.backend;

public class BookDepartment extends Department {

    public BookDepartment(String departmentName, int departmentID) {
        super(departmentName, departmentID);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        shoppingCart.visit(this);
    }

}
