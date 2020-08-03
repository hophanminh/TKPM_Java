package Controller;

import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.StorageDAO;
import Model.DAO.StoreDAO;
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

public class AddStorage implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private StorageDAO storageDAO;


    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    public AddStorage(Stage previousStage, Controller previous){
        try {
            // create new stage, after finished use "Controller previous" to reload previous stage
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            storageDAO = new StorageDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addStorage.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 541, 263));
            thisStage.setTitle("Thêm kho");
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
        AddStorage reload = new AddStorage(parent, previousController);
        reload.showStage();
        thisStage.close();
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
                        "Tên kho không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else if (address.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Tạo thất bại",
                        "Địa chỉ của kho không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else {
                // create and insert
                Storage storage = new Storage(name, address);
                storageDAO.insert(storage);
                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Hoàn tất",
                        "Thêm kho thành công");
                successAlert.showAndWait();

                nameField.clear();
                addressField.clear();
            }
        });

        closeButton.setOnAction(actionEvent -> {
            // reload previous stage
            previousController.reloadStage();
            thisStage.close();
        });
    }
}
