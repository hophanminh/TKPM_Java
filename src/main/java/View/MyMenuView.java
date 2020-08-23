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
        accountMenu.getItems().addAll(accMenuItem2);


        // management menu
        Menu manageMenu = new Menu("Quản lý");
        // management store/storage
        MenuItem manageMenuItem1 = new MenuItem("Quản lý cửa hàng");
        manageMenuItem1.setOnAction(e -> {
            int defaultStore = pref.getInt("defaultStore", -1);
            StoreStorageManagement controller = new StoreStorageManagement(currentStage, currentController);
            controller.showStage();
            if (defaultStore != pref.getInt("defaultStore", -1)) {
                currentController.reloadStage();
            }
        });
        // management item/customer/employee
        MenuItem manageMenuItem2 = new MenuItem("Quản lý sản phẩm");
        manageMenuItem2.setOnAction(e -> {
            Management management = new Management(currentStage, currentController);
            management.showStage();
        });
        // see report chart
        MenuItem manageMenuItem3 = new MenuItem("Báo cáo doanh thu");
        manageMenuItem3.setOnAction(e -> {
            Report report = new Report(currentStage, currentController);
            report.showStage();
        });
        // see bill list
        MenuItem manageMenuItem4 = new MenuItem("Lịch sử bán");
        manageMenuItem4.setOnAction(e -> {
            ViewBill viewBill = new ViewBill(currentStage, currentController, pref.getInt("defaultStore", -1));
            viewBill.showStage();
        });

        if (pref.getInt("position", 0) != 2) {              // boss
            manageMenu.getItems().addAll(manageMenuItem2, manageMenuItem3);
        }
        else {
            manageMenu.getItems().addAll(manageMenuItem1, manageMenuItem2, manageMenuItem3, manageMenuItem4);
        }


        menuBar.getMenus().addAll(viewMenu, accountMenu, manageMenu);
    }
}
