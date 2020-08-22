package Controller;

import Model.Class.*;
import Model.DAO.*;
import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.PrinterPDF;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class StoreStorageManagement implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;

    private StorageDAO storageDAO;
    private StoreDAO storeDAO;

    private List<Store> storeList;
    private List<Storage> storageList;

    private TableView tableView;
    private String type;
    private ScrollPane scroll;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ComboBox typeField;

    @FXML
    private ComboBox ieComboBox;

    @FXML
    private Button backToMainButton;

    @FXML
    private Button addButton;

    @FXML
    private Button defaultButton;

    public StoreStorageManagement(Stage previousStage, Controller previous){
        try{
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);

            storageDAO = new StorageDAO();
            storeDAO = new StoreDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/StoreStorageManagement.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Quản lý");
            thisStage.setResizable(false);
//            thisStage.setMaximized(true);

            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage(){thisStage.showAndWait();}

    public void reloadStage(){
    }

    @FXML
    public void initialize(){
        storeList = storeDAO.getAllStores();
        storageList = storageDAO.getAllStorages();

        // set location type
        typeField.getItems().addAll("Cửa hàng", "Kho");
        typeField.getSelectionModel().selectFirst();
        type = "Cửa hàng";

        typeField.setOnAction(actionEvent -> {
            type = (String) typeField.getSelectionModel().getSelectedItem();
            // create table
            if (type.equals("Cửa hàng")) {
                createTableViewStore(storeList);
            }
            else {
                createTableViewStorage(storageList);
            }
        });

        //default
        tableView = new TableView();
        if (type.equals("Cửa hàng")) {
            createTableViewStore(storeList);
        }
        else {
            createTableViewStorage(storageList);
        }
        scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        borderPane.setCenter(null);
        borderPane.setCenter(scroll);



        backToMainButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        addButton.setOnAction(actionEvent -> {
            System.out.println("Adding...");
            if(type.equals("Cửa hàng")){
                AddStore controller = new AddStore(thisStage, this);
                controller.showStage();
                storeList = storeDAO.getAllStores();
                tableView.getItems().clear();
                tableView.getItems().addAll(storeList);
            }
            else {
                AddStorage controller = new AddStorage(thisStage, this);
                controller.showStage();
                storageList = storageDAO.getAllStorages();
                tableView.getItems().clear();
                tableView.getItems().addAll(storageList);
            }
        });

        defaultButton.setOnAction(actionEvent -> {
            SetDefaultStore controller = new SetDefaultStore(thisStage, this);
            controller.showStage();
        });
    }

    private void createTableViewStorage(List<Storage> list){
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idStorage"));
        idColumn.setStyle( "-fx-alignment: CENTER;");
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(80);


        TableColumn nameColumn = new TableColumn("Tên kho");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameStorage"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn locationColumn = new TableColumn("Địa điểm");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("addressStorage"));
        locationColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn[] columns = {idColumn, nameColumn, locationColumn};
        tableView.getColumns().addAll(columns);

        // make column not draggable
        tableView.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableView.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // add list
        tableView.getItems().addAll(list);
    }

    private void createTableViewStore(List<Store> list){
        tableView.getItems().clear();
        tableView.getColumns().clear();

        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idStore"));
        idColumn.setStyle( "-fx-alignment: CENTER;");
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(80);

        TableColumn nameColumn = new TableColumn("Tên cửa hàng");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameStore"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn locationColumn = new TableColumn("Địa điểm");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("addressStore"));
        locationColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn storageColumn = new TableColumn("Kho liên kết");
        storageColumn.setStyle( "-fx-alignment: CENTER;");
        storageColumn.setMinWidth(120);
        storageColumn.setMaxWidth(120);
        Callback<TableColumn<Store, String>, TableCell<Store, String>> cellFactory
                = //
                new Callback<TableColumn<Store, String>, TableCell<Store, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Store, String> param)
                    {
                        final TableCell<Store, String> cell = new TableCell<Store, String>()
                        {

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final Button allowButton = new Button("Xem danh sách");
                                {
                                    allowButton.setOnAction(event -> {
                                        Store selected = (Store) tableView.getItems().get(getIndex());
                                        openViewStorageOfStore(selected.getIdStore());
                                    });
                                }
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(allowButton);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        storageColumn.setCellFactory(cellFactory);

        TableColumn[] columns = {idColumn, nameColumn, locationColumn, storageColumn};
        tableView.getColumns().addAll(columns);

        // make column not draggable
        tableView.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableView.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // add list
        tableView.getItems().addAll(list);
    }

    private void openViewStorageOfStore(int idStore) {
        // switch stage
        ViewStorageOfStore controller2 = new ViewStorageOfStore(thisStage, this, idStore);
        controller2.showStage();
    }
}
