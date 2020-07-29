package Model.DAO;

import Main.Main;
import Model.Class.Genre;
import org.hibernate.Session;

public class Book_has_Genre {
    public Book_has_Genre(){}

    public void addGenre(Genre genre){
        Session session = Main.getSession();
        try {
            session.getTransaction().begin();
            session.save(genre);
            session.getTransaction().commit();
        } catch (Exception exception){
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }
}
