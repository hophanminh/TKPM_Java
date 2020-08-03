package Model.DAO;

import Main.App;
import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Item_Store;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.prefs.Preferences;

public class ItemStoreDAO {
    Preferences pref;

    public ItemStoreDAO(){
    }

    public void insert(Item_Store itemStore) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(itemStore);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void update(Item_Store itemStore) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.update(itemStore);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Item_Store> getItemByStore(Store store){
        Session session = App.getSession();
        List<Item_Store> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item_Store> query = session.createQuery(
                    "FROM Item_Store as i JOIN FETCH i.item " +
                            "WHERE store_ID = :idStore " , Item_Store.class
            );
            query.setParameter("idStore", store.getIdStore());
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

}
