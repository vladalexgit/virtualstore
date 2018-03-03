package guistore.backend;

public interface Subject {

    void addObserver(Customer customer);
    void removeObserver(Customer c);
    void notifyAllObservers(Notification n);

}
