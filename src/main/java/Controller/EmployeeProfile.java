package Controller;

import Model.Class.Employee;
import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.EmployeeDAO;
import Model.DAO.StorageDAO;
import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;
import java.util.prefs.Preferences;

public class EmployeeProfile {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;
    private EmployeeDAO employeeDAO;
    private StorageDAO storageDAO;
    private StoreDAO storeDAO;
    private Employee employee;
    @FXML
    public TextField nameEmployeeText;
    @FXML
    public TextField addressEmployeeText;
    @FXML
    public TextField phoneEmployeeText;
    @FXML
    public TextField salaryEmployeeText;

    @FXML
    public DatePicker startDatePicker;

    @FXML
    public ComboBox statusComboBox;
    @FXML
    public ComboBox positionComboBox;
    @FXML
    public ComboBox storeWorkingComboBox;
    @FXML
    public ComboBox storageWorkingComboBox;

    @FXML
    public Button closeButton;
    @FXML
    public Button updateButton;

    public EmployeeProfile(Stage previousStage, Controller previousController, Employee employee){
        try {
            thisStage = new Stage();
            parent = previousStage;
            this.previousController = previousController;
            this.employee = employee;

            storeDAO = new StoreDAO();
            storageDAO = new StorageDAO();
            employeeDAO = new EmployeeDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EmployeeProfile.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Hồ sơ nhân viên");
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
        EmployeeProfile reload = new EmployeeProfile(parent, previousController, this.employee);
        reload.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        // Initial value
        String[] status = {"Working","Not working"};
        String[] position = {"Employee", "Manager", "Boss"};
        List<Store> stores = storeDAO.getAllStores();
        List<Storage> storages = storageDAO.getAllStorages();

        statusComboBox.getItems().addAll(status);
        positionComboBox.getItems().addAll(position);
        storeWorkingComboBox.getItems().add("Null");
        storeWorkingComboBox.getItems().addAll(stores);
        storageWorkingComboBox.getItems().add("Null");
        storageWorkingComboBox.getItems().addAll(storages);

        nameEmployeeText.setText(this.employee.getName());
        addressEmployeeText.setText(this.employee.getAddress());
        phoneEmployeeText.setText(this.employee.getPhone());
        salaryEmployeeText.setText(Integer.toString(this.employee.getSalary()));
        startDatePicker.setValue(this.employee.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        statusComboBox.setValue((this.employee.getStatus()==1)?"Working":"Not working");
        if(this.employee.getPosition() == 0)
            positionComboBox.setValue("Employee");
        else if(this.employee.getPosition() == 1)
            positionComboBox.setValue("Manager");
        else positionComboBox.setValue("Boss");
        storeWorkingComboBox.setValue((this.employee.getStore()!=null)?this.employee.getStore():"Null");
        storageWorkingComboBox.setValue((this.employee.getStorage()!=null)?this.employee.getStorage():"Null");

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        updateButton.setOnAction(actionEvent -> {
            updateData();
        });

    }

    public void updateData(){

        String error = "";
        if(nameEmployeeText.getText().trim().equals("")
                || addressEmployeeText.getText().trim().equals("")
                || phoneEmployeeText.getText().trim().equals("")
                || salaryEmployeeText.getText().trim().equals("")
        )
            error += "All input must have value\n";

        if(!nameEmployeeText.getText().trim().matches("^[^\\d]+$"))
            error += "Name cannot have number\n";

        if (!phoneEmployeeText.getText().trim().matches("^[\\d]{10}$"))
            error += "Phone Number must have 10 digits\n";

        if(!salaryEmployeeText.getText().trim().matches("^[\\d]+$"))
            error += "Salary must be some numbers\n";

        if(!error.trim().equals("")){
            AlertDialog fail = new AlertDialog();
            Alert failAlert = fail.createAlert(thisStage,
                    "WARNING",
                    "FAIL",
                    error);
            failAlert.showAndWait();
            return;
        } else {
            int position;
            Employee temp = new Employee();
            temp.setId(this.employee.getId());
            temp.setPassword(this.employee.getPassword());
            temp.setUsername(this.employee.getUsername());

            temp.setName(nameEmployeeText.getText().trim());
            temp.setAddress(addressEmployeeText.getText().trim());
            temp.setPhone(phoneEmployeeText.getText().trim());
            temp.setSalary(Integer.valueOf(salaryEmployeeText.getText().trim()));
            temp.setStartDate(java.sql.Date.valueOf(startDatePicker.getValue()));
            temp.setStatus((statusComboBox.getSelectionModel().getSelectedItem().equals("Working")?1:0));
            if(positionComboBox.getSelectionModel().getSelectedItem().equals("Employee"))
                position = 0;
            else if(positionComboBox.getSelectionModel().getSelectedItem().equals("Manager"))
                position = 1;
            else position = 2;
            temp.setPosition(position);

            if(!storeWorkingComboBox.getSelectionModel().getSelectedItem().equals("Null")){
                Store store = (Store) storeWorkingComboBox.getSelectionModel().getSelectedItem();
                temp.setStore(store);
            } else temp.setStore(null);

            if(!storageWorkingComboBox.getSelectionModel().getSelectedItem().equals("Null")){
                Storage storage = (Storage) storageWorkingComboBox.getSelectionModel().getSelectedItem();
                temp.setStorage(storage);
            } else temp.setStorage(null);

            System.out.println(temp);

            employeeDAO.updateEmployee(temp);

            AlertDialog success = new AlertDialog();
            Alert successAlert = success.createAlert(thisStage,
                    "INFORMATION",
                    "Update Success",
                    "Update employee success");
            successAlert.showAndWait();
            thisStage.close();
        }
    }
}
