package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class Main extends Application {
    private static Stage pStage;
    public static Stage getPrimaryStage() {
        return pStage;
    }
    private void setPrimaryStage(Stage pStage) {
        Main.pStage = pStage;
    }

    private static SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private static Session session = sessionFactory.getCurrentSession();
    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }



    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();
    }
}


