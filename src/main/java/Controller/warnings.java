package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;

public class warnings {
    public Button againButton;
    public CheckBox confirmCheckBox;

    public void doAgain(ActionEvent actionEvent) throws IOException {
        if (confirmCheckBox.isSelected()){
            Parent view = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(view, 400,200);
            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
}
