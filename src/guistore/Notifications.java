package guistore;

import guistore.backend.Customer;
import guistore.backend.Notification;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class Notifications {

    //Clasa ce defineste comportamentul ferestrei de notificari

    private JList listNotificari;
    private JPanel panelMain;
    private JButton OKButton;

    public Notifications(final JFrame frame, Customer activeCustomer) {

        Vector<Notification> notifications = activeCustomer.getNotifications();

        DefaultListModel<String> lm = new DefaultListModel();

        for(Notification n: notifications){
            lm.addElement(getHumanReadable(n));
        }

        listNotificari.setModel(lm);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //inchide fereastra de notificari
                frame.dispose();
            }
        });

    }

    private String getHumanReadable(Notification n){

        //formateaza notificarea n pentru a fi citita mai usor

        String buffer = "";

        switch (n.getType()){

            case ADD:
                buffer+="A fost adaugat produsul ";
                break;
            case MODIFY:
                buffer+="A fost modificat produsul ";
                break;
            case REMOVE:
                buffer+="A fost sters produsul ";
                break;

        }

        buffer+=n.getProductID();
        buffer+=" din departamentul "+n.getDepartmentID();

        buffer+=" "+n.getCreationTime();

        return buffer;

    }

    public JPanel getPanel() {
        //returneaza panoul principal folosit de window manager pentru a afisa fereastra
        return panelMain;
    }

}
