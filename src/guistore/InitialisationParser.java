package guistore;

import guistore.backend.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class InitialisationParser {

    private static Store store = Store.getInstance();

    public static void readStore(File storeFile){

        //initializeaza magazinul folosind fisier text

        String departmentName;
        int departmentId;
        int itemCount;
        String itemName;
        int itemID;
        double itemPrice;

        Department crtDepartment;

        BufferedReader bfr = null;
        StringTokenizer st;

        try{

            bfr = new BufferedReader(new FileReader(storeFile));

            store.setName(bfr.readLine());

            String line;

            while((line= bfr.readLine()) != null) {

                st = new StringTokenizer(line, ";");

                departmentName = st.nextToken();
                departmentId = Integer.parseInt(st.nextToken());

                switch (departmentName){

                    case "BookDepartment":
                        crtDepartment = new BookDepartment(departmentName,departmentId);
                        break;
                    case "MusicDepartment":
                        crtDepartment = new MusicDepartment(departmentName,departmentId);
                        break;
                    case "SoftwareDepartment":
                        crtDepartment = new SoftwareDepartment(departmentName,departmentId);
                        break;
                    case "VideoDepartment":
                        crtDepartment = new VideoDepartment(departmentName,departmentId);
                        break;
                    default:
                        crtDepartment = null;

                }

                itemCount = Integer.parseInt(bfr.readLine());

                for(int i = 0; i < itemCount; i++){

                    st = new StringTokenizer(bfr.readLine(), ";");

                    itemName = st.nextToken();
                    itemID = Integer.parseInt(st.nextToken());
                    itemPrice = Double.parseDouble(st.nextToken());

                    crtDepartment.addItem(new Item(itemName,itemID,itemPrice));

                }

                store.addDepartment(crtDepartment);

            }

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(bfr!=null){
                    bfr.close();
                }
            }catch (IOException e ){
                e.printStackTrace();
            }
        }

    }

    public static void readCustomers(File customersFile){

        //initializeaza clientii magazinului folosind fisier text

        int customerCount;
        String customerName;
        double customerBudget;
        char customerStrategy;

        BufferedReader bfr = null;
        StringTokenizer st;

        Customer crtCustomer;

        try {

            bfr = new BufferedReader(new FileReader(customersFile));

            customerCount = Integer.parseInt(bfr.readLine());

            for (int i = 0; i < customerCount; i++) {

                st = new StringTokenizer(bfr.readLine(), ";");

                customerName = st.nextToken();
                customerBudget = Double.parseDouble(st.nextToken());
                customerStrategy = st.nextToken().charAt(0);

                crtCustomer = new Customer(customerName);
                store.enter(crtCustomer);
                crtCustomer.setShoppingCart(store.getShoppingCart(customerBudget));

                switch (customerStrategy){

                    case 'A':
                        crtCustomer.setWishList(new WishList(new StrategyA()));
                        break;
                    case 'B':
                        crtCustomer.setWishList(new WishList(new StrategyB()));
                        break;
                    case 'C':
                        crtCustomer.setWishList(new WishList(new StrategyC()));
                        break;

                }

            }

        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(bfr!=null){
                    bfr.close();
                }
            }catch (IOException e ){
                e.printStackTrace();
            }
        }
    }

}
