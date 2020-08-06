package Controller;

import Controller.Controller;
import Model.Class.Employee;
import Model.Class.Genre;
import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.StorageDAO;
import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;

public class ViewStorageOfStore {
    private Stage thisStage;
    private Stage parentStage;
    private Controller previousController;
    private StoreDAO storeDAO;
    private StorageDAO storageDAO;
    private Preferences pref;

    private int selectedStore;
    private Store store;

    private TableView tableView;
    private List<Storage> storageOfStoreList;
    private List<Storage> allStorageList;

    @FXML
    private Label nameField;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    public ViewStorageOfStore(Stage previousStage, Controller previous, int idStore){
        try {
            thisStage = new Stage();
            parentStage = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();
            storageDAO = new StorageDAO();

            selectedStore = idStore;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewStorageOfStore.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Danh sách kho liên kết");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(previousStage);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.show();
    }

    @FXML
    private void initialize() {

        // get default store
        store = storeDAO.getStoreWithStorageByID(selectedStore);
        nameField.setText(store.getNameStore());

        // get storage of store
        storageOfStoreList = new ArrayList<>();
        storageOfStoreList.addAll(store.getStorageList());

        // get all storage in system or only storage of selected store
        if (pref.getInt("position", -1) == 2) {
            allStorageList = storageDAO.getAllStorages();
            tableView = createTableView(allStorageList);
        }
        else {
            tableView = createTableView(storageOfStoreList);
        }


        // create table

        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        borderPane.setCenter(null);
        borderPane.setCenter(scroll);

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        // disable edit if not admin
        if (pref.getInt("position", -1) != 2) {
            // hide 'accept' and 'accept all' button when not admin
            acceptButton.setVisible(false);
            acceptButton.managedProperty().bind(acceptButton.visibleProperty());
            // change text of 'close' button from 'Hủy bỏ' -> 'Đóng'
            closeButton.setText("Đóng");
        }

        // create action for button
        acceptButton.setOnAction(actionEvent -> {
            // update store
            storeDAO.update(store);

            // Create and display AlertWindow
            AlertDialog success = new AlertDialog();
            Alert successAlert = success.createAlert(thisStage,
                    "INFORMATION",
                    "Hoàn tất",
                    "Thêm liên kết kho hoàn tấ");
            successAlert.showAndWait();

            // reload previous stage
            previousController.reloadStage();

            // close this stage
            thisStage.close();

        });

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });
    }

    private TableView createTableView(List<Storage> list){
        TableView newTable = new TableView();

        TableColumn idColumn = new TableColumn("ID");
        idColumn.setStyle( "-fx-alignment: CENTER;");
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(80);
        Callback<TableColumn<Storage, String>, TableCell<Storage, String>> cellFactory
                = //
                new Callback<TableColumn<Storage, String>, TableCell<Storage, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Storage, String> param)
                    {
                        final TableCell<Storage, String> cell = new TableCell<Storage, String>()
                        {
                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(null);
                                    Storage selected = (Storage) newTable.getItems().get(getIndex());
                                    String result = Integer.toString(selected.getIdStorage());
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        idColumn.setCellFactory(cellFactory);


        TableColumn nameColumn = new TableColumn("Tên kho");
        nameColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Storage, String>, TableCell<Storage, String>> cellFactory2
                = //
                new Callback<TableColumn<Storage, String>, TableCell<Storage, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Storage, String> param)
                    {
                        final TableCell<Storage, String> cell = new TableCell<Storage, String>()
                        {
                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(null);
                                    Storage selected = (Storage) newTable.getItems().get(getIndex());
                                    String result = selected.getNameStorage();
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        nameColumn.setCellFactory(cellFactory2);

        TableColumn locationColumn = new TableColumn("Địa điểm");
        locationColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Storage, String>, TableCell<Storage, String>> cellFactory3
                = //
                new Callback<TableColumn<Storage, String>, TableCell<Storage, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Storage, String> param)
                    {
                        final TableCell<Storage, String> cell = new TableCell<Storage, String>()
                        {
                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(null);
                                    Storage selected = (Storage) newTable.getItems().get(getIndex());
                                    String result = selected.getAddressStorage();
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        locationColumn.setCellFactory(cellFactory3);

        TableColumn statusColumn = new TableColumn("Liên kết");
        statusColumn.setMinWidth(50);
        statusColumn.setMaxWidth(50);
        statusColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Storage, String>, TableCell<Storage, String>> cellFactory4
                = //
                new Callback<TableColumn<Storage, String>, TableCell<Storage, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Storage, String> param)
                    {
                        final TableCell<Storage, String> cell = new TableCell<Storage, String>()
                        {
                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final CheckBox check = new CheckBox();
                                {
                                    check.setOnAction(event -> {
                                        if (check.isSelected()) {
                                            Storage selected = (Storage) newTable.getItems().get(getIndex());
                                            store.getStorageList().add(selected);
                                        }
                                        else {
                                            Storage selected = (Storage) newTable.getItems().get(getIndex());
                                            store.getStorageList().remove(selected);
                                        }
                                    });
                                }

                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(check);
                                    setText(null);

                                    // initial status of checkbox
                                    Storage selected = (Storage) newTable.getItems().get(getIndex());
                                    if (storageOfStoreList.contains(selected)) {
                                        check.setSelected(true);
                                    }
                                    else {
                                        check.setSelected(false);
                                    }
                                }
                            }
                        };
                        return cell;
                    }
                };
        statusColumn.setCellFactory(cellFactory4);

        TableColumn[] columns = {idColumn, nameColumn, locationColumn};
        newTable.getColumns().addAll(columns);

        /// only editable when user is boss
        if (pref.getInt("position", -1) == 2) {
            newTable.getColumns().add(statusColumn);
        }

        // make column not draggable
        newTable.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    newTable.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
        newTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // add list
        newTable.getItems().addAll(list);
        return newTable;
    }
}
