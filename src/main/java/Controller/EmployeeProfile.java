package Controller;

import Model.Class.Employee;
import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.EmployeeDAO;
import Model.DAO.StorageDAO;
import Model.DAO.StoreDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
    public ChoiceBox statusChoiceBox;
    @FXML
    public ChoiceBox positionChoiceBox;
    @FXML
    public ChoiceBox storeWorkingChoiceBox;
    @FXML
    public ChoiceBox storageWorkingChoiceBox;

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

        statusChoiceBox.getItems().addAll(status);
        positionChoiceBox.getItems().addAll(position);
        storeWorkingChoiceBox.getItems().add("Null");
        storeWorkingChoiceBox.getItems().addAll(stores);
        storageWorkingChoiceBox.getItems().add("Null");
        storageWorkingChoiceBox.getItems().addAll(storages);

        nameEmployeeText.setText(this.employee.getName());
        addressEmployeeText.setText(this.employee.getAddress());
        phoneEmployeeText.setText(this.employee.getPhone());
        salaryEmployeeText.setText(Integer.toString(this.employee.getSalary()));
        startDatePicker.setValue(this.employee.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        updateButton.setOnAction(actionEvent -> {
            updateData();
        });

    }

    public void updateData(){
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
        temp.setStatus((statusChoiceBox.getSelectionModel().getSelectedItem().equals("Working")?1:0));
        if(positionChoiceBox.getSelectionModel().getSelectedItem().equals("Employee"))
            position = 0;
        else if(positionChoiceBox.getSelectionModel().getSelectedItem().equals("Manager"))
            position = 1;
        else position = 2;
        temp.setPosition(position);

        if(!storeWorkingChoiceBox.getSelectionModel().getSelectedItem().equals("Null")){
            Store store = (Store) storeWorkingChoiceBox.getSelectionModel().getSelectedItem();
            temp.setStore(store);
        } else temp.setStore(null);

        if(!storageWorkingChoiceBox.getSelectionModel().getSelectedItem().equals("Null")){
            Storage storage = (Storage) storageWorkingChoiceBox.getSelectionModel().getSelectedItem();
            temp.setStorage(storage);
        } else temp.setStorage(null);

        System.out.println(temp);
        employeeDAO.updateEmployee(temp);
        thisStage.close();
    }

}
