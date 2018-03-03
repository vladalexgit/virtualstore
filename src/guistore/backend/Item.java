package guistore.backend;

public class Item {

    private String name;
    private int id;
    private double price;
    private Department parentDepartment;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Department getParentDepartment() {
        return parentDepartment;
    }

    public void setParentDepartment(Department parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public Item(String name, int id, double price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public Item(Item i) {
        this.name = i.name;
        this.id = i.id;
        this.price = i.price;
        this.parentDepartment = i.parentDepartment;
    }

    @Override
    public String toString() {
        return this.name + ";" + this.id + ";" + Store.getInstance().getPriceFormat().format(price);
    }

}
