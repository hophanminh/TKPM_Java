package View;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertDialog {

    public Alert createAlert(Stage previousStage, String type, String title, String body) {
        // Create and display AlertWindow
        Alert newAlert = null;
        switch (type) {
            case "WARNING":
                newAlert = new Alert(Alert.AlertType.WARNING);
                break;
            case "INFORMATION":
                newAlert = new Alert(Alert.AlertType.INFORMATION);
                break;
            case "CONFIRMATION":
                newAlert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
                break;
            default:
                newAlert = new Alert(Alert.AlertType.NONE);
        }

        newAlert.setTitle(title);
        newAlert.setHeaderText(null);
        newAlert.setContentText(body);

        // Lock alert
        newAlert.initOwner(previousStage);
        newAlert.initModality(Modality.WINDOW_MODAL);

        return newAlert;
    }
}
