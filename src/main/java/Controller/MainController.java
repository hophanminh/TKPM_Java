package Controller;

import Model.Class.Book;
import Model.Class.Employee;
import Model.Class.Item;
import Model.DAO.Item_BookDAO;
import impl.org.controlsfx.skin.AutoCompletePopup;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class MainController implements Controller {
    private Stage thisStage;
    private Preferences pref;
    private Item_BookDAO  Item_BookDAO;
    private TableView tableView;

    @FXML
    private HBox searchPane;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button addItem;

    public MainController(Stage stage) {
        try {
            thisStage = stage;
            pref = Preferences.userNodeForPackage(Employee.class);
            Item_BookDAO = new Item_BookDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 668, 592));
            thisStage.setTitle("Quản lý nhà sách");
            thisStage.setResizable(false);

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
        // get all item in database
        Item_BookDAO = new Item_BookDAO();
        List<Item> listItem = Item_BookDAO.getAllItem();

        // Create autocomplete search bar
        if (listItem != null) {
            // create textfield with clear button
            TextField searchBar = TextFields.createClearableTextField();

            // add autocomplete
            AutoCompletionBinding auto = TextFields.bindAutoCompletion(searchBar, listItem);
            auto.setMinWidth(searchPane.getPrefWidth());
            auto.setVisibleRowCount(10);

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
                        tableView.getItems().add(newItem);
                    }
                } catch (NumberFormatException ex) {
                }
            });
        }

        // create tableview
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

    }

    private TableView createTableView(){
        TableView newTable = new TableView();

        // Add column to Tableview
        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        idColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameItem"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, Float> priceColumn = new TableColumn("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("priceItem"));
        priceColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, String> authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(cellData -> {
            // check if row is item or book class
            Item item = cellData.getValue() ;
            if (item instanceof Book) {
                return new SimpleStringProperty(((Book)item).getAuthorBook());
            }
            return new SimpleObjectProperty<>(null);
        });
        authorColumn.setStyle( "-fx-alignment: CENTER;");

        // Create column with quantity textfield
        TableColumn<Item, Integer> quantityColumn = new TableColumn("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Item, Integer>, TableCell<Item, Integer>> cellFactory
                = //
                new Callback<TableColumn<Item, Integer>, TableCell<Item, Integer>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Item, Integer> param)
                    {
                        final TableCell<Item, Integer> cell = new TableCell<Item, Integer>()
                        {

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
                                            Item currentItem =  (Item)tableView.getItems().get(getIndex());
                                            currentItem.setQuantity(Integer.parseInt(quantity.getText()));
                                        }
                                    }
                                });
                                quantity.setAlignment(Pos.CENTER);
                            }

                            // auto add to table when add new row
                            @Override
                            public void updateItem(Integer item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(quantity);
                                    setText(null);

                                    Item currentItem =  (Item)tableView.getItems().get(getIndex());
                                    String text = Integer.toString(currentItem.getQuantity());
                                    quantity.setText(text);
                                }
                            }
                        };
                        return cell;
                    }
                };
        quantityColumn.setCellFactory(cellFactory);

        // Create column with clear button
        TableColumn clearColumn = new TableColumn("Action");
        clearColumn.setMinWidth(50);
        clearColumn.setMaxWidth(50);
        clearColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory2
                = //
                new Callback<TableColumn<Item, String>, TableCell<Item, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Item, String> param)
                    {
                        final TableCell<Item, String> cell = new TableCell<Item, String>()
                        {

                            final Button btn = new Button("X");
                            {
                                btn.setOnAction(event -> {
                                    tableView.getItems().remove(getIndex());
                                });
                            }

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
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
        clearColumn.setCellFactory(cellFactory2);

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