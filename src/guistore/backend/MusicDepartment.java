package guistore.backend;

public class MusicDepartment extends Department {
    public MusicDepartment(String departmentName, int departmentID) {
        super(departmentName, departmentID);
    }

    @Override
    public void accept(ShoppingCart shoppingCart) {
        shoppingCart.visit(this);
    }
}
