package DAO;

import Main.Main;
import Class.Item;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ItemDAO {
    public ItemDAO(){
    }

    public List<Item> getAllItem(){

        // get global session
        Session session = Main.getSession();
        List<Item> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "from Item " , Item.class
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
