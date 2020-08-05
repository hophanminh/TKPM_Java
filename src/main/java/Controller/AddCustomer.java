package Controller;

import Model.Class.Customer;
import Model.Class.Store;
import Model.DAO.CustomerDAO;
import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AddCustomer {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private StoreDAO storeDAO;
    private CustomerDAO customerDAO;

    @FXML
    public TextField nameText;
    @FXML
    public TextField emailText;
    @FXML
    public TextField identifyText;
    @FXML
    public DatePicker dobPicker;
    @FXML
    public ComboBox storeComboBox;

    @FXML
    public Button closeButton;
    @FXML
    public Button saveButton;

    public AddCustomer(Stage previousStage, Controller previousController){
        try {
            thisStage = new Stage();
            parent = previousStage;
            this.previousController = previousController;

            storeDAO = new StoreDAO();
            customerDAO = new CustomerDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddCustomer.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Thêm khách hàng");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.show();
    }

    public void reloadStage() {
        AddEmployee reload = new AddEmployee(parent, previousController);
        reload.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        List<Store> stores = storeDAO.getAllStores();
        storeComboBox.getItems().add("Null");
        for(Store store: stores)
            storeComboBox.getItems().add(store);

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        saveButton.setOnAction(actionEvent -> {
            createCustomer();
        });
    }

    public void createCustomer(){
        Customer temp = new Customer();
        temp.setNameCustomer(nameText.getText().trim());
        temp.setEmailCustomer(emailText.getText().trim());
        temp.setIdentifyIDCustomer(identifyText.getText().trim());
        temp.setDobCustomer(java.sql.Date.valueOf(dobPicker.getValue()));
        if(!storeComboBox.getSelectionModel().getSelectedItem().equals("Null")){
            Store store = (Store) storeComboBox.getSelectionModel().getSelectedItem();
            temp.setStore(store);
        } else temp.setStore(null);

        if(nameText.getText().trim().equals("")
                || emailText.getText().trim().equals("")
                || identifyText.getText().trim().equals("")){
            AlertDialog fail = new AlertDialog();
            Alert failAlert = fail.createAlert(thisStage,
                    "WARNING",
                    "FAIL",
                    "There must be value in field");
            failAlert.showAndWait();
            return;
        } else {
            customerDAO.createCustomer(temp);

            AlertDialog success = new AlertDialog();
            Alert successAlert = success.createAlert(thisStage,
                    "INFORMATION",
                    "Create Successfully",
                    "Create customer success");
            successAlert.showAndWait();
            thisStage.close();
        }
    }
}
