package Controller;

import Model.Class.*;
import Model.DAO.*;
import View.AlertDialog;
import View.MyMenuView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import utils.DateUtil;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class MainController implements Controller {
    private Stage thisStage;
    private Preferences pref;
    private Item_BookDAO  Item_BookDAO;
    private EmployeeDAO employeeDAO;
    private StoreDAO storeDAO;
    private BillDAO billDAO;
    private TableView tableView;
    private final Duration duration = new Duration(1000); // 1000ms = 1s

    @FXML
    private MenuBar menuBar;

    @FXML
    private Label totalCost;

    @FXML
    private Label discount; // Change to Choice Box or automatic

    @FXML
    private Label dateMakeBill;

    @FXML
    private Label nameSaleMan;

    @FXML
    private TextField nameCustomer;

    @FXML
    private HBox searchPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button addItem;

    @FXML
    private Button addGenreButton;

    @FXML
    private Button employeeList;

    @FXML
    private Button addStorageButton;

    @FXML
    private Button storageButton;

    @FXML
    private Button createBillButton;

    public MainController(Stage stage) {
        try {
            // keep stage, change scene
            thisStage = stage;
            pref = Preferences.userNodeForPackage(Employee.class);
            Item_BookDAO = new Item_BookDAO();
            employeeDAO = new EmployeeDAO();
            storeDAO = new StoreDAO();
            billDAO = new BillDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            loader.setController(this);
            Scene newScene = new Scene(loader.load(), 1200, 1000);
            newScene.getStylesheets().add("CSS/stylesheet.css");
            thisStage.setScene(newScene);
            thisStage.setTitle("Quản lý nhà sách");
            thisStage.setResizable(true);
            thisStage.setMaximized(true);

            // maximize window
            /*Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            thisStage.setX(bounds.getMinX());
            thisStage.setY(bounds.getMinY());
            thisStage.setWidth(bounds.getWidth());
            thisStage.setHeight(bounds.getHeight());

             */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage() {
        thisStage.show();
    }

    public void reloadStage() {
        MainController reload = new MainController(thisStage);
        reload.showStage();
    }

    @FXML
    private void initialize() {
        // create menu bar
        MyMenuView menuView = new MyMenuView();
        menuView.createMenu(menuBar, thisStage, this);


        // Set name of seller
        nameSaleMan.setText(pref.get("name", "NULL"));

        // refresh time every second
        Timeline timeline = new Timeline(new KeyFrame(this.duration, ev -> {
            LocalDateTime now = LocalDateTime.now();
            dateMakeBill.setText(DateUtil.formatDate(now));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        // get all item in database
        Item_BookDAO = new Item_BookDAO();
        List<Item> listItem = Item_BookDAO.getAllItemDefault();

        // Create autocomplete search bar
        if (listItem != null) {
            // create text field with clear button
            TextField searchBar = TextFields.createClearableTextField();
            searchBar.setStyle("-fx-font-size: 15");

            // add autocomplete
            AutoCompletionBinding auto = TextFields.bindAutoCompletion(searchBar, listItem);
            auto.minWidthProperty().bind(searchPane.widthProperty());
            auto.setVisibleRowCount(10);            // only show top 10 result

            //add into panel
            searchPane.getChildren().add(searchBar);
            HBox.setHgrow(searchBar, Priority.ALWAYS);

            // when press enter in searchBar
            searchBar.setOnAction(actionEvent -> {
                // get input text
                String selected = searchBar.getText();
                // find id of item (first split)
                String[] listString = selected.split("-");
                // get item from database
                try
                {
                    int index = Integer.parseInt(listString[0].trim());
                    Item newItem = Item_BookDAO.getItemById(index);
                    // add to table
                    if (newItem != null) {
                        Bill_Item temp = new Bill_Item(newItem, 1, 0);
                        tableView.getItems().add(temp);
                        updateBill();
                    }
                } catch (NumberFormatException ex) {
                }
            });
        }

        // create table view
        tableView = createTableView();
        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        borderPane.setCenter(null);
        borderPane.setCenter(scroll);

        addItem.setOnAction(actionEvent -> {
            AddItem controller = new AddItem(thisStage, this);
            controller.showStage();
        });

        addGenreButton.setOnAction(actionEvent -> {
            AddGenre controller = new AddGenre(thisStage, this);
            controller.showStage();
        });

        addStorageButton.setOnAction(actionEvent -> {
            AddStorage controller = new AddStorage(thisStage, this);
            controller.showStage();
        });

        // Get list employees of store
        employeeList.setOnAction(actionEvent -> {
            Management management = new Management(thisStage, this);
            management.showStage();
        });

        // Get list employees of store
        storageButton.setOnAction(actionEvent -> {
            int id = pref.getInt("defaultStore", -1);
            ViewStorageOfStore management = new ViewStorageOfStore(thisStage, this, id);
            management.showStage();
        });

        createBillButton.setOnAction(actionEvent -> {
            // create bill
            float total = Float.parseFloat(totalCost.getText());
            float discountMoney = Float.parseFloat(discount.getText());
            LocalDateTime time = DateUtil.stringToDateTime(dateMakeBill.getText());
            Customer currentCustomer = null;
            Employee currentEmployee = employeeDAO.getEmployeeByID(pref.getInt("ID",-1));
            Store currentStore = storeDAO.getStoreById(pref.getInt("defaultStore",-1));
            List<Bill_Item> tableList = tableView.getItems();

            Bill newBill = new Bill(total, total,discountMoney, time, currentCustomer, currentEmployee, currentStore);
            billDAO.insert(newBill, tableList);

            // Create and display AlertWindow
            AlertDialog success = new AlertDialog();
            Alert successAlert = success.createAlert(thisStage,
                    "INFORMATION",
                    "Tạo thành công",
                    "Tạo biên lai thành công");
            successAlert.showAndWait();

            // clear table and label
            tableView.getItems().clear();
            totalCost.setText("0.0");
            discount.setText("-0.0");
        });
    }

    private TableView createTableView(){
        TableView newTable = new TableView();

        // Add column to Tableview
        TableColumn idColumn = new TableColumn("ID");
        idColumn.setStyle( "-fx-alignment: CENTER;");
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(80);
        Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>> cellFactory
                = //
                new Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill_Item, String> param)
                    {
                        final TableCell<Bill_Item, String> cell = new TableCell<Bill_Item, String>()
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
                                    Bill_Item selected = (Bill_Item) newTable.getItems().get(getIndex());
                                    String result = Integer.toString(selected.getItem().getIdItem());
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        idColumn.setCellFactory(cellFactory);

        TableColumn nameColumn = new TableColumn("Tên sản phẩm");
        nameColumn.setStyle( "-fx-alignment: CENTER_LEFT;");
        Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>> cellFactory2
                = //
                new Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill_Item, String> param)
                    {
                        final TableCell<Bill_Item, String> cell = new TableCell<Bill_Item, String>()
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
                                    Bill_Item selected = (Bill_Item) newTable.getItems().get(getIndex());
                                    String result = selected.getItem().getNameItem();
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        nameColumn.setCellFactory(cellFactory2);

        TableColumn priceColumn = new TableColumn("Đơn giá");
        priceColumn.setStyle( "-fx-alignment: CENTER_RIGHT;");
        priceColumn.setMinWidth(150);
        priceColumn.setMaxWidth(150);
        Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>> cellFactory3
                = //
                new Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill_Item, String> param)
                    {
                        final TableCell<Bill_Item, String> cell = new TableCell<Bill_Item, String>()
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
                                    Bill_Item selected = (Bill_Item) newTable.getItems().get(getIndex());
                                    String result = Float.toString(selected.getItem().getPriceItem());
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        priceColumn.setCellFactory(cellFactory3);

        // Create column with quantity textfield
        TableColumn quantityColumn = new TableColumn("Số lượng");
        quantityColumn.setMinWidth(150);
        quantityColumn.setMaxWidth(150);
        quantityColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Bill_Item, Integer>, TableCell<Bill_Item, Integer>> cellFactory4
                = //
                new Callback<TableColumn<Bill_Item, Integer>, TableCell<Bill_Item, Integer>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill_Item, Integer> param)
                    {
                        final TableCell<Bill_Item, Integer> cell = new TableCell<Bill_Item, Integer>()
                        {
                            // auto add to table when add new row
                            @Override
                            public void updateItem(Integer item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final TextField quantity = new TextField();
                                {
                                    quantity.setAlignment(Pos.CENTER_RIGHT);
                                    // can only enter number
                                    quantity.textProperty().addListener(new ChangeListener<String>() {
                                        @Override
                                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                                            String newValue) {
                                            // "\d*": only number and matches between zero and unlimited times
                                            if (!newValue.matches("\\d*")) {
                                                quantity.setText(newValue.replaceAll("[^\\d]", ""));
                                            }
                                        }
                                    });
                                    // when out of focus, prevent blank and zero
                                    quantity.focusedProperty().addListener(new ChangeListener<Boolean>() {
                                        @Override
                                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                                            Boolean newValue)
                                        {
                                            if (!newValue) {
                                                // prevent blank value
                                                if (quantity.getText().trim().isEmpty()) {
                                                    quantity.setText("1");
                                                }
                                                //remove 0 ("[^1-9]+" is zero)
                                                if (quantity.getText().matches("[^1-9]+")) {
                                                    quantity.setText("1");
                                                }
                                                Bill_Item selected =  (Bill_Item) tableView.getItems().get(getIndex());

                                                // get current value
                                                float currentPrice = selected.getItem().getPriceItem();
                                                float currentDiscount = selected.getDiscount();

                                                // get new value
                                                int newCount = Integer.parseInt(quantity.getText());
                                                float newTotal = currentPrice * newCount * (100 - currentDiscount)/100;

                                                // update
                                                selected.setCount(newCount);
                                                selected.setTempCount(newCount);
                                                selected.setTotal(newTotal);
                                                updateBill();
                                            }
                                        }
                                    });
                                    quantity.setAlignment(Pos.CENTER);
                                }
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(quantity);
                                    setText(null);

                                    Bill_Item selected =  (Bill_Item)tableView.getItems().get(getIndex());
                                    int num = selected.getCount();
                                    quantity.setText(Integer.toString(num));
                                }
                            }
                        };
                        return cell;
                    }
                };
        quantityColumn.setCellFactory(cellFactory4);

        // Create column with discount textfield
        TableColumn discountColumn = new TableColumn("Giảm giá (%)");
        discountColumn.setMinWidth(150);
        discountColumn.setMaxWidth(150);
        discountColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Bill_Item, Integer>, TableCell<Bill_Item, Integer>> cellFactory5
                = //
                new Callback<TableColumn<Bill_Item, Integer>, TableCell<Bill_Item, Integer>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill_Item, Integer> param)
                    {
                        final TableCell<Bill_Item, Integer> cell = new TableCell<Bill_Item, Integer>()
                        {
                            // auto add to table when add new row
                            @Override
                            public void updateItem(Integer item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final TextField discount = new TextField();
                                {
                                    discount.setAlignment(Pos.CENTER_RIGHT);
                                    // can only enter number
                                    discount.textProperty().addListener(new ChangeListener<String>() {
                                        @Override
                                        public void changed(ObservableValue<? extends String> observable, String oldValue,
                                                            String newValue) {
                                            // "\d*": only number and matches between zero and unlimited times
                                            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                                                discount.setText(oldValue);
                                            }
                                        }
                                    });
                                    // when out of focus, prevent blank and zero
                                    discount.focusedProperty().addListener(new ChangeListener<Boolean>() {
                                        @Override
                                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                                                            Boolean newValue)
                                        {
                                            if (!newValue) {
                                                // prevent blank value
                                                if (discount.getText().trim().isEmpty()) {
                                                    discount.setText("0.0");
                                                }
                                                // only < 100
                                                if (Float.parseFloat(discount.getText()) > 100) {
                                                    discount.setText("100.0");
                                                }
                                                Bill_Item selected =  (Bill_Item) tableView.getItems().get(getIndex());

                                                // get current value
                                                float currentPrice = selected.getItem().getPriceItem();
                                                float currentCount = selected.getCount();

                                                // get new value
                                                float newDiscount = Float.parseFloat(discount.getText());
                                                float newTotal = currentPrice * currentCount * (100 - newDiscount)/100;

                                                // update
                                                selected.setDiscount(newDiscount);
                                                selected.setTempDiscount(newDiscount);
                                                selected.setTotal(newTotal);
                                                updateBill();
                                            }
                                        }
                                    });
                                    discount.setAlignment(Pos.CENTER);
                                }

                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(discount);
                                    setText(null);

                                    Bill_Item selected =  (Bill_Item)tableView.getItems().get(getIndex());
                                    String result = Float.toString(selected.getDiscount());
                                    discount.setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        discountColumn.setCellFactory(cellFactory5);

        TableColumn<Bill_Item, Float> totalColumn = new TableColumn("Thành tiền");
        totalColumn.setMinWidth(150);
        totalColumn.setMaxWidth(150);
        totalColumn.setStyle( "-fx-alignment: CENTER_RIGHT;");
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().tempTotalProperty().asObject());

        /*
        Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>> cellFactory6
                = //
                new Callback<TableColumn<Bill_Item, String>, TableCell<Bill_Item, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill_Item, String> param)
                    {
                        final TableCell<Bill_Item, String> cell = new TableCell<Bill_Item, String>()
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
                                    Bill_Item selected = (Bill_Item) newTable.getItems().get(getIndex());
                                    String result = Float.toString(selected.getTempTotal());
                                    setText(result);
                                }
                            }
                        };
                        return cell;
                    }
                };
        totalColumn.setCellFactory(cellFactory6);

         */

        // Create column with clear button
        TableColumn clearColumn = new TableColumn("Action");
        clearColumn.setMinWidth(80);
        clearColumn.setMaxWidth(80);
        clearColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory7
                = //
                new Callback<TableColumn<Item, String>, TableCell<Item, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Item, String> param)
                    {
                        final TableCell<Item, String> cell = new TableCell<Item, String>()
                        {

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final Button btn = new Button("X");
                                {
                                    btn.setOnAction(event -> {
                                        tableView.getItems().remove(getIndex());
                                        updateBill();
                                    });
                                }

                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        clearColumn.setCellFactory(cellFactory7);

        TableColumn[] columns = {idColumn, nameColumn, priceColumn, quantityColumn, discountColumn, totalColumn, clearColumn};
        newTable.getColumns().addAll(columns);

        // make column not dragable
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

        return newTable;
    }

    private void updateBill() {
        List<Bill_Item> tableList = tableView.getItems();
        float newDiscount = 0;
        float newTotal = 0;
        for (Bill_Item item: tableList) {
            int count = item.getCount();
            float price = item.getItem().getPriceItem();
            float total = item.getTotal();
            newTotal += total;
            newDiscount += (count * price - total);
        }
        totalCost.setText(Float.toString(newTotal));
        discount.setText("-" + newDiscount);
    }
}
