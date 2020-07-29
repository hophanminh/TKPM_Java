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
    private Controller previousController;
    private Preferences pref;

    @FXML
    public TextField textGenre;
    @FXML
    public Button submitButton;

    public AddGenre(Stage previousStage, Controller previous) {
        try {
            thisStage = new Stage();
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addGenre.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 668, 592));
            thisStage.setTitle("Thêm loại sách");
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
        AddItem reload = new AddItem(thisStage, previousController);
        reload.showStage();
    }

    @FXML
    private void initialize() {
        submitButton.setOnAction(actionEvent -> {
            System.out.println("Add Genre");

            String genreName = textGenre.getText().trim();

            // Kiem tra dieu kien
            String error = "";
            if (textGenre == null)
                error += "Phải nhập tên loại sách\n";

            if (error.equals("")) {
                GenreDAO genreDAO = new GenreDAO();
                Genre genre = new Genre(genreName);
                genreDAO.addGenre(genre);
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Success",
                        "Add new genre successfully");
                successAlert.showAndWait();
                System.out.println("Success");
            } else {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Sai cú pháp", error);
                failAlert.showAndWait();

                textGenre.clear();
            }
        });

    }
}
