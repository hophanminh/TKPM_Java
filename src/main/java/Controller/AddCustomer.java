package Controller;

import Model.Class.Customer;
import Model.Class.Employee;
import Model.Class.Storage;
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
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class AddCustomer {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private StoreDAO storeDAO;
    private CustomerDAO customerDAO;
    private Preferences pref;

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
            pref = Preferences.userNodeForPackage(Employee.class);

            storeDAO = new StoreDAO();
            customerDAO = new CustomerDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerView.fxml"));
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
        thisStage.showAndWait();
    }

    public void reloadStage() {
        AddEmployee reload = new AddEmployee(parent, previousController);
        reload.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        List<Store> stores;
        int loginPosition = pref.getInt("position", -1);
        if (loginPosition != 2) {
            Store store = storeDAO.getStoreById(pref.getInt("defaultStore", -1));
            stores = new ArrayList<Store>();
            stores.add(store);
        }
        else {
            stores = storeDAO.getAllStores();
        }

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
        String error = "";
        if(nameText.getText().trim().equals("")
                || emailText.getText().trim().equals("")
                || identifyText.getText().trim().equals(""))
            error += "Phải điền đủ mọi ô trống\n";

        if(!emailText.getText().trim().matches("^([a-z\\d.]+)@([a-z\\d-]+)\\.([a-z]{2,8})(\\.[a-z]{2,8})?$"))
            error += "Email không hợp lệ\n";

        if(!nameText.getText().trim().matches("^[^\\d]+$"))
            error += "Tên không được chứa số\n";

        if (!identifyText.getText().trim().matches("^([0-9]{10})$"))
            error += "Số CMND phải có đúng 10 chữ số\n";


        if(!error.trim().equals("")){
            AlertDialog fail = new AlertDialog();
            Alert failAlert = fail.createAlert(thisStage,
                    "WARNING",
                    "Tạo thất bại",
                    error);
            failAlert.showAndWait();
            return;
        } else {
            Customer temp = new Customer();
            temp.setNameCustomer(nameText.getText().trim());
            temp.setEmailCustomer(emailText.getText().trim());
            temp.setIdentifyIDCustomer(identifyText.getText().trim());
            temp.setDobCustomer(java.sql.Date.valueOf(dobPicker.getValue()));
            if(!storeComboBox.getSelectionModel().getSelectedItem().equals("Null")){
                Store store = (Store) storeComboBox.getSelectionModel().getSelectedItem();
                temp.setStore(store);
            } else temp.setStore(null);

            customerDAO.createCustomer(temp);

            AlertDialog success = new AlertDialog();
            Alert successAlert = success.createAlert(thisStage,
                    "INFORMATION",
                    "Tạo thành công",
                    "Tạo khách hàng thành công");
            successAlert.showAndWait();
            thisStage.close();
        }
    }
}
