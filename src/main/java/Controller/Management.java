package Controller;

import Model.Class.*;
import Model.DAO.*;
import com.itextpdf.text.DocumentException;
import javafx.beans.property.SimpleStringProperty;
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
import utils.PrinterPDF;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class Management implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;

    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;
    private StorageDAO storageDAO;
    private StoreDAO storeDAO;
    private Item_BookDAO item_bookDAO;
    private ItemStoreDAO itemStoreDAO;
    private ItemStorageDAO itemStorageDAO;

    private String selectionIE;

    private List<Employee> employeeList;
    private List<Item> itemList;
    private List<Customer> customerList;
    private Store store = new Store();
    private Storage storage = new Storage();
    private ObservableList observableList;

    @FXML
    public TableView tableView;

    @FXML
    public ComboBox ssComboBox;
    @FXML
    public ComboBox ieComboBox;

    @FXML
    public TextField searchText;

    @FXML
    public Button searchButton;
    @FXML
    public Button printButton;
    @FXML
    public Button sendEmailButton;

    @FXML
    public Button backToMainButton;
    @FXML
    public Button addButton;
    @FXML
    public Button deleteButton;

    public Management(Stage previousStage, Controller previous){
        try{
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);

            employeeDAO = new EmployeeDAO();
            customerDAO = new CustomerDAO();
            storageDAO = new StorageDAO();
            storeDAO = new StoreDAO();
            item_bookDAO = new Item_BookDAO();
            itemStoreDAO = new ItemStoreDAO();
            itemStorageDAO = new ItemStorageDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Management.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Quản lý");
            thisStage.setResizable(false);
//            thisStage.setMaximized(true);

            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

            sendEmailButton.setVisible(false);

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
    }

    @FXML
    public void initialize(){
        itemList = new ArrayList<>();
        List<Store> storeList = storeDAO.getAllStores();
        List<Storage> storageList = storageDAO.getAllStorages();
        for (Store store: storeList){
            ssComboBox.getItems().add(store);
        }
        for(Storage storage: storageList){
            ssComboBox.getItems().add(storage);
        }
        ssComboBox.getSelectionModel().selectFirst();
        ssComboBox.setOnAction(actionEvent -> {
            if(ssComboBox.getSelectionModel().getSelectedItem().getClass() == Store.class){
                this.store = (Store) ssComboBox.getSelectionModel().getSelectedItem();
                this.storage = null;
            } else {
                this.storage = (Storage) ssComboBox.getSelectionModel().getSelectedItem();
                this.store = null;
            }
            generateData();
        });

        String[] ieChoiceList = {"Items", "Employees", "Customers"};
        ieComboBox.getItems().addAll(ieChoiceList);
        ieComboBox.getSelectionModel().selectFirst();
        ieComboBox.setOnAction(actionEvent -> {
            if (ieComboBox.getSelectionModel().getSelectedItem().equals("Items")){
                sendEmailButton.setVisible(false);
                this.selectionIE = "Items";
                addButton.setText("Add");
            }
            else if(ieComboBox.getSelectionModel().getSelectedItem().equals("Employees")){
                sendEmailButton.setVisible(false);
                this.selectionIE = "Employees";
                addButton.setText("Add");
            } else {
                sendEmailButton.setVisible(true);
                this.selectionIE = "Customers";
                addButton.setText("Add");
            }
            generateData();
        });


        // set default
        this.store = (Store) ssComboBox.getSelectionModel().getSelectedItem();
        this.selectionIE = "Items";
        this.addButton.setText("Add Items");
        generateData();

        backToMainButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

        addButton.setOnAction(actionEvent -> {
            System.out.println("Adding...");
            if(this.selectionIE.equals("Items")){
                AddItem controller = new AddItem(thisStage, this);
                controller.showStage();
            } else if (this.selectionIE.equals("Employees")){
                AddEmployee addEmployee = new AddEmployee(thisStage, this);
                addEmployee.showStage();
            } else {
                AddCustomer addCustomer = new AddCustomer(thisStage, this);
                addCustomer.showStage();
            }
        });

        deleteButton.setOnAction(actionEvent -> {
            if(this.selectionIE.equals("Items")){
                Item item = (Item) tableView.getSelectionModel().getSelectedItem();
                item_bookDAO.delete(item);
                observableList.remove(item);
            } else if (this.selectionIE.equals("Employees")) {
                Employee employee = (Employee) tableView.getSelectionModel().getSelectedItem();
                employeeDAO.deleteEmployee(employee);
                observableList.remove(employee);
            } else {
                Customer customer = (Customer) tableView.getSelectionModel().getSelectedItem();
                customerDAO.deleteCustomer(customer);
                observableList.remove(customer);

            }
        });

        searchButton.setOnAction(actionEvent -> {
            if(this.selectionIE.equals("Items")){

            } else if (this.selectionIE.equals("Employees")) {
                searchEmployee();
            } else {
                searchCustomer();
            }
        });

        sendEmailButton.setOnAction((actionEvent -> {
            SendEmail sendEmail = new SendEmail(thisStage, this, customerList);
            sendEmail.showStage();
        }));

        printButton.setOnAction(actionEvent -> {
            PrinterPDF printerPDF = null;
            try {
                printerPDF = new PrinterPDF();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            if(selectionIE.equals("Items")) {
                try {
                    printerPDF.ItemsReport(itemList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }  else if (selectionIE.equals("Employees")) {
                try {
                    printerPDF.EmployeesReport(employeeList);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        });


        searchText.setOnAction(actionEvent -> {
            searchButton.fire();
        });
    }

    public void createTableViewItem(){
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
                                        Item selected = (Item) tableView.getItems().get(getIndex());

                                        if (store != null) {
                                            goToItemDetail((Item) selected, store, null);

                                        }
                                        else {
                                            goToItemDetail((Item) selected,null, storage);
                                        }
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

    public void createTableViewEmployee(){
        // Clear table view
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Add columns to Table view
        TableColumn<Employee, String> nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Employee, String> phoneColumn = new TableColumn("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Employee, Integer> salaryColumn = new TableColumn("Salary");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Employee, Date> startDateColumn = new TableColumn<Employee, Date>("Start Date");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

        TableColumn<Employee, String> positionColumn = new TableColumn("Position");
        positionColumn.setCellValueFactory(cell -> new SimpleStringProperty(((cell.getValue().getPosition()==0) ? "Employee":"Manager")));
        positionColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Employee, String> statusColumn = new TableColumn("Status");
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(((cell.getValue().getStatus()==0) ? "Not working":"Working")));
        statusColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Employee, Void> detailColumn = new TableColumn("More Details");
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory =
                new Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>>() {
                    @Override
                    public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param){
                        final TableCell<Employee, Void> cell = new TableCell<Employee, Void>() {
                            final Button detailButton = new Button("Details");
                            {
                                detailButton.setOnAction(actionEvent -> {
                                    goToEmployeeDetail((Employee) tableView.getItems().get(getIndex()));
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(detailButton);
                                }
                            }
                        };
                        return cell;
                    }
                };
        detailColumn.setCellFactory(cellFactory);
        TableColumn[] columns = {nameColumn,salaryColumn, phoneColumn, startDateColumn, positionColumn, statusColumn, detailColumn};
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

    public void createTableViewCustomer(){
        // Clear table view
        tableView.getItems().clear();
        tableView.getColumns().clear();

        // Add columns to Table view
        TableColumn<Customer, String> nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nameCustomer"));
        nameColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Customer, String> emailColumn = new TableColumn("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("emailCustomer"));
        emailColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Customer, String> identifyIDCustomer = new TableColumn("Identify ID Customer");
        identifyIDCustomer.setCellValueFactory(new PropertyValueFactory<>("identifyIDCustomer"));

        TableColumn<Customer, Date> dobColumn = new TableColumn("DoB");
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dobCustomer"));
        dobColumn.setStyle( "-fx-alignment: CENTER;");

        TableColumn<Employee, Void> detailColumn = new TableColumn("More Details");
        Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>> cellFactory =
                new Callback<TableColumn<Employee, Void>, TableCell<Employee, Void>>() {
                    @Override
                    public TableCell<Employee, Void> call(final TableColumn<Employee, Void> param){
                        final TableCell<Employee, Void> cell = new TableCell<Employee, Void>() {
                            final Button detailButton = new Button("Details");
                            {
                                detailButton.setOnAction(actionEvent -> {
                                    goToCustomerDetail((Customer) tableView.getItems().get(getIndex()));
                                });
                            }

                            @Override
                            public void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(detailButton);
                                }
                            }
                        };
                        return cell;
                    }
                };
        detailColumn.setCellFactory(cellFactory);
        TableColumn[] columns = {nameColumn,emailColumn, identifyIDCustomer, dobColumn, detailColumn};
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

    public void generateData(){
        if(this.selectionIE.equals("Employees")){
            if(this.store != null){
                employeeList = employeeDAO.getEmployeeByStore(this.store);
            } else employeeList = employeeDAO.getEmployeeByStorage(this.storage);
            observableList = FXCollections.observableList(this.employeeList);
            createTableViewEmployee();
        } else if(this.selectionIE.equals("Items")) {
            createTableViewItem();
            // convert List<Item_Store>, List<Item_Storage> to List<Item>
            if (this.store != null) {
                List<Item_Store> tempList = itemStoreDAO.getItemByStore(this.store);
                for (Item_Store o : tempList) {
                    Item item = o.getItem();
                    item.setQuantityItem(o.getCount());
                    itemList.add(item);
                }
            }
            else {
                List<Item_Storage> tempList = itemStorageDAO.getItemByStorage(this.storage);
                for (Item_Storage o : tempList) {
                    Item item = o.getItem();
                    item.setQuantityItem(o.getCount());
                    itemList.add(item);
                }
            }
            observableList = FXCollections.observableList(this.itemList);
        } else {
            createTableViewCustomer();
            if (this.store != null) {
                this.customerList = customerDAO.getCustomerByStore(store);
                observableList = FXCollections.observableArrayList(this.customerList);
            }
            else createTableViewCustomer();
        }
        tableView.setItems(observableList);
    }

    public void goToItemDetail(Item item, Store store, Storage storage){
        ItemProfile itemProfile = new ItemProfile(thisStage, this, item, store, storage);
        itemProfile.showStage();
    }

    public void goToEmployeeDetail(Employee employee){
        EmployeeProfile employeeProfile = new EmployeeProfile(thisStage, this, employee);
        employeeProfile.showStage();
    }

    public void goToCustomerDetail(Customer customer){
        CustomerProfile customerProfile = new CustomerProfile(thisStage, this, customer);
        customerProfile.showStage();
    }

    public void searchEmployee(){
        this.employeeList.clear();
        String searchSQL = searchText.getText().trim();
        if(searchSQL.equals("")){
            if(this.store != null)
                this.employeeList = employeeDAO.getEmployeeByStore(this.store);
            else this.employeeList = employeeDAO.getEmployeeByStorage(this.storage);
        } else {
            List<Object[]> resultList = employeeDAO.getSearchEmployeeList(searchSQL);
            for(Object[] ob: resultList){
                Employee temp = new Employee();
                temp.setId((int)ob[0]);
                temp.setAddress((String)ob[1]);
                temp.setName((String)ob[2]);
                temp.setPassword((String)ob[3]);
                temp.setPhone((String)ob[4]);
                temp.setPosition((int)ob[5]);
                temp.setSalary((int)ob[6]);
                temp.setStartDate((Date)ob[7]);
                temp.setStatus((int)ob[8]);
                temp.setUsername((String)ob[9]);
                if(ob[10] == null){
                    temp.setStore(null);
                } else {
                    Store store = storeDAO.getStoreById((int)ob[10]);
                    temp.setStore(store);
                }
                if(ob[11] == null){
                    temp.setStorage(null);
                } else {
                    Storage storage = storageDAO.getStorageById((int)ob[11]);
                    temp.setStorage(storage);
                }
                employeeList.add(temp);
            }
        }
        observableList = FXCollections.observableList(this.employeeList);
        tableView.setItems(observableList);
    }

    public void searchCustomer(){
        this.customerList.clear();
        String searchSQL = searchText.getText().trim();
        if(searchSQL.equals("")){
            if(this.store != null)
                this.customerList = customerDAO.getCustomerByStore(this.store);
        } else {
            List<Object[]> resultList = customerDAO.getSearchCustomerList(searchSQL);
            for(Object[] ob: resultList){
                Customer temp = new Customer();
                temp.setIdCustomer((int)ob[0]);
                temp.setDobCustomer((Date) ob[1]);
                temp.setEmailCustomer((String)ob[2]);
                temp.setIdentifyIDCustomer((String)ob[3]);
                temp.setNameCustomer((String)ob[4]);
                if(ob[5] == null){
                    temp.setStore(null);
                } else {
                    Store store = storeDAO.getStoreById((int)ob[5]);
                    temp.setStore(store);
                }
                customerList.add(temp);
            }
        }
        observableList = FXCollections.observableList(this.customerList);
        tableView.setItems(observableList);
    }



}
