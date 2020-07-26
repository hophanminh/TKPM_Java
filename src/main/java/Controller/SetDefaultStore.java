package Controller;

import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Store;
import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class SetDefaultStore implements Controller {
    private Stage thisStage;
    private StoreDAO storeDAO;
    private Preferences pref;

    @FXML
    public ComboBox comboBox;

    @FXML
    public Button addButton;

    @FXML
    public Button acceptButton;

    public SetDefaultStore(Stage stage) {
        try {
            thisStage = stage;
            storeDAO = new StoreDAO();
            pref = Preferences.userNodeForPackage(Employee.class);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setDefaultStore.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 392, 184));
            thisStage.setTitle("Cài đặt cửa hàng");
            thisStage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.show();
    }

    public void reloadStage() {
        SetDefaultStore reload = new SetDefaultStore(thisStage);
        reload.showStage();
    }

    @FXML
    private void initialize() {
        // get all store from database into combobox
        List<Store> list = storeDAO.getAllStore();
        comboBox.getItems().addAll(list);

        // default select first option
        if (!comboBox.getItems().isEmpty()) {
            comboBox.getSelectionModel().selectFirst();
        }

        acceptButton.setOnAction(actionEvent -> {
            if (!comboBox.getItems().isEmpty()) {
                Store selected = (Store) comboBox.getSelectionModel().getSelectedItem();
                pref.putInt("defaultStore", selected.getIdStore());

                // go to Main
                MainController controller1 = new MainController(thisStage);
                controller1.showStage();
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
        });

    }
}
