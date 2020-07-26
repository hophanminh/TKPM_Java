package Controller;

import Model.Class.Employee;
import Model.DAO.Item_BookDAO;
import View.AlertDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AddItem {
    private Stage thisStage;
    private Controller previousController;
    private Preferences pref;

    @FXML
    public TextField textNameItem;

    @FXML
    public TextField textPriceItem;

    @FXML
    public Button submitButton;

    public AddItem(Stage previousStage, Controller previous){
        try {
            thisStage = new Stage();
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addItem.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 668, 592));
            thisStage.setTitle("Thêm sản phẩm");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(previousStage);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.show();
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction(actionEvent -> {
            System.out.println("hi");
            System.out.println("Name: " + textNameItem.getText());
            System.out.println("Price: " + Integer.valueOf(textPriceItem.getText()));

            String nameItem = textNameItem.getText();
            if(isNumber(textPriceItem.getText())){
                Item_BookDAO item_bookDAO = new Item_BookDAO();
                //Item item = new Item(textNameItem.getText(), Integer.valueOf(textPriceItem.getText()));
                item_bookDAO.addItem(nameItem, Integer.valueOf(textPriceItem.getText()));
                System.out.println("Success");
            } else {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Sai cú pháp",
                        "Giá tiền phải là một con số.");
                failAlert.showAndWait();

                textNameItem.clear();
                textPriceItem.clear();
            }
        });
    }

    public boolean isNumber(String number){
        try
        {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}
