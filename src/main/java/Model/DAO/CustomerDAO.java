package Model.DAO;

import Main.App;
import Model.Class.Customer;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerDAO {
    public void createCustomer(Customer temp) {
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

    public void deleteCustomer(Customer customer){
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

    public void updateCustomer(Customer customer){
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

    public List<Customer> getCustomerByStore(Store store){
        Session session = App.getSession();
        List<Customer> resultList = null;
        try {
            session.getTransaction().begin();
            Query<Customer> query = session.createQuery("SELECT c " +
                    "FROM Customer as c " +
                    "WHERE c.store = :store ", Customer.class);
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