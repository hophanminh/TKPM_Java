package Controller;

import DAO.Item_BookDAO;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class addItem {
    public TextField textNameItem;
    public TextField textPriceItem;

    public void submitButton(ActionEvent actionEvent) {
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
            System.out.println("Wrong id or password");
            Stage addItemWindow = (Stage) textNameItem.getScene().getWindow(); // set owner

            // Create and display Alert
            Alert warnings = new Alert(Alert.AlertType.WARNING);
            warnings.setTitle("Not number");
            warnings.setHeaderText(null);
            warnings.setContentText("Price is not number");

            // Lock alert
            warnings.initOwner(addItemWindow);
            warnings.initModality(Modality.WINDOW_MODAL);

            warnings.showAndWait();
            textNameItem.clear();
            textPriceItem.clear();
        }
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
