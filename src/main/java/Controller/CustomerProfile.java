package Controller;

import Model.Class.Customer;
import Model.Class.Employee;
import Model.Class.Store;
import Model.DAO.CustomerDAO;
import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class CustomerProfile {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private CustomerDAO customerDAO;
    private StoreDAO storeDAO;
    private Customer customer;
    private Preferences pref;

    @FXML
    public javafx.scene.control.TextField nameText;
    @FXML
    public javafx.scene.control.TextField emailText;
    @FXML
    public javafx.scene.control.TextField identifyText;
    @FXML
    public DatePicker dobPicker;
    @FXML
    public ComboBox storeComboBox;

    @FXML
    public javafx.scene.control.Button closeButton;
    @FXML
    public Button saveButton;

    public CustomerProfile(Stage previousStage, Controller previousController, Customer customer){
        try {
            thisStage = new Stage();
            parent = previousStage;
            previousController = previousController;
            this.customer = customer;
            customerDAO = new CustomerDAO();
            storeDAO = new StoreDAO();
            pref = Preferences.userNodeForPackage(Employee.class);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CustomerView.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Hồ sơ khách hàng");
            thisStage.setResizable(false);

            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage(){
        thisStage.showAndWait();
    }
    public void reloadStage(){
        CustomerProfile customerProfile = new CustomerProfile(parent, previousController, customer);
        customerProfile.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        saveButton.setText("Cập nhập");
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

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        saveButton.setOnAction(actionEvent -> {
            updateCustomer();
        });

        nameText.setText(this.customer.getNameCustomer());
        emailText.setText(this.customer.getEmailCustomer());
        identifyText.setText(this.customer.getIdentifyIDCustomer());
        dobPicker.setValue(this.customer.getDobCustomer().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        storeComboBox.setValue(this.customer.getStore());
    }
    public void updateCustomer(){
        String error = "";
        if(nameText.getText().trim().equals("")
                || emailText.getText().trim().equals("")
                || identifyText.getText().trim().equals(""))
            error += "Phải điền đủ mọi ô trống\n";

        if(!emailText.getText().trim().matches("^([a-z\\d.]+)@([a-z\\d-]+)\\.([a-z]{2,8})(\\.[a-z]{2,8})?$"))
            error += "Email không hợp lệ\n";

        if(!nameText.getText().trim().matches("^[^\\d]+$"))
            error += "Tên không được chứa số\n";

        if (!identifyText.getText().trim().matches("^([\\d]{10})$"))
            error += "Số CMND phải ít nhất 10 chữ số\n";

        if(!error.trim().equals("")){
            AlertDialog fail = new AlertDialog();
            Alert failAlert = fail.createAlert(thisStage,
                    "WARNING",
                    "Cập nhập thất bại",
                    error);
            failAlert.showAndWait();
            return;
        } else {
            Customer temp = new Customer();
            temp.setIdCustomer(this.customer.getIdCustomer());
            temp.setNameCustomer(nameText.getText().trim());
            temp.setIdentifyIDCustomer(identifyText.getText().trim());
            temp.setEmailCustomer(emailText.getText().trim());
            temp.setDobCustomer(java.sql.Date.valueOf(dobPicker.getValue()));
            if(!storeComboBox.getSelectionModel().getSelectedItem().equals("Null"))
                temp.setStore((Store)storeComboBox.getSelectionModel().getSelectedItem());
            else temp.setStore(null);

            customerDAO.updateCustomer(temp);

            AlertDialog success = new AlertDialog();
            Alert successAlert = success.createAlert(thisStage,
                    "INFORMATION",
                    "Cập nhập thành công",
                    "Cập nhập thông tin khách hàng thành công");
            successAlert.showAndWait();
            thisStage.close();
        }
    }
}
