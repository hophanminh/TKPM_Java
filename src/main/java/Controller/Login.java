package Controller;

import Model.Class.Employee;
import Model.Class.Store;
import Model.DAO.EmployeeDAO;

import View.AlertDialog;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Login {
    private Stage thisStage;
    private Preferences pref;
    private EmployeeDAO employeeDAO;

    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button loginButton;


    public Login(Stage stage){
        try {
            thisStage = stage;
            pref = Preferences.userNodeForPackage(Employee.class);
            employeeDAO = new EmployeeDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 400, 200));
            thisStage.setTitle("Đăng nhập");
            thisStage.setResizable(false);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        // check if app has default store
        thisStage.show();
        if (pref.getInt("defaultStore", -1) == -1) {
            // Create and display AlertWindow
            AlertDialog fail = new AlertDialog();
            Alert failAlert = fail.createAlert(thisStage,
                    "WARNING",
                    "Không tìm thấy cửa hàng",
                    "Chưa cài đặt cửa hàng mặc định cho phần mềm. Vui lòng đăng nhập bằng tài khoản admin để cài đặt.");
            failAlert.showAndWait();
        }
    }


    @FXML
    private void initialize() {
        loginButton.setOnAction(actionEvent -> {

            //loginButton.setText("Welcome");
            String username = usernameField.getText().trim();
            String pass = passwordField.getText().trim();
            System.out.println("ID: " + username);
            System.out.println("Password: " + pass);

            Employee loginUser = employeeDAO.getEmployeeByUsername(username);
            // no username in database or wrong password
            //if (loginUser == null || !BCrypt.checkpw(pass, loginUser.getPassword())) {    // when have signup -> bcrypt
            if (loginUser == null || !loginUser.getPassword().equals(pass)) {
                System.out.println("Wrong id or password");

                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Đăng nhập thất bại",
                        "Sai tên đăng nhập hay mật khẩu.");
                failAlert.showAndWait();

                // clear text field
                usernameField.clear();
                passwordField.clear();
            }
            // check default store and boss account
            else if (pref.getInt("defaultStore", -1) == -1 && loginUser.getPosition() != 1) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Không tìm thấy cửa hàng",
                        "Chưa cài đặt cửa hàng mặc định cho phần mềm. Vui lòng đăng nhập bằng tài khoản admin để cài đặt.");
                failAlert.showAndWait();

                // clear text field
                usernameField.clear();
                passwordField.clear();
            }
            else {
                System.out.println("Login...");

                // save data into preference
                pref.putInt("ID", loginUser.getId());
                pref.put("username", loginUser.getUsername());
                pref.put("name", loginUser.getName());
                pref.put("phone", loginUser.getPhone());
                pref.put("address", loginUser.getAddress());
                pref.putInt("salary", loginUser.getSalary());
                pref.putInt("position", loginUser.getPosition());
                pref.putInt("status", loginUser.getStatus());

                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Đăng nhập thành công",
                        "Xin chào " + loginUser.getName());
                successAlert.showAndWait();

                // check default store
                if (pref.getInt("defaultStore", -1) == -1) {
                    SetDefaultStore controller1 = new SetDefaultStore(thisStage);
                    controller1.showStage();
                }
                else {
                    // go to Main
                    MainController controller1 = new MainController(thisStage);
                    controller1.showStage();
                }
            }
        });

        // enter event when input
        usernameField.setOnAction(actionEvent -> {
            loginButton.fire();
        });

        passwordField.setOnAction(actionEvent -> {
            loginButton.fire();
        });

    }


}
