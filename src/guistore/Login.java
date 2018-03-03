package guistore;

import guistore.backend.Customer;
import guistore.backend.Store;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login {

    //Clasa ce defineste comportamentul ferestrei de login

    private JButton buttonLogin;
    private JTextField textUser;
    private JPanel mainPanel;
    private JLabel labelLogo;

    private String sanitizeUsername(String username){

        //modifica numele de utilizator introdus pentru a respecta formatul intern
        //prima litera mare restul litere mici

        username = username.toLowerCase();
        return username.substring(0,1).toUpperCase() + username.substring(1);

    }

    public Login() {

        labelLogo.setText("Login "+ Store.getInstance().getName());

        buttonLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //incearca login

                if(textUser.getText().isEmpty()){
                    return;
                }

                if(sanitizeUsername(textUser.getText()).equals("Admin")){
                    WindowManager.switchWindow(getPanel(),"admin");
                    return;
                }

                Customer c = Store.getInstance().getCustomerByName(sanitizeUsername(textUser.getText()));

                if(c!=null){
                    WindowManager.setCrtUser(c);
                    WindowManager.switchWindow(getPanel(),"client");
                }else{
                    JOptionPane.showMessageDialog(WindowManager.getCrtFrame(),"Clientul \""+textUser.getText()+"\" nu a fost gasit! Verfica cutomers.txt",
                            "EroareLogin",JOptionPane.WARNING_MESSAGE);
                }

            }
        });
    }

    public JPanel getPanel() {
        //returneaza panoul principal folosit de window manager pentru a afisa fereastra
        return mainPanel;
    }

}
