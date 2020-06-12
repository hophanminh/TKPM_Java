package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
            Parent view = FXMLLoader.load(getClass().getResource("/fxml/warnings.fxml"));
            Scene scene = new Scene(view, 200,200);

            Stage window = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }
}
