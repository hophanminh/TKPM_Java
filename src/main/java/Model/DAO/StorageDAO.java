package Model.DAO;

import Main.Main;
import Model.Class.Storage;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StorageDAO {
    public StorageDAO(){
    }

    public void insert(String name, String address) {
        Session session = Main.getSession();
        try{
            session.getTransaction().begin();
            Storage storage = new Storage(name, address);
            session.save(storage);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Storage> getAllStorage(){
        Session session = Main.getSession();
        List<Storage> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Storage> query = session.createQuery(
                    "from Storage as s JOIN FETCH s.itemList " , Storage.class
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
