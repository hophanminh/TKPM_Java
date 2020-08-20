package Controller;

import Model.Class.*;
import Model.DAO.*;
import View.AlertDialog;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.prefs.Preferences;

public class Report implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private Preferences pref;

    private StoreDAO storeDAO;
    private StorageDAO storageDAO;
    private BillDAO billDAO;

    private List<Store> storeList;
    private List<Storage> storageList;
    private LineChart<Number,Number> lineChart;
    private Double max = 0.0;

    @FXML
    private BorderPane borderPane;

    @FXML
    private ComboBox locationNameField;

    @FXML
    private TextField yearField;

    @FXML
    private Button showButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button closeButton;

    public Report(Stage previousStage, Controller previous){
        try{
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            pref = Preferences.userNodeForPackage(Employee.class);

            storeDAO = new StoreDAO();
            storageDAO = new StorageDAO();
            billDAO = new BillDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/report.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Báo cáo");
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
        Report reloadManagement = new Report(thisStage, this.previousController);
        reloadManagement.showStage();
    }

    @FXML
    public void initialize(){
        // get list of store and storage
        storeList = storeDAO.getAllStores();

        // set yearField field to allow only number
        yearField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                // this will run whenever text is changed
                if (!newValue.matches("\\d*")) {
                    yearField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // default show report of current year
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();
        yearField.setText(Integer.toString(currentYear));



        // set default store
        int idStore = pref.getInt("defaultStore", -1);
        Store currentStore = storeDAO.getStoreById(idStore);
        locationNameField.getItems().addAll(storeList);
        locationNameField.getSelectionModel().select(currentStore);

        // create chart
        createChart();
        addData(lineChart, currentYear, idStore);

        showButton.setOnAction(actionEvent -> {
            int year = Integer.parseInt(yearField.getText());
            int id = ((Store)locationNameField.getSelectionModel().getSelectedItem()).getIdStore();
            addData(lineChart, year, id);
        });

        clearButton.setOnAction(actionEvent -> {
            lineChart.getData().clear();
            max = 0.0;
        });

        yearField.setOnAction(actionEvent -> {
            showButton.fire();
        });

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });
    }

    private void createChart() {
        // create the x and y axes that the chart is going to use
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.tickLabelFontProperty().set(Font.font(15));
        yAxis.tickLabelFontProperty().set(Font.font(15));

        // set the titles for the axes
        xAxis.setLabel("Tháng");
        yAxis.setLabel("Lợi nhuân (đ)");


        // create the line chart. The values of the chart are given as numbers
        // and it uses the axes we created earlier
        lineChart = new LineChart<Number,Number>(xAxis,yAxis);
        lineChart.getXAxis().setAutoRanging(true);
        lineChart.getYAxis().setAutoRanging(true);
        lineChart.setCreateSymbols(false);
        lineChart.setTitle("Doanh thu");

        borderPane.setCenter(lineChart);
    }

    private void addData(LineChart<Number,Number> lineChart, int year, int idStore){
        // prevent duplicate
        Store store = storeDAO.getStoreById(idStore);
        String name = "Cửa hàng: " + store.getNameStore() + " - năm " + year;

        for(XYChart.Series series : lineChart.getData()) {
            if (series.getName().equals(name)) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Đã có báo cáo",
                        "Báo cáo doanh thu của " + name + " đã có sẵn trên biểu đồ.");
                failAlert.showAndWait();
                return;
            }
        }

        List<Object[]> data = billDAO.calculateSaleByYear(year, idStore);

        // find max value
        for (Object[] o: data) {
            if ((Double) o[2] > max) {
                max = (Double) o[2];
            }
        }

        // create data set that's going to be added to the chart
        XYChart.Series saleData = new XYChart.Series();
        saleData.setName(name);


        // create default 12 months
        for (int i = 1; i <= 12; i++) {
            saleData.getData().add(new XYChart.Data(i, 0));
        }

        // add calculated data
        for (Object[] o: data) {
            // update value
            Double sale = (Double) o[2];
            int month = (int) o[1];
            int index = month - 1;
            saleData.getData().remove(index);
            saleData.getData().add(index, new XYChart.Data(month, sale) );

            // show value above each point
            XYChart.Data temp = (XYChart.Data) saleData.getData().get(index);
            temp.setNode(createDataNode( (Double) temp.getYValue()));
        }
        // add the data set to the line chart
        lineChart.getData().add(saleData);
    }

    private HBox createDataNode(Double value) {
        // create pane to show value
        Label label = new Label();
        label.setText(Double.toString(value));
        label.setMaxHeight(30);

        HBox pane = new HBox(label);
        pane.setShape(new Circle(6.0));
        pane.setScaleShape(false);

        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        // hide value normally
        label.setVisible(false);

        // only show value when mouse over
        pane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                // check value to set margin
                Label temp = ((Label) pane.getChildren().get(0));
                Double value = Double.parseDouble(temp.getText());

                if (value > max/2) {
                    HBox.setMargin(temp, new Insets(30, 0, 0, 0));
                }
                else {
                    HBox.setMargin(temp, new Insets(-25, 0, 0, 0));
                }
                // show value
                pane.getChildren().get(0).setVisible(true);
                pane.toFront();
            }
        });

        // hide value when mouse out
        pane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent mouseEvent) {
                HBox.setMargin(pane.getChildren().get(0), new Insets(0, 0, 0, 0));
                pane.getChildren().get(0).setVisible(false);
            }
        });

        return pane;
    }
}

