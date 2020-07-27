package Controller;

import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AddStore implements Controller {
    private Stage thisStage;
    private Controller previousController;
    private StoreDAO storeDAO;


    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    public AddStore(Stage previousStage, Controller previous){
        try {
            // create new stage, after finished use "Controller previous" to reload previous stage
            thisStage = new Stage();
            previousController = previous;
            storeDAO = new StoreDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addStore.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 541, 263));
            thisStage.setTitle("Thêm cửa hàng");
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
        AddStore reload = new AddStore(thisStage, previousController);
        reload.showStage();
    }

    @FXML
    private void initialize() {
        acceptButton.setOnAction(actionEvent -> {
            // get all input
            String name = nameField.getText().trim();
            String address = addressField.getText().trim();

            // check blank input
            if (name.isEmpty() || address.isEmpty()) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Vui lòng nhập đầy đủ thông tin.");
                failAlert.showAndWait();
            }
            // check length
            else if (name.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Tạo thất bại",
                        "Tên cửa hàng không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else if (address.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Tạo thất bại",
                        "Địa chỉ của cửa hàng không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else {
                // create and insert
                storeDAO.insert(name, address);
                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Hoàn tất",
                        "Thêm cửa hàng thành công");
                successAlert.showAndWait();

                // reload previous stage
                previousController.reloadStage();

                // close login window
                thisStage.close();
            }
        });

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });
    }
}
