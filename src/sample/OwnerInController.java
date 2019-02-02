package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OwnerInController implements Initializable {

    Socket socket;
    @FXML public ComboBox<String> type,unit_type;
    @FXML public ComboBox<String> name;
    @FXML public TextField quantity,unit_price;
    @FXML private Button updateButton,nextpagebutton,OrderCheckButton;
    @FXML private Label FootLabel,ItemshowLabel;

    public static ArrayList<product> item=new ArrayList<>();


    @FXML public void nextpagebuttonClicked(ActionEvent event) throws Exception{
        Parent shoppage= FXMLLoader.load(getClass().getResource("ShopTypesView.fxml"));
        Common.ButtonClicked(event,shoppage);
    }

   /* public void MoreItemButtonClicked(){
        try{
            ArrayList<product> temp=null;
            String s=type.getSelectionModel().getSelectedItem();
            String t=name.getSelectionModel().getSelectedItem();
            if(s.equalsIgnoreCase("Fruits")) {
                temp = fruit_items;
            }
            else if(s.equalsIgnoreCase("Vegetables")){
                temp=veg_items;
            }
            else if(s.equalsIgnoreCase("Meat and Fish")){
                temp=mf_items;
            }
            else if(s.equalsIgnoreCase("Staples")){
                temp=staple_items;
            }
            for(int i=0;i<temp.size();i++){
                if(temp.get(i).getName().equalsIgnoreCase(t)){
                    temp.get(i).setPrice(Integer.parseInt(unit_price.getText()));
                    temp.get(i).setUnit_type(unit_type.getSelectionModel().getSelectedItem());
                    temp.get(i).add_available_units(Integer.parseInt(quantity.getText()));
                    System.out.println(temp.get(i));
                    break;
                }
            }
        type.getSelectionModel().clearSelection();
        unit_type.getSelectionModel().clearSelection();
        name.getSelectionModel().clearSelection();
            unit_price.setText("");
            quantity.setText("");
        }catch (Exception ex)
        {
            FootLabel.setText("You've left an option empty!");
        }

    }*/



    public void productTypeSelected(){
        try{
            String s=type.getSelectionModel().getSelectedItem();
            if(s!=null) {
                try{
                    socket=new Socket("localhost",4444);
                    ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream oi=new ObjectInputStream(socket.getInputStream());
                    outtoServer.writeObject(s);
                    item=(ArrayList<product>)oi.readObject();

                }catch(Exception e){
                    System.out.println(e);
                }
                name.getItems().clear();

                for (product i : item) {
                    name.getItems().add(i.getName());
                }
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
        name.setDisable(false);
        ItemshowLabel.setText("");
    }

    public void ItemTypeSelected() throws Exception{
        String s=name.getSelectionModel().getSelectedItem();
        for(product i:item){
            if(i.getName().equals(s)){
                String text="Product name: "+s+"\nAvailable Units: "+i.getAvailable_units()+"\nPrice: "+i.getPrice()+" tk per "+i.getUnit_type();
                ItemshowLabel.setText(text);
                break;
            }
        }
    }


    public void updateButtonClicked() throws IOException {
        try{
            String s=type.getSelectionModel().getSelectedItem();
            String t=name.getSelectionModel().getSelectedItem();
            int k=0;
            for(int i=0;i<item.size();i++){
                if(item.get(i).getName().equalsIgnoreCase(t)){
                    k=1;
                    item.get(i).setPrice(Integer.parseInt(unit_price.getText()));
                    item.get(i).setUnit_type(unit_type.getSelectionModel().getSelectedItem());
                    item.get(i).add_available_units(Integer.parseInt(quantity.getText()));
                    break;
                }
            }
            if(k==0){
                item.add(new product(t,s,Integer.parseInt(unit_price.getText()),
                        Integer.parseInt(quantity.getText()),unit_type.getSelectionModel().getSelectedItem()));
            }
            try{
                socket=new Socket("localhost",4444);
                ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
                //ObjectInputStream oi=new ObjectInputStream(socket.getInputStream());
                outtoServer.writeObject(item);
            }catch(Exception ex){
                System.out.println(ex);
            }
            //Common.fileupdate(new File(s.toLowerCase()+".txt"),item);
            FootLabel.setText("Updated ! ! !");
            type.getSelectionModel().clearSelection();
            unit_type.getSelectionModel().clearSelection();
            name.getSelectionModel().clearSelection();
            unit_price.setText("");
            quantity.setText("");
            ItemshowLabel.setText("");
        }catch(Exception ex){
            FootLabel.setText("You've left an option empty!");
        }
    }

    public void OrderCheckButtonClicked(ActionEvent e) throws Exception{
        Parent page= FXMLLoader.load(getClass().getResource("OrderView.fxml"));
        Common.ButtonClicked(e,page);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<String> types=new ArrayList<>();
        try{
            socket=new Socket("localhost",4444);
            ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream oi=new ObjectInputStream(socket.getInputStream());
            String s="type list";
            outtoServer.writeObject(s);
            types = (ArrayList<String>) oi.readObject();

        }catch(Exception ex){
            System.out.println(ex);
        }

        type.getItems().addAll(types);

        ArrayList<String> unit_types=new ArrayList<>();
        //unit_types=Common.OwnerFile(unit_types,new File("Unit type list"));
        try{
            socket=new Socket("localhost",4444);
            ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream oi=new ObjectInputStream(socket.getInputStream());
            String s="Unit type list";
            outtoServer.writeObject(s);
            unit_types = (ArrayList<String>) oi.readObject();

        }catch(Exception ex){
            System.out.println(ex);
        }
        unit_type.getItems().addAll(unit_types);



        // ArrayList<product> MF=new ArrayList<>();
        /*MF.add(new product("Onion","Vegetable"));
        MF.add(new product("Tomato","Vegetable"));
        MF.add(new product("Potato","Vegetable"));
        MF.add(new product("Chilli", "Vegetable"));
        MF.add(new product("Beans","Vegetable"));*/



        /*MF.add(new product("Chompa Banana","Fruit"));
        MF.add(new product("Green Apples","Fruit"));
        MF.add(new product("Orange","Fruit"));
        MF.add(new product("Green Grapes", "Fruit"));
        MF.add(new product("Coconut","Fruit"));*/


        /*MF.add(new product("Rice","Staple"));
        MF.add(new product("Dal","Staple"));
        MF.add(new product("Salt","Staple"));
        MF.add(new product("Oil", "Staple"));
        MF.add(new product("Turmeric Powder","Staple"));*/


        /*MF.add(new product("Rui Fish","Fish"));
        MF.add(new product("Prawn","Fish"));*/
       /*MF.add(new product("Broiler Chicken","Meat"));
        MF.add(new product("Beef", "Meat"));
        MF.add(new product("Mutton","Meat"));*/


       /*try {
            FileOutputStream fo = new FileOutputStream(new File("meat.txt"));
            ObjectOutputStream oo = new ObjectOutputStream(fo);
            for (product i : MF) {
                oo.writeObject(i);
            }
            oo.close();
            fo.close();
        }catch(FileNotFoundException ff){
            System.out.println( ff);
        }catch(IOException io){
            System.out.println(io);
        }*/






        //////unit type list file created

       /* ArrayList<String> unit_types=new ArrayList<>();
        unit_types.add("pcs");
        unit_types.add(" kg");
        unit_types.add("litre");

       try {
            FileOutputStream fo=new FileOutputStream("Unit type list",true);
            ObjectOutputStream Oo=new ObjectOutputStream(fo);
            Oo.writeObject(unit_types);

            //FootLabel.setText("Successfully Updated");

            Oo.close();
            fo.close();
        }catch (Exception e){
           System.out.println(e);
        }*/

        /////unit type list file creation closed


        ////type list file created

       /* ArrayList<String> types=new ArrayList<>();

       types.add("Staple");
        types.add("Vegetable");
        types.add("Meat");
        types.add("Fruit");
        types.add("Fish");
        type.getItems().addAll(types);

       try {
            FileOutputStream fo=new FileOutputStream("type list");
            ObjectOutputStream Oo=new ObjectOutputStream(fo);
            Oo.writeObject(types);

            //FootLabel.setText("Successfully Updated");

            Oo.close();
            fo.close();
        }catch (Exception e){
           System.out.println(e);
        }*/


    }
}
