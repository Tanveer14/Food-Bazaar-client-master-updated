package sample;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmationViewController implements Initializable {
   @FXML
   public Label ConfirmationMessage;

    @FXML Button homeButton;


    public void homeButtonClicked(ActionEvent e) throws IOException {

        Parent shoppage= FXMLLoader.load(getClass().getResource("ShopTypesView.fxml"));
        Common.ButtonClicked(e,shoppage);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConfirmationMessage.setText(LogInPage.ConfirmationMessage);
    }
}

