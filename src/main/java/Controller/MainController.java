package Controller;

import Model.Class.Book;
import Model.Class.Employee;
import Model.Class.Item;
import Model.DAO.Item_BookDAO;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.prefs.Preferences;

public class MainController implements Controller {
    private Stage thisStage;
    private Preferences pref;
    private Item_BookDAO  Item_BookDAO;
    private TableView tableView;
    private final Duration duration = new Duration(1000); // 1000ms = 1s

    @FXML
    private Label totalCost;
    @FXML
    private Label summary;
    @FXML
    private Label tax;
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

    public MainController(Stage stage) {
        try {
            // keep stage, change scene
            thisStage = stage;
            pref = Preferences.userNodeForPackage(Employee.class);
            Item_BookDAO = new Item_BookDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 1200, 1000));
            thisStage.setTitle("Quản lý nhà sách");
            thisStage.setResizable(true);
            thisStage.setMaximized(true);

            // maximize window
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

    public void showStage() {
        thisStage.show();
    }

    public void reloadStage() {
        MainController reload = new MainController(thisStage);
        reload.showStage();
    }

    @FXML
    private void initialize() {
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
        List<Item> listItem = Item_BookDAO.getAllItem();

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
                        newItem.setQuantityItem(1);             // only default 1 when sell
                        tableView.getItems().add(newItem);
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

        // Get list employees of store
        employeeList.setOnAction(actionEvent -> {
            Management management = new Management(thisStage);
            management.showStage();
        });
    }

    private TableView createTableView(){
        TableView newTable = new TableView();

        // Add column to Tableview
        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        idColumn.setStyle( "-fx-alignment: CENTER;");
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(80);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameItem"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, Float> priceColumn = new TableColumn("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("priceItem"));
        priceColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, String> authorColumn = new TableColumn("Author");
        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory
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
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(null);

                                    Item selected = (Item) newTable.getItems().get(getIndex());
                                    if (selected instanceof Book) {
                                        setText(((Book) selected).getAuthorBook());
                                    }
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        authorColumn.setCellFactory(cellFactory);
        authorColumn.setStyle( "-fx-alignment: CENTER;");

        // Create column with quantity textfield
        TableColumn<Item, Integer> quantityColumn = new TableColumn("Quantity");
        quantityColumn.setMinWidth(120);
        quantityColumn.setMaxWidth(120);
        Callback<TableColumn<Item, Integer>, TableCell<Item, Integer>> cellFactory2
                = //
                new Callback<TableColumn<Item, Integer>, TableCell<Item, Integer>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Item, Integer> param)
                    {
                        final TableCell<Item, Integer> cell = new TableCell<Item, Integer>()
                        {
                            // auto add to table when add new row
                            @Override
                            public void updateItem(Integer item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final TextField quantity = new TextField();
                                {
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
                                                Item selected =  (Item)tableView.getItems().get(getIndex());
                                                selected.setQuantityItem(Integer.parseInt(quantity.getText()));
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

                                    Item selected =  (Item)tableView.getItems().get(getIndex());
                                    int num = selected.getQuantityItem();
                                    quantity.setText(Integer.toString(num));
                                }
                            }
                        };
                        return cell;
                    }
                };
        quantityColumn.setCellFactory(cellFactory2);
        quantityColumn.setStyle( "-fx-alignment: CENTER;");

        // Create column with clear button
        TableColumn clearColumn = new TableColumn("Action");
        clearColumn.setMinWidth(50);
        clearColumn.setMaxWidth(50);
        clearColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory3
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
        clearColumn.setCellFactory(cellFactory3);

        TableColumn[] columns = {idColumn, nameColumn, priceColumn, authorColumn, quantityColumn, clearColumn};
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
}
