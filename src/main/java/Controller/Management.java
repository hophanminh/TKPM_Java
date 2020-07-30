package Controller;

import Model.Class.Employee;
import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class Management {
    private Stage thisStage;
    private Stage parent;
    private Preferences pref;
    private GenreDAO genreDAO;

    private EmployeeDAO employeeDAO;
    private StorageDAO storageDAO;
    private StoreDAO storeDAO;
    private Item_BookDAO item_bookDAO;

    @FXML
    public TableView tableView;

    @FXML
    public ChoiceBox ssChoiceBox;
    @FXML
    public ChoiceBox ieChoiceBox;

    @FXML
    public TextField searchText;

    @FXML
    public Button searchButton;
    @FXML
    public Button printButton;
    @FXML
    public Button backToMainButton;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button updateButton;

    public Management(Stage previousStage){
        try{
            thisStage = previousStage;
            pref = Preferences.userNodeForPackage(Employee.class);
            employeeDAO = new EmployeeDAO();
            storageDAO = new StorageDAO();
            storeDAO = new StoreDAO();
            item_bookDAO = new Item_BookDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Management.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 1200, 1000));
            thisStage.setTitle("Management");
            thisStage.setResizable(false);
            thisStage.setMaximized(true);


            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            thisStage.setX(bounds.getMinX());
            thisStage.setY(bounds.getMinY());
            thisStage.setWidth(bounds.getWidth());
            thisStage.setHeight(bounds.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage(){thisStage.show();}

    public void reloadStage(){
        Management reloadManagement = new Management(thisStage);
        reloadManagement.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        List<Store> storeList = storeDAO.getAllStore();
        List<Storage> storageList = storageDAO.getAllStorage();
        for (Store store: storeList){
            ssChoiceBox.getItems().add(store.getNameStore());
        }
        for(Storage storage: storageList){
            ssChoiceBox.getItems().add(storage.getNameStorage());
        }

        ssChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)->{
            System.out.println(newValue);
        });

        String[] ieChoiceList = {"Items", "Employees"};
        ieChoiceBox.getItems().addAll(ieChoiceList);
        ieChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newVlue)->{
            System.out.println(newVlue);
        });

        backToMainButton.setOnAction(actionEvent -> {
            MainController mainController = new MainController(thisStage);
            mainController.showStage();
        });

        addButton.setOnAction(actionEvent -> {
            System.out.println("Adding...");
        });

        deleteButton.setOnAction(actionEvent -> {
            System.out.println("Deleting...");
        });

        updateButton.setOnAction(actionEvent -> {
            System.out.println("Updating...");
        });
    }

}
