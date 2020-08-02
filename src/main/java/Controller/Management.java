package Controller;

import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Storage;
import Model.Class.Store;
import Model.DAO.EmployeeDAO;
import Model.DAO.Item_BookDAO;
import Model.DAO.StorageDAO;
import Model.DAO.StoreDAO;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.prefs.Preferences;

public class Management implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;

    private EmployeeDAO employeeDAO;
    private StorageDAO storageDAO;
    private StoreDAO storeDAO;
    private Item_BookDAO item_bookDAO;
    private String selectionIE;
    private int selectionSS;

    private List<Employee> employeeList;
    private List<Item> itemList;
    private Store store = new Store();
    private Storage storage = new Storage();
    private ObservableList observableList;

    @FXML
    public TableView tableView;

    @FXML
    public ChoiceBox ssChoiceBox;
    @FXML
    public ChoiceBox ieChoiceBox;

    @FXML
    public TextField searchText;

    @FXML
    public Button searchButton;
    @FXML
    public Button printButton;
    @FXML
    public Button backToMainButton;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Button updateButton;

    public Management(Stage previousStage, Controller previous){
        try{
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);

            employeeDAO = new EmployeeDAO();
            storageDAO = new StorageDAO();
            storeDAO = new StoreDAO();
            item_bookDAO = new Item_BookDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Management.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Management");
            thisStage.setResizable(false);
//            thisStage.setMaximized(true);

            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

//            Screen screen = Screen.getPrimary();
//            Rectangle2D bounds = screen.getVisualBounds();
//            thisStage.setX(bounds.getMinX());
//            thisStage.setY(bounds.getMinY());
//            thisStage.setWidth(bounds.getWidth());
//            thisStage.setHeight(bounds.getHeight());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStage(){thisStage.show();}

    public void reloadStage(){
        Management reloadManagement = new Management(thisStage, this.previousController);
        reloadManagement.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        this.selectionIE = "Items";

        List<Store> storeList = storeDAO.getAllStores();
        List<Storage> storageList = storageDAO.getAllStorages();
        for (Store store: storeList){
            ssChoiceBox.getItems().add(store);
        }
        for(Storage storage: storageList){
            ssChoiceBox.getItems().add(storage);
        }
        ssChoiceBox.getSelectionModel().selectFirst();
        ssChoiceBox.setOnAction(actionEvent -> {
            if(ssChoiceBox.getSelectionModel().getSelectedItem().getClass() == Store.class){
                this.store = (Store) ssChoiceBox.getSelectionModel().getSelectedItem();
                System.out.println(store);
                this.storage = null;
            } else {
                this.storage = (Storage) ssChoiceBox.getSelectionModel().getSelectedItem();
                System.out.println(storage);
                this.store = null;
            }
            generateData();
        });

        String[] ieChoiceList = {"Items", "Employees"};
        ieChoiceBox.getItems().addAll(ieChoiceList);
        ieChoiceBox.getSelectionModel().selectFirst();
        ieChoiceBox.setOnAction(actionEvent -> {
            if (ieChoiceBox.getSelectionModel().getSelectedItem().equals("Items")){
                this.selectionIE = "Items";
            }
            else {
                this.selectionIE = "Employees";
            }
            generateData();
        });

        // set default
        this.store = (Store) ssChoiceBox.getSelectionModel().getSelectedItem();
        this.selectionIE = "Items";
        generateData();

        backToMainButton.setOnAction(actionEvent -> {
            MainController mainController = new MainController(thisStage);
            mainController.showStage();
        });

        addButton.setOnAction(actionEvent -> {
            System.out.println("Adding...");
        });

        deleteButton.setOnAction(actionEvent -> {
            System.out.println("Deleting...");
        });

        updateButton.setOnAction(actionEvent -> {
            System.out.println("Updating...");
        });
    }

    private void createTableViewItem(){
        tableView.getItems().clear();
        tableView.getColumns().clear();
        // Add column to Tableview
        TableColumn<Item, Integer> idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idItem"));
        idColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, String> nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameItem"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, Float> costColumn = new TableColumn("Cost");
        costColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("costItem"));
        costColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, Float> priceColumn = new TableColumn("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Float>("priceItem"));
        priceColumn.setStyle( "-fx-alignment: CENTER;");

//        TableColumn<Item, String> authorColumn = new TableColumn("Author");
//        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory
//                = //
//                new Callback<TableColumn<Item, String>, TableCell<Item, String>>()
//                {
//                    @Override
//                    public TableCell call(final TableColumn<Item, String> param)
//                    {
//                        final TableCell<Item, String> cell = new TableCell<Item, String>()
//                        {
//                            @Override
//                            public void updateItem(String item, boolean empty)
//                            {
//                                super.updateItem(item, empty);
//                                if (empty) {
//                                    setGraphic(null);
//                                    setText(null);
//                                }
//                                else {
//                                    setGraphic(null);
//
//                                    Item selected = (Item) tableView.getItems().get(getIndex());
//                                    if (selected instanceof Book) {
//                                        setText(((Book) selected).getAuthorBook());
//                                    }
//                                    setText(null);
//                                }
//                            }
//                        };
//                        return cell;
//                    }
//                };
//        authorColumn.setCellFactory(cellFactory);
//        authorColumn.setStyle( "-fx-alignment: CENTER;");

        // Create column with quantity textfield
        TableColumn<Item, Integer> quantityColumn = new TableColumn("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantityItem"));
        quantityColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, Float> profitColumn = new TableColumn("Profit");
        Callback<TableColumn<Item, Float>, TableCell<Item, Float>> cellFactory
                = //
                new Callback<TableColumn<Item, Float>, TableCell<Item, Float>>()
                {
                    @Override
                    public TableCell call(final TableColumn<Item, Float> param)
                    {
                        final TableCell<Item, Float> cell = new TableCell<Item, Float>()
                        {
                            @Override
                            public void updateItem(Float item, boolean empty)
                            {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                }
                                else {
                                    Item temp = (Item) tableView.getItems().get(getIndex());
                                    float profit = (temp.getPriceItem() - temp.getCostItem())*temp.getQuantityItem();
                                    setText(Float.toString(profit));
                                }
                            }
                        };
                        return cell;
                    }
                };
        profitColumn.setCellFactory(cellFactory);
        profitColumn.setStyle( "-fx-alignment: CENTER;");

        // Create column with clear button
        TableColumn detail = new TableColumn("Detail");
        detail.setStyle( "-fx-alignment: CENTER;");
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
                                final Button btn = new Button("Details");
                                {
                                    btn.setOnAction(event -> {
                                        //tableView.getItems().remove(getIndex());
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
        detail.setCellFactory(cellFactory3);

        TableColumn[] columns = {idColumn, nameColumn,costColumn, priceColumn, quantityColumn,profitColumn, detail};
        tableView.getColumns().addAll(columns);

        // make column not dragable
        tableView.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableView.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void createTableViewEmployee(){
        tableView.getItems().clear();
        tableView.getColumns().clear();
        // Add column to Tableview
        TableColumn<Item, Integer> idColumn = new TableColumn("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, String> nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, Integer> salaryColumn = new TableColumn("Salary");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Item, String> phoneColumn = new TableColumn("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setStyle( "-fx-alignment: CENTER;");

//        TableColumn<Item, String> authorColumn = new TableColumn("Author");
//        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory
//                = //
//                new Callback<TableColumn<Item, String>, TableCell<Item, String>>()
//                {
//                    @Override
//                    public TableCell call(final TableColumn<Item, String> param)
//                    {
//                        final TableCell<Item, String> cell = new TableCell<Item, String>()
//                        {
//                            @Override
//                            public void updateItem(String item, boolean empty)
//                            {
//                                super.updateItem(item, empty);
//                                if (empty) {
//                                    setGraphic(null);
//                                    setText(null);
//                                }
//                                else {
//                                    setGraphic(null);
//
//                                    Item selected = (Item) tableView.getItems().get(getIndex());
//                                    if (selected instanceof Book) {
//                                        setText(((Book) selected).getAuthorBook());
//                                    }
//                                    setText(null);
//                                }
//                            }
//                        };
//                        return cell;
//                    }
//                };
//        authorColumn.setCellFactory(cellFactory);
//        authorColumn.setStyle( "-fx-alignment: CENTER;");

        // Create column with quantity textfield
//        TableColumn<Item, Integer> quantityColumn = new TableColumn("Quantity");
//        quantityColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantityItem"));
//        quantityColumn.setStyle( "-fx-alignment: CENTER;");
//
//        TableColumn<Item, Float> profitColumn = new TableColumn("Profit");
//        Callback<TableColumn<Item, Float>, TableCell<Item, Float>> cellFactory
//                = //
//                new Callback<TableColumn<Item, Float>, TableCell<Item, Float>>()
//                {
//                    @Override
//                    public TableCell call(final TableColumn<Item, Float> param)
//                    {
//                        final TableCell<Item, Float> cell = new TableCell<Item, Float>()
//                        {
//                            @Override
//                            public void updateItem(Float item, boolean empty)
//                            {
//                                super.updateItem(item, empty);
//                                if (empty) {
//                                    setGraphic(null);
//                                    setText(null);
//                                }
//                                else {
//                                    Item temp = (Item) tableView.getItems().get(getIndex());
//                                    float profit = (temp.getPriceItem() - temp.getCostItem())*temp.getQuantityItem();
//                                    setText(Float.toString(profit));
//                                }
//                            }
//                        };
//                        return cell;
//                    }
//                };
//        profitColumn.setCellFactory(cellFactory);
//        profitColumn.setStyle( "-fx-alignment: CENTER;");
//
//        // Create column with clear button
//        TableColumn detail = new TableColumn("Detail");
//        detail.setStyle( "-fx-alignment: CENTER;");
//        Callback<TableColumn<Item, String>, TableCell<Item, String>> cellFactory3
//                = //
//                new Callback<TableColumn<Item, String>, TableCell<Item, String>>()
//                {
//                    @Override
//                    public TableCell call(final TableColumn<Item, String> param)
//                    {
//                        final TableCell<Item, String> cell = new TableCell<Item, String>()
//                        {
//
//                            @Override
//                            public void updateItem(String item, boolean empty)
//                            {
//                                super.updateItem(item, empty);
//                                final Button btn = new Button("Details");
//                                {
//                                    btn.setOnAction(event -> {
//                                        //tableView.getItems().remove(getIndex());
//                                    });
//                                }
//
//                                if (empty) {
//                                    setGraphic(null);
//                                    setText(null);
//                                }
//                                else {
//                                    setGraphic(btn);
//                                    setText(null);
//                                }
//                            }
//                        };
//                        return cell;
//                    }
//                };
//        detail.setCellFactory(cellFactory3);

        TableColumn[] columns = {idColumn, nameColumn,salaryColumn, phoneColumn};//, quantityColumn,profitColumn, detail};
        tableView.getColumns().addAll(columns);

        // make column not dragable
        tableView.getColumns().addListener(new ListChangeListener() {
            public boolean suspended;
            @Override
            public void onChanged(Change change) {
                change.next();
                if (change.wasReplaced() && !suspended) {
                    this.suspended = true;
                    tableView.getColumns().setAll(columns);
                    this.suspended = false;
                }
            }
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void generateData(){
        if(this.selectionIE.equals("Employees")){
            if(this.store != null){
                employeeList = employeeDAO.getEmployeeByStore(this.store);
            } else employeeList = employeeDAO.getEmployeeByStorage(this.storage);
            observableList = FXCollections.observableList(this.employeeList);
            createTableViewEmployee();
            tableView.setItems(observableList);
        } else {
            if(this.store != null){
                itemList = item_bookDAO.getItemByStore(this.store);
            } else itemList = item_bookDAO.getItemByStorage(this.storage);
            observableList = FXCollections.observableList(this.itemList);
            createTableViewItem();
            tableView.setItems(observableList);
        }
    }
}
