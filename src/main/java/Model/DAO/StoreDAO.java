package Model.DAO;

import Main.App;
import Model.Class.Bill_Item;
import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class StoreDAO {
    public StoreDAO(){
    }

    public void insert(Store store) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(store);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void update(Store store) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.update(store);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void delete(Store store) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.remove(store);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
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

    public Store getStoreWithStorageByID(int ID){
        Session session = App.getSession();
        List<Store> resultList = null;
        Store result = null;
        try{
            session.getTransaction().begin();

            Query<Store> query = session.createQuery(
                    "SELECT DISTINCT s " +
                    "FROM Store as s LEFT JOIN FETCH s.storageList " +
                    "WHERE s.idStore = :ID" , Store.class
            );
            query.setParameter("ID", ID);
            resultList = query.list();
            if (!resultList.isEmpty()) {
                result = resultList.get(0);
            }
             session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return result;
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

    public List<Store> getAllStoresExceptOne(int idStore){
        Session session = App.getSession();
        List<Store> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Store> query = session.createQuery(
                    "FROM Store " +
                            "WHERE idStore <> :idStore" , Store.class
            );
            query.setParameter("idStore", idStore);
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Store> getStoreByStorage(int idStorage) {
        Session session = App.getSession();
        List<Store> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Store> query = session.createQuery(
                    "FROM Store as st JOIN FETCH st.storageList " +
                            "WHERE idStorage = :idStorage" , Store.class
            );
            query.setParameter("idStorage", idStorage);
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }
}
