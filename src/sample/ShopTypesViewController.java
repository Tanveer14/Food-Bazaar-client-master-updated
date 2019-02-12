package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import javafx.event.ActionEvent;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ShopTypesViewController implements Initializable {
    Socket socket;

    @FXML
    ComboBox<String> FoodType;
    ArrayList<String> types=new ArrayList<>();
    @FXML
    TextField idtext;
    @FXML Button CheckButton;

    public static String SelectedType=null;


    public void FoodTypeSelected(ActionEvent event) throws Exception{
        SelectedType=FoodType.getValue();
        Parent Page= FXMLLoader.load(getClass().getResource("CommonTypeView.fxml"));
        int i=0;
        Common.ButtonClicked(event,Page);

        /*while(true){
            temp.add((product) oi.readObject());
        }
        for(product i:temp){
            System.out.println(i);
        }*/


        //SelectedType=FoodType.getValue();

    }

    @FXML public void  CheckButtonClicked()
    {
        if(idtext.getText().equals("")){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("ID cannot be empty!");
            alert.showAndWait();
        }else {
            String temp="CheckOrder "+idtext.getText();
            try{
                socket=new Socket("localhost",4444);
                ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream oinfromserver=new ObjectInputStream(socket.getInputStream());
                outtoServer.writeObject(temp);
               // Order CustomerOrder = (Order) oinfromserver.readObject();
                CustomerOrder CustomerOrder= (CustomerOrder)oinfromserver.readObject();

                /*CustomerOrder CustomerOrder=new CustomerOrder(3,"Ordered");
                CustomerOrder.setName("naeem");*/
                if(CustomerOrder.getStatus().equals("Invalid ID")){
                    //alertbox
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText(CustomerOrder.getStatus()+" !");
                    alert.showAndWait();
                    System.out.println(CustomerOrder.getStatus());
                }
                else {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Order Status");
                    String temp1="Customer Name: "+CustomerOrder.getName()+"\nCustomer ID: "+CustomerOrder.getId()+"\nOrder:\t"+CustomerOrder.getStatus();
                    alert.setContentText(temp1);
                    alert.showAndWait();
                    System.out.println(CustomerOrder.getStatus());
                }

            }catch(Exception ex){
                System.out.println(ex);
            }
            //send the id with proper instruction
            //get the reply message from server
            //reply will be an order type object
        }
        idtext.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

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
        //tor wood color 483121

        FoodType.getItems().addAll(types);
       // FoodType.setStyle("-fx-text-fill: #ffa500;");

        CheckButton.setStyle("-fx-background-color: #232020;"+"-fx-border-color: #fce28c;");

    }
}
