package Controller;

import Class.Item;
import DAO.Item_BookDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.List;

public class main {
    @FXML
    private ComboBox searchBar;

    @FXML
    private TableView tableItem;

    private Item_BookDAO  Item_BookDAO;

    @FXML
    private void initialize() {
        Item_BookDAO = new Item_BookDAO();
        List<Item> listItem = Item_BookDAO.getAllItem();

        // Create autocomplete search bar
        searchBar.getItems().addAll(listItem);
        TextFields.bindAutoCompletion(searchBar.getEditor(), searchBar.getItems());

        // Add column to Tableview
        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameItem"));

        TableColumn priceColumn = new TableColumn("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("priceItem"));

        TableColumn authorColumn = new TableColumn("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorBook"));

        tableItem.getColumns().addAll(idColumn, nameColumn, priceColumn, authorColumn);
    }


    public void selectItem(ActionEvent actionEvent) throws IOException {
        // Check null value

        System.out.println(searchBar.getValue());
        tableItem.getItems().add(searchBar.getValue());

        // clear search bar after select
        //searchBar.setValue(null);
    }
}
