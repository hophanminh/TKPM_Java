package Model.DAO;

import Main.App;
import Model.Class.Storage;
import Model.Class.Store;
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

    public void delete(Storage storage) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.remove(storage);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
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

    public Storage getStorageById(int idStorage) {
        Session session = App.getSession();
        Storage resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Storage> query = session.createQuery(
                    "from Storage s WHERE s.idStorage = :idStorage" , Storage.class
            );
            query.setParameter("idStorage", idStorage);
            resultList = query.getSingleResult();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Storage> getAllStoragesExceptOne(int idStorage){
        Session session = App.getSession();
        List<Storage> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Storage> query = session.createQuery(
                    "FROM Storage " +
                            "WHERE idStorage <> :idStorage" , Storage.class
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

    public List<Storage> getStorageByStore(int idStore) {
        Session session = App.getSession();
        List<Storage> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Storage> query = session.createQuery(
                    "FROM Storage as s JOIN FETCH s.storeList " +
                            "WHERE idStore = :idStore" , Storage.class
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

}
