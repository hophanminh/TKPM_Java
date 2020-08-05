package Model.DAO;

import Main.App;
import Model.Class.Discount;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class DiscountDAO {
    public void createDiscount(Discount temp) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(temp);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void deleteDiscount(Discount customer){
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.remove(customer);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void updateDiscount(Discount customer){
        Session session = App.getSession();
        try {
            session.getTransaction().begin();
            session.saveOrUpdate(customer);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Discount> getDiscountByStore(Store store){
        Session session = App.getSession();
        List<Discount> resultList = null;
        try {
            session.getTransaction().begin();
            Query<Discount> query = session.createQuery("SELECT d " +
                    "FROM Discount as d " +
                    "WHERE d.store = :store ", Discount.class);
            query.setParameter("store", store);
            resultList = query.list();
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }
}
