package View;
import Controller.*;

import Model.Class.Employee;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MyMenuView {
    private Preferences pref;

    // Create Menu bar for program
    public void createMenu(MenuBar menuBar, Stage currentStage, Controller currentController){
        pref = Preferences.userNodeForPackage(Employee.class);

        // view menu
        Menu viewMenu = new Menu("Xem");
                // Return to home page
        MenuItem viewMenuItem1 = new MenuItem("Trang chủ");
        viewMenuItem1.setOnAction(e -> {
            MainController controller = new MainController(currentStage);
            controller.showStage();
        });
        viewMenu.getItems().add(viewMenuItem1);

        // account menu
        Menu accountMenu = new Menu("Tài khoản");
                // account's info
        MenuItem accMenuItem1 = new MenuItem("Thông tin cá nhân");
        accMenuItem1.setOnAction(e -> {

        });
                // sign out
        MenuItem accMenuItem2 = new MenuItem("Đăng xuất");
        accMenuItem2.setOnAction(e -> {
            try {
                // clear user's info
                int layout = pref.getInt("defaultStore", -1);
                pref.clear();
                pref.putInt("defaultStore", layout);

                Login controller = new Login(currentStage);
                controller.showStage();
            } catch (BackingStoreException backingStoreException) {
                backingStoreException.printStackTrace();
            }
        });
        accountMenu.getItems().addAll(accMenuItem1, accMenuItem2);


        // management menu
        Menu manageMenu = new Menu("Quản lý");
                // management
        MenuItem manageMenuItem1 = new MenuItem("Quản lý chung");
        manageMenuItem1.setOnAction(e -> {

        });
        manageMenu.getItems().addAll(manageMenuItem1);


        menuBar.getMenus().addAll(viewMenu, accountMenu, manageMenu);
    }
}
