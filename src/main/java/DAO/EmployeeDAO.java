package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;
import Class.Employee;

import javax.persistence.Query;
import java.util.List;

public class EmployeeDAO {


    SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    Session session = sessionFactory.getCurrentSession();


    public EmployeeDAO(){
    }

    public String getPassword(String nameEmployee){

        try{
            session.getTransaction().begin();

            String sql = "SELECT employee.passwordEmployee " +
                    "   FROM employee " +
                    "   WHERE employee.nameEmployee == " + nameEmployee ;
            Query result = session.createQuery(sql);
            String password = result.getResultList().get(0).toString();
            return password;
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return "CANNOT FIND";
    }

    public List<Employee> getListEmployee(){
        try{
            session.getTransaction().begin();

            String sql ="SELECT s FROM employee s" ;

            Query<Employee> result = session.createQuery(sql);

            List<Employee> list = result.getResultList();
            return list;
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
        return null;

    }
}
