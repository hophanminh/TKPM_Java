package Controller;

import Model.Class.Genre;
import Model.DAO.GenreDAO;
import View.AlertDialog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.hibernate.annotations.Check;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;

public class SetGenreBook implements Controller {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private GenreDAO genreDAO;

    private HashSet<Genre> previousList;
    private List<Genre> list;
    private FlowPane flowPane;
    private String type;
    private List<CheckBox> listCheckBox;
    @FXML
    private BorderPane borderPane;

    @FXML
    private Button acceptButton;

    @FXML
    private Button closeButton;

    @FXML
    private Button addButton;

    public SetGenreBook(Stage previousStage, Controller previous, HashSet<Genre> list, String type){
        try {
            // create new stage, after finished use "Controller previous" to reload previous stage
            thisStage = new Stage();
            parent = previousStage;
            previousController = previous;
            genreDAO = new GenreDAO();

            previousList = list;
            this.type = type;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setGenreBook.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load(), 600, 469));
            thisStage.setTitle("Chọn thể loại");
            thisStage.setResizable(false);

            // lock to previous stage
            thisStage.initOwner(parent);
            thisStage.initModality(Modality.WINDOW_MODAL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void showStage() {
        thisStage.show();
    }

    public void reloadStage() {
    }

    @FXML
    private void initialize() {
        // get all genre
        list = genreDAO.getAllGenresInitialized();
        listCheckBox = new ArrayList<>();

        // create flow panel
        flowPane = new FlowPane();
        createFlowPane(list, listCheckBox);
        ScrollPane scroll = new ScrollPane(flowPane);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        borderPane.setCenter(null);
        borderPane.setCenter(scroll);


        acceptButton.setOnAction(actionEvent -> {
            // get list of selected genre
            List<Genre> selectedGenre = new ArrayList<>();;
            int count = 0;
            for (int i = 0; i < list.size(); i++) {
                CheckBox check = listCheckBox.get(i);
                if (check.isSelected()) {               // check if i checkbox is checked
                    count++;
                    selectedGenre.add(list.get(i));         // get i genre
                }

            }

            if (count > 3) {
                // Create and display AlertWindow
                AlertDialog fail = new AlertDialog();
                Alert failAlert = fail.createAlert(thisStage,
                        "WARNING",
                        "Thêm thất bại",
                        "Mỗi sách chỉ có thẻ có tối đa 3 thể loại.");
                failAlert.showAndWait();
            }
            else {

                // return list genre to previous stage
                if (type.equals("add")) {
                    ((AddItem)previousController).setGenre(selectedGenre);
                }
                else if (type.equals("update")) {
                    ((ItemProfile)previousController).setGenre(selectedGenre);
                }

                // close login window
                thisStage.close();
            }
        });

        addButton.setOnAction(actionEvent -> {
            AddGenre controller = new AddGenre(thisStage, this);
            controller.showStage();

            list = genreDAO.getAllGenresInitialized();
            listCheckBox = new ArrayList<>();
            createFlowPane(list, listCheckBox);
        });

        closeButton.setOnAction(actionEvent -> {
            thisStage.close();
        });

    }

    private void createFlowPane(List<Genre> list, List<CheckBox> listCheckBox) {
        flowPane.getChildren().clear();

        for (Genre genre: list) {
            // create checkbox for each genre
            String name = genre.getNameGenre();
            CheckBox check = new CheckBox();
            listCheckBox.add(check);

            // check if checkbox is selected previously
            for (Genre temp: previousList) {
                if (name.equals(temp.getNameGenre())) {
                    check.fire();
                };
            }

            // create flow pane
            Label nameGenre = new Label(name);
            nameGenre.setStyle("-fx-font-size: 20");

            HBox genreBox = new HBox();
            genreBox.getChildren().addAll(check, nameGenre);
            genreBox.setSpacing(10);
            genreBox.setAlignment(Pos.CENTER);

            flowPane.getChildren().add(genreBox);
            flowPane.setHgap(50);
            flowPane.setVgap(20);
            flowPane.setPadding(new Insets(10, 0, 10, 20));
        }
    }
}
