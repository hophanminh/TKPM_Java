package Model.DAO;

import Main.App;
import Model.Class.Customer;
import Model.Class.Employee;
import Model.Class.Item;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.prefs.Preferences;

public class CustomerDAO {
    Preferences pref;

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

    public List<Customer> getAllCustomerDefault(){
        Session session = App.getSession();
        List<Customer> resultList = null;

        // only get item from store where app is used
        pref = Preferences.userNodeForPackage(Employee.class);
        int idStore = pref.getInt("defaultStore", -1);

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Customer> query = session.createQuery(
                    "FROM Customer as c JOIN FETCH c.store as s " +
                            "WHERE s.idStore = :idStore " , Customer.class
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

    public Customer getCustomerByID(int id){
        Session session = App.getSession();
        List<Customer> resultList = null;
        Customer result = null;
        try{
            session.getTransaction().begin();

            Query<Customer> query = session.createQuery(
                    "from Customer " +
                            "where idCustomer = :id", Customer.class
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

    public List<Object[]> getSearchCustomerList(String searchText){
        Session session = App.getSession();
        List<Object[]> resultList = null;
        try{
            session.getTransaction().begin();
            String query = "SELECT c.* " +
                    "FROM customer c " +
                    "WHERE MATCH(c.emailCustomer, c.nameCustomer, c.identifyIDCustomer) " +
                    "AGAINST('" + searchText.trim() + "' IN BOOLEAN MODE)";

            resultList = session.createNativeQuery(query)
                    .list();
            session.getTransaction().commit();
        } catch (Exception ex){
            ex.printStackTrace();
            session.getTransaction().rollback();
        } finally{
            session.close();
        }
        return resultList;

    }
}
