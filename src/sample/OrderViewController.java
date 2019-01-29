package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;


import java.net.URL;
import java.util.ResourceBundle;

public class OrderViewController implements Initializable {
    @FXML
    Button DoneButton,NextOrderButton;
    @FXML Label OrderLabel;

   @FXML public void DoneButtonClicked()
   {

   }
    @FXML public void NextOrderButtonClicked()
    {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       //networking needed to read from the arraylist of orders
        OrderLabel.setText("");
    }
}
