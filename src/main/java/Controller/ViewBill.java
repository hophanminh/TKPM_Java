package Controller;

import Model.Class.Bill;
import Model.Class.Employee;
import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.BillDAO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class ViewBill implements Controller {
    private Stage thisStage;
    private Stage parentStage;
    private Controller previousController;
    private BillDAO billDAO;
    private StoreDAO storeDAO;
    private Preferences pref;

    private int selectedStore;
    private Store store;

    private TableView tableView;
    private List<Bill> billList;

    @FXML
    private Label nameField;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button closeButton;

    public ViewBill(Stage previousStage, Controller previous, int idStore){
        try {
            thisStage = new Stage();
            parentStage = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);
            storeDAO = new StoreDAO();
            billDAO = new BillDAO();

            selectedStore = idStore;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/viewBill.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Danh sách hóa đơn");
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

    public void reloadStage() {
    }

    @FXML
    private void initialize() {

        // get default store
        store = storeDAO.getStoreById(selectedStore);
        nameField.setText(store.getNameStore());

        // get storage of store
        billList = new ArrayList<>();
        billList.addAll(billDAO.getBillByStore(store));

        tableView = createTableView(billList);
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

    private TableView createTableView(List<Bill> list){
        TableView newTable = new TableView();

        TableColumn idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idBill"));
        idColumn.setStyle( "-fx-alignment: CENTER;");
        idColumn.setMinWidth(80);
        idColumn.setMaxWidth(80);

        TableColumn dateColumn = new TableColumn("Ngày tạo");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateBill"));
        dateColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn customerColumn = new TableColumn("Khách hàng");
        customerColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Bill, String>, TableCell<Bill, String>> cellFactory
                = //
                new Callback<TableColumn<Bill, String>, TableCell<Bill, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill, String> param)
                    {
                        final TableCell<Bill, String> cell = new TableCell<Bill, String>()
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
                                    Bill selected = (Bill) newTable.getItems().get(getIndex());
                                    if (selected.getCustomer() != null) {
                                        String result = selected.getCustomer().getNameCustomer();
                                        setText(result);
                                    }
                                    else {
                                        setText("");
                                    }
                                }
                            }
                        };
                        return cell;
                    }
                };
        customerColumn.setCellFactory(cellFactory);

        TableColumn priceColumn = new TableColumn("Tổng tiền");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("paidBill"));
        priceColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn employeeColumn = new TableColumn("Người tạo");
        employeeColumn.setStyle( "-fx-alignment: CENTER;");
        Callback<TableColumn<Bill, String>, TableCell<Bill, String>> cellFactory2
                = //
                new Callback<TableColumn<Bill, String>, TableCell<Bill, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill, String> param)
                    {
                        final TableCell<Bill, String> cell = new TableCell<Bill, String>()
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
                                    Bill selected = (Bill) newTable.getItems().get(getIndex());
                                    if (selected.getEmployee() != null) {
                                        String result = selected.getEmployee().getName();
                                        setText(result);
                                    }
                                    else {
                                        setText("");
                                    }
                                }
                            }
                        };
                        return cell;
                    }
                };
        employeeColumn.setCellFactory(cellFactory2);


        TableColumn itemColumn = new TableColumn("Sản phẩm");
        itemColumn.setStyle( "-fx-alignment: CENTER;");
        itemColumn.setMinWidth(120);
        itemColumn.setMaxWidth(120);
        Callback<TableColumn<Bill, String>, TableCell<Bill, String>> cellFactory3
                = //
                new Callback<TableColumn<Bill, String>, TableCell<Bill, String>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Bill, String> param)
                    {
                        final TableCell<Bill, String> cell = new TableCell<Bill, String>()
                        {

                            @Override
                            public void updateItem(String item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                final Button allowButton = new Button("Xem danh sách");
                                {
                                    allowButton.setOnAction(event -> {
                                        Bill selected = (Bill) tableView.getItems().get(getIndex());
                                        openItemList(selected.getIdBill());
                                    });
                                }
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    setGraphic(allowButton);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        itemColumn.setCellFactory(cellFactory3);

        TableColumn[] columns = {idColumn, dateColumn, customerColumn, priceColumn, employeeColumn, itemColumn};
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

    private void openItemList(int idBill) {
        // switch stage
        ViewBillItem controller2 = new ViewBillItem(thisStage, this, idBill);
        controller2.showStage();
    }

}
