package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;

public class main {
    @FXML
    private ComboBox searchBar ;

    @FXML
    private void initialize() {
        searchBar.getItems().addAll("Hello", "Hello World", "Hey", "Abc");
        TextFields.bindAutoCompletion(searchBar.getEditor(), searchBar.getItems());
    }

}
