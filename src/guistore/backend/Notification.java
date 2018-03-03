package guistore.backend;

import java.util.Date;

public class Notification {

    private Date creationTime;
    private NotificationType type;
    private int departmentID;
    private int productID;

    public enum NotificationType{
        ADD,REMOVE,MODIFY
    }

    public Notification(NotificationType type, int departmentID, int productID){
        this.creationTime = new Date();
        this.type = type;
        this.departmentID = departmentID;
        this.productID = productID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public NotificationType getType() {
        return type;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public int getProductID() {
        return productID;
    }

    @Override
    public String toString() {
        return this.type+";"+this.productID+";"+this.departmentID;
    }

}
