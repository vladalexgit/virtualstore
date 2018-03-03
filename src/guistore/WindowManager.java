package guistore;

import guistore.backend.Customer;

import javax.swing.*;

public class WindowManager {

    //Mediator, primeste comenzi de la celelalte ferestre si modifica UI-ul corespunzator

    private static JFrame crtFrame;
    private static Customer crtUser = null;

    public static JFrame getCrtFrame() {
        //Returneaza JFrame-ul activ
        return crtFrame;
    }

    public static void switchWindow(JPanel calleePanel, String requestedWindowName){

        //Schimba fereastra la cererea calleePanel

        switch (requestedWindowName){

            case "login":
                crtFrame.setTitle("Login");
                crtFrame.setContentPane(new Login().getPanel());
                break;
            case "admin":
                crtFrame.setTitle("Administrare magazin");
                crtFrame.setContentPane(new AdminWindow().getPanel());
                break;
            case "client":
                if(crtUser == null){
                    return;
                }
                crtFrame.setTitle("Pagina clientului " + crtUser.getName());
                crtFrame.setContentPane(new CustomerWindow(crtUser).getPanel());
                break;

        }

        crtFrame.pack();
        crtFrame.setVisible(true);
        crtFrame.remove(calleePanel);

    }

    public static void setCrtUser(Customer crtUser) {
        //Retine utilizatorul care a cerut fereastra respectiva
        WindowManager.crtUser = crtUser;
    }

    public static void main(String[] args) {

        //Punctul de pornire al programului
        //Incepem prin a afisa fereastra de initializare

        crtFrame = new JFrame("Initializare Magazin");
        crtFrame.setContentPane(new StoreInitializer().getPanel());
        crtFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        crtFrame.pack();
        crtFrame.setVisible(true);

    }

}
