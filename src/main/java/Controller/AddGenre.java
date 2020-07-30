package Controller;

import Model.Class.Employee;
import Model.Class.Genre;
import Model.DAO.GenreDAO;
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
import java.util.prefs.Preferences;

public class AddGenre {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;
    private GenreDAO genreDAO;

    @FXML
    private TextField nameField;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    public AddGenre(Stage previousStage, Controller previous) {
        try {
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            genreDAO = new GenreDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addGenre.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 541, 271));
            thisStage.setTitle("Thêm thể loại sách");
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
        AddGenre reload = new AddGenre(parent, previousController);
        reload.showStage();
        thisStage.close();
    }

    @FXML
    private void initialize() {
        acceptButton.setOnAction(actionEvent -> {
            // get all input
            String name = nameField.getText().trim();

            // check blank input
            if (name.isEmpty()) {
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
                        "Thêm thất bại",
                        "Tên thể loại không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else {
                // create and insert
                genreDAO.insert(name);
                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Hoàn tất",
                        "Thêm thể loại thành công");
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
