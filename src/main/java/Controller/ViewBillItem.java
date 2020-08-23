package Controller;

import Model.Class.Bill;
import Model.Class.Bill_Item;
import Model.Class.Employee;
import Model.Class.Store;
import Model.DAO.BillDAO;
import Model.DAO.StoreDAO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ViewBillItem {
    private Stage thisStage;
    private Stage parentStage;
    private Controller previousController;
    private BillDAO billDAO;
    private StoreDAO storeDAO;
    private Preferences pref;

    private int selectedBill;
    private Bill bill;

    private TableView tableView;
    private List<Bill_Item> bill_itemList;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button closeButton;

    public ViewBillItem(Stage previousStage, Controller previous, int idBill){
        try {
            thisStage = new Stage();
            parentStage = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();
            billDAO = new BillDAO();

            selectedBill = idBill;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewBillItem.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Danh sách sản phẩm");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(previousStage);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.showAndWait();
    }

    @FXML
    private void initialize() {

        // get storage of store
        bill_itemList = new ArrayList<>();
        bill_itemList.addAll(billDAO.getBillItemByIDBill(selectedBill));

        tableView = createTableView(bill_itemList);
        // create table
        ScrollPane scroll = new ScrollPane(tableView);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        borderPane.setCenter(null);
        borderPane.setCenter(scroll);

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });
    }

    private TableView createTableView(List<Bill_Item> list){
        TableView newTable = new TableView();

        TableColumn nameColumn = new TableColumn("Tên sản phẩm");
        nameColumn.setStyle( "-fx-alignment: CENTER;");
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
                                    setText(selected.getItem().getNameItem());
                                }
                            }
                        };
                        return cell;
                    }
                };
        nameColumn.setCellFactory(cellFactory);


        TableColumn priceColumn = new TableColumn("Đơn giá");
        priceColumn.setStyle( "-fx-alignment: CENTER;");
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
                                    String price = Float.toString(selected.getItem().getPriceItem());
                                    setText(price);
                                }
                            }
                        };
                        return cell;
                    }
                };
        priceColumn.setCellFactory(cellFactory2);

        TableColumn countColumn = new TableColumn("Số lượng");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        countColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn discountColumn = new TableColumn("Khuyến mãi");
        discountColumn.setStyle( "-fx-alignment: CENTER;");
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
                                    String price = Float.toString(selected.getDiscount());
                                    setText("-" + price);
                                }
                            }
                        };
                        return cell;
                    }
                };
        discountColumn.setCellFactory(cellFactory3);


        TableColumn totalColumn = new TableColumn("Tổng tiền");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn[] columns = {nameColumn, priceColumn, countColumn, discountColumn, totalColumn};
        newTable.getColumns().addAll(columns);

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
