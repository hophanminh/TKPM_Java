package Model.DAO;
import Model.Class.*;
import Main.App;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.List;
import java.util.prefs.Preferences;

public class Item_BookDAO {
    Preferences pref;

    public Item_BookDAO(){
    }

    public void insertItem(Item item, int quantity, Store store, Storage storage) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            // set store, storage for book
            if (store != null) {
                Item_StoreID id = new Item_StoreID(item.getIdItem(), store.getIdStore());
                Item_Store itemStore = new Item_Store(id, item, store, quantity);
                session.save(itemStore);
            }
            else {
                Item_StorageID id = new Item_StorageID(item.getIdItem(), storage.getIdStorage());
                Item_Storage itemStorage = new Item_Storage(id, item, storage, quantity);
                session.save(itemStorage);
            }

            session.save(item);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void insertBook(Book book, int quantity, Store store, Storage storage, HashSet<Genre> genreList) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();

            // set store, storage for book
            if (store != null) {
                Item_StoreID id = new Item_StoreID(book.getIdItem(), store.getIdStore());
                Item_Store itemStore = new Item_Store(id, book, store, quantity);
                session.save(itemStore);
            }
            else {
                Item_StorageID id = new Item_StorageID(book.getIdItem(), storage.getIdStorage());
                Item_Storage itemStorage = new Item_Storage(id, book, storage, quantity);
                session.save(itemStorage);
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

    public void updateItem(Item item, Store store, Storage storage){
        Session session = App.getSession();
        try{
            session.getTransaction().begin();

            if (store != null) {
                Item_StoreID id = new Item_StoreID(item.getIdItem(), store.getIdStore());
                Item_Store itemStore = new Item_Store(id, item, store, item.getQuantityItem());
                session.saveOrUpdate(itemStore);
            }
            else {
                Item_StorageID id = new Item_StorageID(item.getIdItem(), storage.getIdStorage());
                Item_Storage itemStorage = new Item_Storage(id, item, storage, item.getQuantityItem());
                session.saveOrUpdate(itemStorage);
            }

            session.saveOrUpdate(item);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void updateBook(Book book, Store store, Storage storage){
        Session session = App.getSession();
        try{
            session.getTransaction().begin();

            if (store != null) {
                Item_StoreID id = new Item_StoreID(book.getIdItem(), store.getIdStore());
                Item_Store itemStore = new Item_Store(id, book, store, book.getQuantityItem());
                session.saveOrUpdate(itemStore);
            }
            else {
                Item_StorageID id = new Item_StorageID(book.getIdItem(), storage.getIdStorage());
                Item_Storage itemStorage = new Item_Storage(id, book, storage, book.getQuantityItem());
                session.saveOrUpdate(itemStorage);
            }

            session.saveOrUpdate(book);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void delete(Item item, Store store, Storage storage){
        Session session = App.getSession();
        try{
            session.getTransaction().begin();

            if (store == null) {
                List<Item_Storage> resultList = null;
                Item_Storage result = null;
                Query<Item_Storage> query = session.createQuery(
                        "from Item_Storage  " +
                                "where item = :item AND storage = :storage", Item_Storage.class
                );
                query.setParameter("item", item).setParameter("storage", storage);
                resultList = query.list();
                if (!resultList.isEmpty()) {                               // find and update
                    result = resultList.get(0);
                    session.delete(result);
                }
            }
            else {
                List<Item_Store> resultList = null;
                Item_Store result = null;
                Query<Item_Store> query = session.createQuery(
                        "from Item_Store  " +
                                "where item = :item AND store = :store", Item_Store.class
                );
                query.setParameter("item", item).setParameter("store", store);
                resultList = query.list();
                if (!resultList.isEmpty()) {                               // find and update
                    result = resultList.get(0);
                    session.delete(result);
                }

            }

            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Item> getAllItemDefault(){
        Session session = App.getSession();
        List<Item> resultList = null;

        // only get item from store where app is used
        pref = Preferences.userNodeForPackage(Employee.class);
        int idStore = pref.getInt("defaultStore", -1);

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "FROM Item as i JOIN FETCH i.itemStoreList as s " +
                    "WHERE store_ID = :idStore " , Item.class
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

    public List<Item> getItemByStore(Store store){
        Session session = App.getSession();
        List<Item> resultList = null;

        // only get item from store where app is used
        pref = Preferences.userNodeForPackage(Employee.class);

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "FROM Item as i JOIN FETCH i.itemStoreList as s " +
                            "WHERE store_ID = :idStore " , Item.class
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


    public List<Item> getItemByStorage(Storage storage){
        Session session = App.getSession();
        List<Item> resultList = null;

        // only get item from store where app is used
        pref = Preferences.userNodeForPackage(Employee.class);

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "FROM Item as i JOIN FETCH i.itemStorageList as s " +
                            "WHERE storage_ID = :idStorage " , Item.class
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

    public List<Item> getAllItems(){
        Session session = App.getSession();
        List<Item> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Item> query = session.createQuery(
                    "FROM Item as i " , Item.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public Item getItemById(int id) {
        Session session = App.getSession();
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

    public Book getBookById(int id) {
        Session session = App.getSession();
        List<Book> resultList = null;
        Book result = null;
        try {
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Book> query = session.createQuery(
                    "from Book as b JOIN FETCH b.genreList " +
                            "where b.idItem = :id", Book.class
            );
            query.setParameter("id", id);
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

    public void transferStoreToStore(Item item, Store from, Store to, int quantity) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            // to
            List<Item_Store> desList = null;
            Item_Store des = null;
            Query<Item_Store> query = session.createQuery(
                    "from Item_Store  " +
                            "where item = :item AND store = :store", Item_Store.class
            );
            query.setParameter("item", item).setParameter("store", to);
            desList = query.list();
            if (!desList.isEmpty()) {                               // find and update
                des = desList.get(0);
                int newQuantity = des.getCount() + quantity;
                des.setCount(newQuantity);
                session.save(des);
            }
            else {                                                  // create new
                Item_StoreID id = new Item_StoreID(item.getIdItem(), to.getIdStore());
                Item_Store itemStore = new Item_Store(id, item, to, quantity);
                session.saveOrUpdate(itemStore);
            }

            // from
            List<Item_Store> startList = null;
            Item_Store start = null;
            Query<Item_Store> query2 = session.createQuery(
                    "from Item_Store  " +
                            "where item = :item AND store = :store", Item_Store.class
            );
            query2.setParameter("item", item).setParameter("store", from);
            startList = query2.list();
            if (!startList.isEmpty()) {                               // find and update
                start = startList.get(0);
                int newQuantity = start.getCount() - quantity;
                if (newQuantity == 0) {
                    session.delete(start);
                }
                else {
                    start.setCount(newQuantity);
                    session.save(start);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void transferStoreToStorage(Item item, Store from, Storage to, int quantity) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            // to
            List<Item_Storage> desList = null;
            Item_Storage des = null;
            Query<Item_Storage> query = session.createQuery(
                    "from Item_Storage  " +
                            "where item = :item AND storage = :storage", Item_Storage.class
            );
            query.setParameter("item", item).setParameter("storage", to);
            desList = query.list();
            if (!desList.isEmpty()) {                               // find and update
                des = desList.get(0);
                int newQuantity = des.getCount() + quantity;
                des.setCount(newQuantity);
                session.save(des);
            }
            else {                                                  // create new
                Item_StorageID id = new Item_StorageID(item.getIdItem(), to.getIdStorage());
                Item_Storage itemStore = new Item_Storage(id, item, to, quantity);
                session.saveOrUpdate(itemStore);
            }

            // from
            List<Item_Store> startList = null;
            Item_Store start = null;
            Query<Item_Store> query2 = session.createQuery(
                    "from Item_Store  " +
                            "where item = :item AND store = :store", Item_Store.class
            );
            query2.setParameter("item", item).setParameter("store", from);
            startList = query2.list();
            if (!startList.isEmpty()) {                               // find and update
                start = startList.get(0);
                int newQuantity = start.getCount() - quantity;
                if (newQuantity == 0) {
                    session.delete(start);
                }
                else {
                    start.setCount(newQuantity);
                    session.save(start);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void transferStorageToStore(Item item, Storage from, Store to, int quantity) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            // to
            List<Item_Store> desList = null;
            Item_Store des = null;
            Query<Item_Store> query = session.createQuery(
                    "from Item_Store  " +
                            "where item = :item AND store = :store", Item_Store.class
            );
            query.setParameter("item", item).setParameter("store", to);
            desList = query.list();
            if (!desList.isEmpty()) {                               // find and update
                des = desList.get(0);
                int newQuantity = des.getCount() + quantity;
                des.setCount(newQuantity);
                session.save(des);
            }
            else {                                                  // create new
                Item_StoreID id = new Item_StoreID(item.getIdItem(), to.getIdStore());
                Item_Store itemStore = new Item_Store(id, item, to, quantity);
                session.saveOrUpdate(itemStore);
            }

            // from
            List<Item_Storage> startList = null;
            Item_Storage start = null;
            Query<Item_Storage> query2 = session.createQuery(
                    "from Item_Storage  " +
                            "where item = :item AND storage = :storage", Item_Storage.class
            );
            query2.setParameter("item", item).setParameter("storage", from);
            startList = query2.list();
            if (!startList.isEmpty()) {                               // find and update
                start = startList.get(0);
                int newQuantity = start.getCount() - quantity;
                if (newQuantity == 0) {
                    session.delete(start);
                }
                else {
                    start.setCount(newQuantity);
                    session.save(start);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void transferStorageToStorage(Item item, Storage from, Storage to, int quantity) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            // to
            List<Item_Storage> desList = null;
            Item_Storage des = null;
            Query<Item_Storage> query = session.createQuery(
                    "from Item_Storage  " +
                            "where item = :item AND storage = :storage", Item_Storage.class
            );
            query.setParameter("item", item).setParameter("storage", to);
            desList = query.list();
            if (!desList.isEmpty()) {                               // find and update
                des = desList.get(0);
                int newQuantity = des.getCount() + quantity;
                des.setCount(newQuantity);
                session.save(des);
            }
            else {                                                  // create new
                Item_StorageID id = new Item_StorageID(item.getIdItem(), to.getIdStorage());
                Item_Storage itemStore = new Item_Storage(id, item, to, quantity);
                session.saveOrUpdate(itemStore);
            }

            // from
            List<Item_Storage> startList = null;
            Item_Storage start = null;
            Query<Item_Storage> query2 = session.createQuery(
                    "from Item_Storage  " +
                            "where item = :item AND storage = :storage", Item_Storage.class
            );
            query2.setParameter("item", item).setParameter("storage", from);
            startList = query2.list();
            if (!startList.isEmpty()) {                               // find and update
                start = startList.get(0);
                int newQuantity = start.getCount() - quantity;
                if (newQuantity == 0) {
                    session.delete(start);
                }
                else {
                    start.setCount(newQuantity);
                    session.save(start);
                }
            }

            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

}
