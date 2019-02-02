package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OrderViewController implements Initializable {
    @FXML
    Button DoneButton,NextOrderButton;
    @FXML Label OrderLabel;
    Socket socket;
    ArrayList<Customer> OrderList;
    private int count;

    public OrderViewController() throws IOException {
    }

    @FXML public void DoneButtonClicked()
   {


   }
    @FXML public void NextOrderButtonClicked()
    {

        if(count+1<OrderList.size()){
            count++;
            OrderLabel.setText(OrderList.get(count).toMessage());
        }
    }
    public void PreviousOrderButtonClicked(ActionEvent event) {

        if(count-1>=0)
        {
            count--;
            OrderLabel.setText(OrderList.get(count).toMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       //networking needed to read from the arraylist of orders
        count=0;
        try {
            socket=new Socket("localhost",4444);
            ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream oinfromServer=new ObjectInputStream(socket.getInputStream());
            outtoServer.writeObject("Show Orders");
            OrderList= (ArrayList<Customer>) oinfromServer.readObject();
            System.out.println(OrderList);
            OrderLabel.setText(OrderList.get(count).toMessage());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void GoBackButtonClicked(ActionEvent event) throws IOException {
        Parent newsceneparent= FXMLLoader.load(getClass().getResource("OwnerIn.fxml"));
        Common.ButtonClicked(event,newsceneparent);
    }
}
