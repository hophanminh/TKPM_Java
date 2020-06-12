package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class login {

    public TextField idEmployee;
    public PasswordField passwordEmployee;
    public Button LoginButton;

    public void login(ActionEvent actionEvent) throws IOException {
        //loginButton.setText("Welcome");
        String id = idEmployee.getText();
        String pass = passwordEmployee.getText();
        System.out.println("ID: "+ id);
        System.out.println("Password: " + pass);

        if(id.equals("hophanminh") && pass.equals("hophanminh1")) {
            System.out.println("Login...");
        } else {
            System.out.println("Wrong id or password");

            // Create and display Alert
            Alert loginFail = new Alert(Alert.AlertType.WARNING);
            loginFail.setTitle("Login failed");
            loginFail.setHeaderText(null);
            loginFail.setContentText("Wrong Id or Password. Please try again");
            Stage loginWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();  // set owner
            loginFail.initOwner(loginWindow);
            loginFail.initModality(Modality.WINDOW_MODAL);

            loginFail.showAndWait();

            // clear text field
            idEmployee.clear();
            passwordEmployee.clear();
        }
    }
}
