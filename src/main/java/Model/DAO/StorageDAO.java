package Model.DAO;

import Main.App;
import Model.Class.Storage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StorageDAO {
    public StorageDAO(){
    }

    public void insert(Storage storage) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(storage);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Storage> getAllStoragesInitialized(){
        Session session = App.getSession();
        List<Storage> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Storage> query = session.createQuery(
                    "SELECT DISTINCT s FROM Storage as s LEFT JOIN FETCH s.itemList " , Storage.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Storage> getAllStorages(){
        Session session = App.getSession();
        List<Storage> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Storage> query = session.createQuery(
                    "from Storage " , Storage.class
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
