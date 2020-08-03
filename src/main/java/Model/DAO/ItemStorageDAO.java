package Model.DAO;

import Main.App;
import Model.Class.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.prefs.Preferences;

public class ItemStorageDAO {
    public ItemStorageDAO(){
    }

    public void insert(Item_Storage itemStorage) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(itemStorage);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void update(Item_Storage itemStorage) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.update(itemStorage);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Item_Storage> getItemByStorage(Storage storage){
        Session session = App.getSession();
        List<Item_Storage> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item_Storage> query = session.createQuery(
                    "FROM Item_Storage as i JOIN FETCH i.item " +
                            "WHERE storage_ID = :idStorage " , Item_Storage.class
            );
            query.setParameter("idStorage", storage.getIdStorage());
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

}
