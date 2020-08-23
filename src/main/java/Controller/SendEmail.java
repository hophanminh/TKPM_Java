package Controller;

import Model.Class.Customer;
import Model.DAO.CustomerDAO;
import View.AlertDialog;
import utils.EmailUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public class SendEmail {
    private Stage thisStage;
    private Stage parent;
    private Controller previousController;
    private CustomerDAO customerDAO;
    private List<Customer> listCustomer;

    @FXML
    public TextField titleText;
    @FXML
    public TextArea contentText;

    @FXML
    public Button sendButton;
    @FXML
    public Button backButton;


    public SendEmail(Stage previousStage, Controller previousController, List<Customer> list){
        try {
            thisStage = new Stage();
            parent = previousStage;
            this.previousController = previousController;
            listCustomer = list;
            customerDAO = new CustomerDAO();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Email.fxml"));
            loader.setController(this);
            thisStage.setScene(new Scene(loader.load()));
            thisStage.setTitle("Gui thu");
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
        SendEmail reload = new SendEmail(parent, previousController, listCustomer);
        reload.showStage();
        thisStage.close();
    }

    @FXML
    public void initialize(){
        sendButton.setOnAction(actionEvent -> {
            EmailUtil email = new EmailUtil();
            int success = 0;
            try {
                success += email.sendEmail(thisStage,
                        listCustomer,
                        titleText.getText(),
                        contentText.getText());
            } catch (MessagingException e) {
                e.printStackTrace();
            }

            AlertDialog successDialog = new AlertDialog();
            Alert successAlert = successDialog.createAlert(thisStage,
                    "INFORMATION",
                    "Gửi thành công",
                    "Gửi "+ success+ "/"+ listCustomer.size()+" email thành công");
            successAlert.showAndWait();
            thisStage.close();
        });

        backButton.setOnAction(actionEvent -> {
            thisStage.close();
        });
    }

}
