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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

public class AddItem implements Controller {
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
    private HashSet<Genre> genreList;

    @FXML
    public ComboBox typeField;

    @FXML
    public TextField nameField;

    @FXML
    public ComboBox locationField;

    @FXML
    public ComboBox locationNameField;

    @FXML
    public TextField quantityField;

    @FXML
    public TextField priceField;

    @FXML
    public TextField costField;

    @FXML
    public TextField authorField;

    @FXML
    public TextField publisherField;

    @FXML
    public TextField yearField;

    @FXML
    public TextArea desField;

    @FXML Button genreButton;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    public AddItem(Stage previousStage, Controller previous){
        try {
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();
            storageDAO = new StorageDAO();
            item_bookDAO = new Item_BookDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addItem.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 800, 816));
            thisStage.setTitle("Thêm sách");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.show();
    }

    public void reloadStage() {
        AddItem reload = new AddItem(parent, previousController);
        reload.showStage();
        thisStage.close();
    }

    @FXML
    private void initialize() {
        storeList = storeDAO.getAllStore();
        storageList = storageDAO.getAllStorage();
        genreList = new HashSet<>();

        // set item type
        typeField.getItems().addAll("Thường", "Sách");
        typeField.getSelectionModel().selectFirst();
        authorField.setDisable(true);
        publisherField.setDisable(true);
        yearField.setDisable(true);
        desField.setDisable(true);
        genreButton.setDisable(true);

        // when change type -> disable some field
        typeField.setOnAction(actionEvent -> {
            String selected = (String) typeField.getSelectionModel().getSelectedItem();
            if (selected.equals("Sách")) {
                authorField.setDisable(false);
                publisherField.setDisable(false);
                yearField.setDisable(false);
                desField.setDisable(false);
                genreButton.setDisable(false);
            }
            else {
                authorField.setDisable(true);
                publisherField.setDisable(true);
                yearField.setDisable(true);
                desField.setDisable(true);
                genreButton.setDisable(true);
            }
        });

        // set location type
        locationField.getItems().addAll("Cửa hàng", "Kho");
        locationField.getSelectionModel().selectFirst();
        locationType = (String) locationField.getSelectionModel().getSelectedItem();

        // set default location
        locationNameField.getItems().addAll(storeList);
        locationNameField.getSelectionModel().selectFirst();

        locationField.setOnAction(actionEvent -> {
            locationType = (String) locationField.getSelectionModel().getSelectedItem();
            if (locationType.equals("Cửa hàng")) {
                locationNameField.getItems().clear();
                locationNameField.getItems().addAll(storeList);
            }
            else {
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
            }
        });
        priceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed
                if (!newValue.matches("\\d*")) {
                    priceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        costField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed
                if (!newValue.matches("\\d*")) {
                    costField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        yearField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed
                if (!newValue.matches("\\d*")) {
                    yearField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });



        // create then insert
        acceptButton.setOnAction(actionEvent -> {
            // get all input
            String name = nameField.getText().trim();
            String quantity = quantityField.getText();
            String price = priceField.getText();
            String cost = costField.getText();
            String author = authorField.getText();
            String publisher = publisherField.getText();
            String year = yearField.getText();
            String des = desField.getText();

            // check blank input
            if (name.isEmpty() || quantity.isEmpty() || price.isEmpty() || cost.isEmpty() ||
                    locationNameField.getSelectionModel().getSelectedIndex() == -1) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Vui lòng nhập đầy đủ thông tin.");
                failAlert.showAndWait();
            }
            // check length
            else if (name.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Tên sách không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else if (author.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Tên tác giả không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else if (publisher.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Tên nhà xuất bản không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            else if (des.length() > 255) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Mô tả không được vượt quá 255 kí tự.");
                failAlert.showAndWait();
            }
            // check if quantity != 0
            else if (Integer.parseInt(quantity) == 0) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Số lượng không được phép bằng 0.");
                failAlert.showAndWait();
            }
            else {
                // check location
                Store store = null;
                Storage storage = null;
                if (locationType.equals("Cửa hàng")) {
                    store = (Store) locationNameField.getSelectionModel().getSelectedItem();
                }
                else {
                    storage = (Storage) locationNameField.getSelectionModel().getSelectedItem();
                }

                String selected = (String) typeField.getSelectionModel().getSelectedItem();
                if (selected.equals("Sách")) {
                    // create and insert
                    item_bookDAO.insertBook(name, Integer.parseInt(quantity), Float.parseFloat(price), Float.parseFloat(cost),
                            author, publisher, Integer.parseInt(year), des, store, storage, genreList);
                }
                else {
                    item_bookDAO.insertItem(name, Integer.parseInt(quantity), Float.parseFloat(price),
                            Float.parseFloat(cost), store, storage);
                }



                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Hoàn tất",
                        "Thêm sản phẩm thành công");
                successAlert.showAndWait();


                // reload previous stage
                previousController.reloadStage();

                // close login window
                thisStage.close();
            }
        });

        // set genre for book
        genreButton.setOnAction(actionEvent -> {
            SetGenreBook controller = new SetGenreBook(thisStage, this, genreList);
            controller.showStage();
        });

        // cancel
        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });
    }

    public void setGenre(List<Genre> list) {
        genreList.clear();
        for (Genre genre: list) {
            genreList.add(genre);
        }
        genreButton.setText("Chọn thể loại (" + list.size() + "/3)");
    }
}
