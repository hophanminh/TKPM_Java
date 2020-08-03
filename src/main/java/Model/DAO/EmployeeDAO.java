package Model.DAO;

import Main.App;
import Model.Class.Employee;
import Model.Class.Storage;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.prefs.Preferences;

public class EmployeeDAO {
    Preferences pref;
    public EmployeeDAO(){
    }

    public Employee getEmployeeByUsername(String username){
        Session session = App.getSession();
        List<Employee> resultList = null;
        Employee result = null;
        try{
            session.getTransaction().begin();

            Query<Employee> query = session.createQuery(
                    "from Employee " +
                            "where username = :username", Employee.class
            );
            query.setParameter("username", username);
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

    public List<Employee> getAllEmployee(){
        Session session = App.getSession();
        List<Employee> resultList = null;

        // only get item from store where app is used
        pref = Preferences.userNodeForPackage(Employee.class);
        int idStore = pref.getInt("defaultStore", -1);

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Employee> query = session.createQuery(
                    "FROM Employee " , Employee.class
            );
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Employee> getEmployeeByStore(Store store){
        Session session = App.getSession();
        List<Employee> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Employee> query = session.createQuery("SELECT e "+
                    "FROM Employee as e " +
                            "WHERE e.store = :idStore " , Employee.class
            );
            query.setParameter("idStore", store);
            resultList = query.list();
            System.out.println(resultList);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public List<Employee> getEmployeeByStorage(Storage storage){
        Session session = App.getSession();
        List<Employee> resultList = null;

        try{
            session.getTransaction().begin();

            // get all Item and book from database
            Query<Employee> query = session.createQuery("SELECT e "+
                    "FROM Employee as e " +
                    "WHERE e.storage = :idStorage " , Employee.class
            );
            query.setParameter("idStorage", storage);
            resultList = query.list();

            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }

    public Boolean checkLogin(String name, String pass){

        // get global session
        Session session = App.getSession();

        try{
            session.getTransaction().begin();

            Query<Employee> query = session.createQuery(
                    "from Employee e " +
                            "where e.nameEmployee = :name AND e.passwordEmployee = :pass", Employee.class
            );
            query.setParameter("name",name).setParameter("pass",pass);
            List<Employee> resultList = query.list();

            session.getTransaction().commit();

            // if empty -> no account with input name and password
            if (resultList.isEmpty()) {
                return false;
            }
            else {
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return false;
    }

    public void updateEmployee(Employee temp) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.saveOrUpdate(temp);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public List<Object[]> getSearchEmployeeList(String searchText){
        Session session = App.getSession();
        List<Object[]> resultList = null;
        try{
            session.getTransaction().begin();
            String query = "SELECT e.* " +
                    "FROM employee e " +
                    "WHERE MATCH(e.name, e.phone) " +
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
