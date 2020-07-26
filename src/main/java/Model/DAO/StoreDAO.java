package Model.DAO;

import Main.Main;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StoreDAO {
    public StoreDAO(){
    }

    public void insert(String name, String address) {
        Session session = Main.getSession();
        try{
            session.getTransaction().begin();
            Store location = new Store(name, address);
            session.save(location);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Store> getAllStore(){
        Session session = Main.getSession();
        List<Store> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Store> query = session.createQuery(
                    "from Store " , Store.class
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