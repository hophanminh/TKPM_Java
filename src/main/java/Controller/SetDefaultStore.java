package Controller;

import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Store;
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
import java.util.prefs.Preferences;

public class SetDefaultStore implements Controller {
    private Stage thisStage;
    private Stage parentStage;
    private Controller previousController;
    private StoreDAO storeDAO;
    private Preferences pref;

    @FXML
    public Label nameField;

    @FXML
    public ComboBox comboBox;

    @FXML
    public Button addButton;

    @FXML
    public Button acceptButton;

    public SetDefaultStore(Stage previousStage, Controller previous) {
        try {
            // keep stage, change scene
            thisStage = new Stage();
            parentStage = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setDefaultStore.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Cài đặt cửa hàng");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(previousStage);
            thisStage.initModality(Modality.WINDOW_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public void reloadStage() {
    }

    @FXML
    private void initialize() {
        // get all store from database into combobox
        List<Store> list = storeDAO.getAllStores();
        comboBox.getItems().addAll(list);


        // default select first option
        if (!comboBox.getItems().isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
        }

        int current = pref.getInt("defaultStore", -1);
        if (current != -1){
            Store store = storeDAO.getStoreById(current);
            nameField.setText(store.getNameStore());
            comboBox.getSelectionModel().select(store);
        };

        acceptButton.setOnAction(actionEvent -> {
            if (!comboBox.getItems().isEmpty()) {
                Store selected = (Store) comboBox.getSelectionModel().getSelectedItem();
                pref.putInt("defaultStore", selected.getIdStore());
                thisStage.close();
            }
            else {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Không tìm thấy cửa hàng",
                        "Xin chọn cửa hàng mặc định hợp lệ.");
                failAlert.showAndWait();
            }
        });

        addButton.setOnAction(actionEvent -> {
            AddStore controller = new AddStore(thisStage, this);
            controller.showStage();
            comboBox.getItems().clear();
            comboBox.getItems().addAll(storeDAO.getAllStores());
            comboBox.getSelectionModel().selectFirst();
        });

    }
}
