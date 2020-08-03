package Model.DAO;

import Main.App;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StoreDAO {
    public StoreDAO(){
    }

    public void insert(String name, String address) {
        Session session = App.getSession();
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

    public List<Store> getAllStoresInitialized(){
        Session session = App.getSession();
        List<Store> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Store> query = session.createQuery(
                    "SELECT DISTINCT s FROM Store as s LEFT JOIN FETCH s.itemList" , Store.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Store> getAllStores(){
        Session session = App.getSession();
        List<Store> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Store> query = session.createQuery(
                    "FROM Store " , Store.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public Store getStoreById(int idStore) {
        Session session = App.getSession();
        Store resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Store> query = session.createQuery(
                    "FROM Store s WHERE s.idStore = :idStore" , Store.class
            );
            query.setParameter("idStore", idStore);
            resultList = query.getSingleResult();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }
}
