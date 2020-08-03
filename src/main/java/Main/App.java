package Main;

import Controller.Login;
import Model.Class.Employee;
import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

import java.util.prefs.Preferences;

public class App extends Application {
    Preferences pref;
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
        // clear login info when open
        pref = Preferences.userNodeForPackage(Employee.class);
        int store = pref.getInt("defaultStore", -1);
        pref.clear();
        //pref.putInt("defaultStore", store);

        Login controller1 = new Login(primaryStage);
        controller1.showStage();
    }
}


