package Controller;

import Model.Class.*;
import Model.DAO.Item_BookDAO;
import Model.DAO.StorageDAO;
import Model.DAO.StoreDAO;
import View.AlertDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

public class TransferItem implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;
    private Item_BookDAO item_bookDAO;
    private StoreDAO storeDAO;
    private StorageDAO storageDAO;

    private String locationType;

    private List<Store> storeList;
    private List<Storage> storageList;
    private Store fromStore;
    private Storage fromStorage;
    private Item item;
    private int max;

    @FXML
    private Label nameLabel;

    @FXML
    private Label maxLabel;

    @FXML
    private ComboBox locationField;

    @FXML
    private ComboBox locationNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    public TransferItem(Stage previousStage, Controller previous, Item item, Store store, Storage storage ,int max){
        try {
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();
            storageDAO = new StorageDAO();
            item_bookDAO = new Item_BookDAO();

            this.item = item;
            fromStore = store;
            fromStorage = storage;
            this.max = max;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transferItem.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Chuyển sản phẩm");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public void reloadStage() {
    }

    @FXML
    private void initialize() {
        nameLabel.setText(item.getNameItem());
        maxLabel.setText("Tối đa: " + max);
        // set location type
        if (fromStore != null) {
            locationField.getItems().addAll("Cửa hàng", "Kho liên kết");
            storeList = storeDAO.getAllStoresExceptOne(fromStore.getIdStore());             // get all store but itself
            storageList = storageDAO.getStorageByStore(fromStore.getIdStore());
        }
        else {
            locationField.getItems().addAll("Cửa hàng liên kết", "Kho");
            storeList = storeDAO.getStoreByStorage(fromStorage.getIdStorage());
            storageList = storageDAO.getAllStoragesExceptOne(fromStorage.getIdStorage());   // get all storage but itself
        }
        locationField.getSelectionModel().selectFirst();
        locationType = (String) locationField.getSelectionModel().getSelectedItem();

        // set default location
        locationNameField.getItems().addAll(storeList);
        locationNameField.getSelectionModel().selectFirst();

        // add store list or storage list depends on comboxbox 1
        locationField.setOnAction(actionEvent -> {
            locationType = (String) locationField.getSelectionModel().getSelectedItem();
            if (locationType.equals("Cửa hàng") || locationType.equals("Cửa hàng liên kết")) {
                locationNameField.getItems().clear();
                locationNameField.getItems().addAll(storeList);
            }
            else if (locationType.equals("Kho") || locationType.equals("Kho liên kết")) {
                locationNameField.getItems().clear();
                locationNameField.getItems().addAll(storageList);
            }
            locationNameField.getSelectionModel().selectFirst();
        });

        // set quantity, price, cost ,year field to allow only number
        quantityField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed
                if (!newValue.matches("\\d*")) {
                    quantityField.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.matches("\\d*") && !newValue.isEmpty() && Integer.parseInt(newValue) > max) {
                    quantityField.setText(Integer.toString(max));
                }
                if (newValue.matches("\\d*") && !newValue.isEmpty() && Integer.parseInt(newValue) == 0) {
                    quantityField.setText("1");
                }
            }
        });

        acceptButton.setOnAction(actionEvent -> {
            // get all input
            String quantity = quantityField.getText();

            // check blank input
            if (quantity.isEmpty()) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Chuyển thất bại",
                        "Vui lòng nhập đầy đủ thông tin.");
                failAlert.showAndWait();
            }
            // check if quantity != 0
            else if (Integer.parseInt(quantity) == 0) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Chuyển thất bại",
                        "Số lượng không được phép bằng 0.");
                failAlert.showAndWait();
            }
            else {
                // check location
                Store toStore = null;
                Storage toStorage = null;
                if (locationType.equals("Cửa hàng") || locationType.equals("Cửa hàng liên kết")) {
                    toStore = (Store) locationNameField.getSelectionModel().getSelectedItem();
                }
                else {
                    toStorage = (Storage) locationNameField.getSelectionModel().getSelectedItem();
                }

                // transfer
                int count = Integer.parseInt(quantity);
                if (fromStore != null && toStore != null) {             //store -> store
                    item_bookDAO.transferStoreToStore(item, fromStore, toStore, count);
                }
                else if (fromStore != null && toStorage != null) {
                    item_bookDAO.transferStoreToStorage(item, fromStore, toStorage, count);
                }
                else if (fromStorage != null && toStore != null) {
                    item_bookDAO.transferStorageToStore(item, fromStorage, toStore, count);
                }
                else if (fromStorage != null && toStorage != null) {
                    item_bookDAO.transferStorageToStorage(item, fromStorage, toStorage, count);
                }


                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Hoàn tất",
                        "Thêm sản phẩm thành công");
                successAlert.showAndWait();
                thisStage.close();
            }
        });


        // cancel
        closeButton.setOnAction(actionEvent -> {
            // reload previous stage
            thisStage.close();
        });
    }
}
