package Model.DAO;
import Model.Class.*;
import Main.Main;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

public class Item_BookDAO {
    Preferences pref;

    public Item_BookDAO(){
    }

    public void insertBook(String name, int quantity, float price, float cost, String author,
                           String publisher, int year, String des, Store store, Storage storage, HashSet<Genre> genreList) {
        Session session = Main.getSession();
        try{
            session.getTransaction().begin();

            Book book = new Book(name, quantity, price, cost, author, des, publisher, year, genreList);
            // set store, storage for book
            if (store != null) {
                store.getItemList().add(book);
                book.getStoreList().add(store);
            }
            else {
                storage.getItemList().add(book);
                book.getStoreList().add(store);
            }
            // set genre for book
            for (Genre genre: genreList) {
                genre.getBookList().add(book);
            }

            session.save(book);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Item> getAllItem(){
        Session session = Main.getSession();
        List<Item> resultList = null;

        // only get item from store where app is used
        pref = Preferences.userNodeForPackage(Employee.class);
        int idStore = pref.getInt("defaultStore", -1);

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "FROM Item as i JOIN FETCH i.storeList as s " +
                    "WHERE s.idStore = :idStore " , Item.class
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

    public Item getItemById(int id) {
        Session session = Main.getSession();
        List<Item> resultList = null;

        try {
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "from Item " +
                            "where idItem = :id", Item.class
            );
            query.setParameter("id", id);
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList.get(0);
    }

    public void addItem(Item item){
        Session session = Main.getSession();
        try {
            session.getTransaction().begin();
            session.save(item);
            session.getTransaction().commit();
        } catch(Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }
}
