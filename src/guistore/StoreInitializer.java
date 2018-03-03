package guistore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class StoreInitializer {

    //Clasa ce defineste comportamentul ferestrei de initializare

    private JTextField textFieldStore;
    private JTextField textFieldCustomers;
    private JButton initializeazaMagazinulButton;
    private JPanel panelStoreInitializer;
    private JButton btOpenStore;
    private JButton btOpenCustomers;

    private File store = null;
    private File customers = null;

    public JPanel getPanel() {
        //returneaza panoul principal folosit de window manager pentru a afisa fereastra
        return panelStoreInitializer;
    }

    public StoreInitializer() {

        initializeazaMagazinulButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //initializeaza magazinul utilizand pathurile din textbox-uri

                if(textFieldStore.getText().isEmpty() || textFieldCustomers.getText().isEmpty()){
                    JOptionPane.showMessageDialog(WindowManager.getCrtFrame(), "Nu ai ales un fisier!");
                    return;
                }

                InitialisationParser.readStore(new File(textFieldStore.getText()));
                InitialisationParser.readCustomers(new File(textFieldCustomers.getText()));

                WindowManager.switchWindow(getPanel(), "login");

            }
        });

        btOpenStore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //deschide file picker

                JFileChooser fc = new JFileChooser(new File(System.getProperty("user.home")));
                fc.setDialogType(JFileChooser.FILES_ONLY);

                //actualizeaza pathul in text box

                switch(fc.showOpenDialog(WindowManager.getCrtFrame())){
                    case JFileChooser.APPROVE_OPTION:
                        textFieldStore.setText(fc.getSelectedFile().getAbsolutePath());
                        break;
                }

            }
        });

        btOpenCustomers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //deschide file picker

                JFileChooser fc = new JFileChooser(new File(System.getProperty("user.home")));
                fc.setDialogType(JFileChooser.FILES_ONLY);

                //actualizeaza pathul in text box

                switch(fc.showOpenDialog(WindowManager.getCrtFrame())){
                    case JFileChooser.APPROVE_OPTION:
                        textFieldCustomers.setText(fc.getSelectedFile().getAbsolutePath());
                        break;
                }

            }
        });

    }

}
