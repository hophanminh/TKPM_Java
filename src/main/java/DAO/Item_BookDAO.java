package DAO;
import Class.Item;
import Main.Main;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class Item_BookDAO {
    public Item_BookDAO(){
    }

    public List<Item> getAllItem() {

        // get global session
        Session session = Main.getSession();
        List<Item> resultList = null;

        try {
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

    public Item getIDItem(int id) {

        // get global session
        Session session = Main.getSession();
        List<Item> resultList = null;

        try {
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "from Item where idItem = :id", Item.class
            );
            query.setParameter("id",id);
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList.get(0);

    }
    public void addItem(String name, int price) {
        Session session = Main.getSession();

        try {
            session.getTransaction().begin();

            Query<Item> query = session.createSQLQuery(
                    "INSERT INTO Item(nameItem, priceItem) VALUES (:name, :price)"
            );
            query.setParameter("name",name).setParameter("price",price);
            query.executeUpdate();

            session.getTransaction().commit();

        } catch(Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }
}
