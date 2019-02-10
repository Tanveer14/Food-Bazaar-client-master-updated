package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LogInPage implements Initializable{
    @FXML private TextField CustomerName;
    @FXML private TextField CustomerMail;
    @FXML private TextField CustomerPhone;
    @FXML private TextField CustomerAddress;
    @FXML private Button OrderButton;
    @FXML private Button goBackButton;
    @FXML private TextArea CommentBox;
    @FXML private Label Heading,Labelcheck;

    private  Socket socket;


    public static String ConfirmationMessage=new String();
    public static String gobackPage;
    public int customercount=0;

    public Customer customer=new Customer();
    public void setCustomerName() {}
    public void setCustomerMail() {}
    public void setCustomerPhone() {}
    public void setCustomerAddress() {}

    public void goBackButtonClicked(ActionEvent event) throws Exception{
        Alert alert=new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText("If you go back, your order will be cancelled and your cart will be cleared");
        Optional<ButtonType> action=alert.showAndWait();
        if(action.get()==ButtonType.OK) {
            Parent subPage = FXMLLoader.load(getClass().getResource("CommonTypeView.fxml"));
            Common.ButtonClicked(event, subPage);
        }
    }

    public boolean CheckAChar(String str,char c)
    {
        char []arr=str.toCharArray();
        for (char a:arr
             ) {
            if(a==c)return true;
        }
        return false;
    }
    public void OrderButtonClicked (ActionEvent e) throws Exception {
        boolean NameValid=false,NumValid=false;

        String tempc="";
        customer.setName(CustomerName.getText());

        tempc=CustomerMail.getText();
        if(tempc.toLowerCase().equals(tempc)&&CheckAChar(tempc,'@')){
            customer.setMail(tempc);
            NameValid=true;
        }else {
            NameValid=false;
        }
        boolean state1=true,state2=true;
        tempc=CustomerPhone.getText();



        if(!(tempc.length()==11||tempc.length()==9))state1=false;
        String digits="0123456789";
        char []tempcDigits=tempc.toCharArray();
        for (char c:tempcDigits
             ) {
            state2 = CheckAChar(digits, c);
            if (state2 == false) {
                break;
            }
        }

        if((state1==false||state2==false))NumValid=false;
        else {

            customer.setContactNo(tempc);
            NumValid=true;
        }

        customer.setAddress(CustomerAddress.getText());
        customer.setProductList(CommonTypeViewController.TableProductList);
        customer.setTotalPrice(CommonTypeViewController.totalPrice);
        //customer.setArea((String) CustomerArea.getValue());
        System.out.println(customer);

        int temp=0;
        if(CustomerName.getText().length()==0||CustomerMail.getText().length()==0||CustomerAddress.getText().length()==0||CustomerPhone.getText().length()==0)
        {
            temp=1;
        }

        //modifying file available units
        if(temp==0) {
            //Labelcheck.setText("");
            String temptype;
            for (int i = 0; i < customer.ProductList.size(); i++) {
                temptype = customer.getProductList().get(i).getType();
            }
            customer.setId(customercount);
            ConfirmationMessage=customer.toMessage();

            if(NameValid&&NumValid){

                try{
                    socket=new Socket("localhost",4444);
                    ObjectOutputStream Out=new ObjectOutputStream(socket.getOutputStream());
                    Out.writeObject(customer);
                    Out.flush();
                    Out.close();
                    socket.close();

                }catch (Exception ex)
                {
                    System.out.println(ex);
                }
                Parent newsceneparent= FXMLLoader.load(getClass().getResource("ConfirmationView.fxml"));
                Common.ButtonClicked(e,newsceneparent);
            }else {
                //Labelcheck.setText("Email Address or Contact No Not Valid");

                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Email Address or Contact No Not Valid");
                alert.showAndWait();
            }



        }else{
           // Labelcheck.setText("you have left some options empty");
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("you have left some options empty");
            alert.showAndWait();
        }


        //to read from the file and get value
       /* FileInputStream fileInputStream=new FileInputStream(customerFile);
        ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
        Customer c=new Customer();
        c=(Customer)objectInputStream.readObject();
        System.out.println(c);
        */


       /* try{
            socket=new Socket("localhost",4444);
            ObjectOutputStream Out=new ObjectOutputStream(socket.getOutputStream());
            Out.writeObject(customer);
            Out.flush();
            Out.close();
            socket.close();

        }catch (Exception ex)
        {
            System.out.println(ex);
        }*/

       try {
           CustomerName.setText("");
           CustomerMail.setText("");
           CustomerPhone.setText("");
           CustomerAddress.setText("");
           Labelcheck.setText("");
       }catch (Exception e2){

       }



        ///table items must be removed
        ///data must be written in a file
        ///send mails
        ///after selection and confirmation, the items must decrease.



        //scene change

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Image icon=new Image(getClass().getResourceAsStream("icon.png"));
        gobackPage=CommonTypeViewController.Currentpage;

        goBackButton.setStyle("-fx-background-color: #232020;"+"-fx-border-color: ORANGE");
        OrderButton.setStyle("-fx-background-color: #232020;"+"-fx-border-color: ORANGE");
        Heading.setStyle("-fx-background-color: #232020;"+"-fx-border-color: ORANGE");


        try {
            socket=new Socket("localhost",4444);
            ObjectOutputStream outtoServer=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream oinfromServer=new ObjectInputStream(socket.getInputStream());
            outtoServer.writeObject("Customer Count");
            customercount= (int) oinfromServer.readObject();

            System.out.println("Customer no- "+customercount);

            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*File file=new File("Idcount.txt");
        try {

            Scanner scanner=new Scanner(file);
            customercount=scanner.nextInt();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}

