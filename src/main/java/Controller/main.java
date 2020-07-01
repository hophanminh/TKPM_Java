package Controller;

import Class.Book;
import Class.Item;
import DAO.Item_BookDAO;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.List;

public class main {
    @FXML
    private TextField searchBar;

    @FXML
    private TableView tableItem;

    private Item_BookDAO  Item_BookDAO;

    @FXML
    private void initialize() {
        // get all item in database
        Item_BookDAO = new Item_BookDAO();
        List<Item> listItem = Item_BookDAO.getAllItem();

        // Create autocomplete search bar
        TextFields.bindAutoCompletion(searchBar, listItem);

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
                                            Item currentItem =  (Item)tableItem.getItems().get(getIndex());
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

                                    Item currentItem =  (Item)tableItem.getItems().get(getIndex());
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
                                    tableItem.getItems().remove(getIndex());
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
        tableItem.getColumns().addAll(columns);

        // make column not dragable
        tableItem.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableItem.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
    }

    // when press enter in searchBar
    public void selectItem(ActionEvent actionEvent) throws IOException {
        // get input text
        String selected = searchBar.getText();
        // find id of item (first split)
        String[] listString = selected.split("-");
        int index = Integer.parseInt(listString[0].trim());
        // get item from database
        Item_BookDAO = new Item_BookDAO();
        Item newItem = Item_BookDAO.getIDItem(index);
        // add to table
        tableItem.getItems().add(newItem);
    }

    public void addItem(ActionEvent actionEvent) throws IOException {

        Stage addItemScreen = (Stage) searchBar.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/addItem.fxml"));
        addItemScreen.setScene(new Scene(root, 668, 592));
        addItemScreen.show();

    }
}
