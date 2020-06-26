package Controller;

import Class.Employee;
import DAO.EmployeeDAO;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class login {
    @FXML
    public TextField idEmployee;
    @FXML
    public PasswordField passwordEmployee;
    @FXML
    public Button LoginButton;

    private EmployeeDAO employeeDAO;

    public void login(ActionEvent actionEvent) throws IOException {
        employeeDAO = new EmployeeDAO();

        //loginButton.setText("Welcome");
        String id = idEmployee.getText();
        String pass = passwordEmployee.getText();
        System.out.println("ID: "+ id);
        System.out.println("Password: " + pass);

        List<Employee> list = this.employeeDAO.getListEmployee();
        if(list.contains(id) && this.employeeDAO.getPassword(id).equals(pass) ){
            System.out.println("Login...");

            // go to main
            Stage main = (Stage) idEmployee.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
            main.setScene(new Scene(root, 668, 592));
            main.show();

        } else {
            System.out.println("Wrong id or password");
            //Stage loginWindow = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();  // set owner
            Stage loginWindow = (Stage) idEmployee.getScene().getWindow(); // set owner

            // Create and display Alert
            Alert loginFail = new Alert(Alert.AlertType.WARNING);
            loginFail.setTitle("Login failed");
            loginFail.setHeaderText(null);
            loginFail.setContentText("Wrong Id or Password. Please try again");

            // Lock alert
            loginFail.initOwner(loginWindow);
            loginFail.initModality(Modality.WINDOW_MODAL);

            loginFail.showAndWait();

            // clear text field
            idEmployee.clear();
            passwordEmployee.clear();
        }
    }

    public void onEnter(ActionEvent actionEvent) {
        LoginButton.fire();
    }
}
