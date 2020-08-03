package Model.DAO;

import Main.App;
import Model.Class.Genre;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class GenreDAO {
    public GenreDAO(){}

    public void insert(Genre genre) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(genre);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Genre> getAllGenresInitialized() {
        Session session = App.getSession();
        List<Genre> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Genre> query = session.createQuery(
                    "SELECT DISTINCT g FROM Genre as g LEFT JOIN FETCH g.bookList " , Genre.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Genre> getAllGenres() {
        Session session = App.getSession();
        List<Genre> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Genre> query = session.createQuery(
                    "FROM Genre " , Genre.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;

    }

}
