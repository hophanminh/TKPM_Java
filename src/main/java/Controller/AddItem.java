package Controller;

import Model.Class.Employee;
import Model.Class.Item;
import Model.DAO.Item_BookDAO;
import View.AlertDialog;
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

public class AddItem implements Controller {
    private Stage thisStage;
    private Controller previousController;
    private Preferences pref;

    @FXML
    public TextField textNameItem;
    @FXML
    public TextField textPriceItem;
    @FXML
    public TextField textCostItem;
    @FXML
    public TextField textQuantityItem;

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

    public void reloadStage() {
        AddItem reload = new AddItem(thisStage, previousController);
        reload.showStage();
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction(actionEvent -> {
            System.out.println("Add Item");

            String nameItem = textNameItem.getText().trim();
            int costItem = 0;
            int priceItem = 0;
            int quantityItem = 0;

            // Kiem tra dieu kien
            String error = "";
            if(!isNumber(textCostItem.getText().trim()))
                error += "Giá gốc phải là một số\n";
            else costItem = Integer.valueOf(textCostItem.getText().trim());

            if(!isNumber(textPriceItem.getText().trim()))
                error += "Giá thành phải là một số\n";
            else priceItem = Integer.valueOf(textPriceItem.getText().trim());

            if(!isNumber(textQuantityItem.getText().trim()))
                error += "Số lượng phải là một số\n";
            else quantityItem = Integer.valueOf(textQuantityItem.getText().trim());

            if(textNameItem == null || textCostItem == null || textPriceItem == null || textQuantityItem == null)
                error += "Phải nhập đủ hết các trường dữ liệu\n";

            if(costItem >= priceItem)
                error += "Giá bán phải cao hơn giá gốc\n";

            if(error.equals("")){
                Item_BookDAO item_bookDAO = new Item_BookDAO();
                Item item = new Item(nameItem, costItem, priceItem, quantityItem);
                item_bookDAO.addItem(item);
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Success",
                        "Add successfully");
                successAlert.showAndWait();
                System.out.println("Success");
            } else {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Sai cú pháp", error);
                failAlert.showAndWait();

                textNameItem.clear();
                textPriceItem.clear();
                textCostItem.clear();
                textQuantityItem.clear();
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
