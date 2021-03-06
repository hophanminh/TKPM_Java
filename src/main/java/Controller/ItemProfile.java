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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

public class ItemProfile implements Controller {
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

    private Boolean isBook;
    private Item currentItem;
    private Store currentStore;
    private Storage currentStorage;


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

    public ItemProfile(Stage previousStage, Controller previous, Item item, Store store, Storage storage){
        try {
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();
            storageDAO = new StorageDAO();
            item_bookDAO = new Item_BookDAO();

            // check item or book
            int count = item.getQuantityItem();
            isBook = true;
            currentItem = item_bookDAO.getBookById(item.getIdItem());
            if (currentItem == null) {      // no book found
                currentItem = item_bookDAO.getItemById(item.getIdItem());
                isBook = false;
            }
            currentItem.setQuantityItem(count);

            currentStore = store;
            currentStorage = storage;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/addItem.fxml"));
            loader.setController(this);
            Scene newScene = new Scene(loader.load(), 800, 816);
            thisStage.setScene(newScene);
            thisStage.setTitle("Update item");
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
        storeList = storeDAO.getAllStores();
        storageList = storageDAO.getAllStorages();
        genreList = new HashSet<>();

        // can't change location or type of item
        locationField.setDisable(true);
        locationNameField.setDisable(true);
        typeField.setDisable(true);
        acceptButton.setText("Update");

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
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    priceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        costField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
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
            if (name.isEmpty() || quantity.isEmpty() || price.isEmpty() || cost.isEmpty()) {
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
                // create and insert
                if (selected.equals("Sách")) {
                    // check year empty
                    int yearInt = 0;
                    if (!year.isEmpty()) {
                        yearInt = Integer.parseInt(year);
                    }

                    Book currentBook = (Book) currentItem;
                    currentBook.setNameItem(name);
                    currentBook.setPriceItem(Float.parseFloat(price));
                    currentBook.setCostItem(Float.parseFloat(cost));
                    currentBook.setQuantityItem(Integer.parseInt(quantity));
                    currentBook.setAuthorBook(author);
                    currentBook.setDescriptionBook(des);
                    currentBook.setPulisherBook(publisher);
                    currentBook.setYear(yearInt);
                    currentBook.setGenreList(genreList);

                    item_bookDAO.updateBook(currentBook, currentStore, currentStorage);
                }
                else {
                    currentItem.setNameItem(name);
                    currentItem.setPriceItem(Float.parseFloat(price));
                    currentItem.setCostItem(Float.parseFloat(cost));
                    currentItem.setQuantityItem(Integer.parseInt(quantity));
                    item_bookDAO.updateItem(currentItem, currentStore, currentStorage);
                }



                // Create and display AlertWindow
                AlertDialog success = new AlertDialog();
                Alert successAlert = success.createAlert(thisStage,
                        "INFORMATION",
                        "Hoàn tất",
                        "Cập nhập sản phẩm thành công");
                successAlert.showAndWait();

                // reload previous stage
                thisStage.close();
            }
        });

        // set genre for book
        genreButton.setOnAction(actionEvent -> {
            SetGenreBook controller = new SetGenreBook(thisStage, this, genreList, "update");
            controller.showStage();
        });

        // cancel
        closeButton.setOnAction(actionEvent -> {
            // reload previous stage
            thisStage.close();
        });

        // get all input
        nameField.setText(currentItem.getNameItem());
        quantityField.setText(Integer.toString(currentItem.getQuantityItem()));
        priceField.setText(Float.toString(currentItem.getPriceItem()));
        costField.setText(Float.toString(currentItem.getCostItem()));
        if (currentStore != null) {
            locationType = "Cửa hàng";
            locationField.getSelectionModel().select(0);

            locationNameField.getItems().clear();
            locationNameField.getItems().addAll(storeList);
            locationNameField.getSelectionModel().select(currentStore);
        }
        else {
            locationType= "Kho";
            locationField.getSelectionModel().select(1);

            locationNameField.getItems().clear();
            locationNameField.getItems().addAll(storageList);
            locationNameField.getSelectionModel().select(currentStorage);
        }

        if (isBook) {
            authorField.setDisable(false);
            publisherField.setDisable(false);
            yearField.setDisable(false);
            desField.setDisable(false);
            genreButton.setDisable(false);

            typeField.getSelectionModel().select(1);
            authorField.setText( ((Book)currentItem).getAuthorBook());
            publisherField.setText( ((Book)currentItem).getPulisherBook());
            yearField.setText( Integer.toString(
                    ((Book)currentItem).getYear())
            );
            List<Genre> tempList = new ArrayList<>();
            tempList.addAll(((Book) currentItem).getGenreList());
            setGenre(tempList);
            desField.setText( ((Book)currentItem).getDescriptionBook());
        }

    }

    public void setGenre(List<Genre> list) {
        genreList.clear();
        for (Genre genre: list) {
            genreList.add(genre);
        }
        genreButton.setText("Chọn thể loại (" + list.size() + "/3)");
    }
}
