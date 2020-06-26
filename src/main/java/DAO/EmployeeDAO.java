package DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

import javax.persistence.Query;
import java.util.List;

public class EmployeeDAO {




    public EmployeeDAO(){
    }

    public String getPassword(String nameEmployee1){

        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();

        try{
            session.getTransaction().begin();

            String sql = "select e.passwordEmployee from Employee e where e.nameEmployee = \'"+ nameEmployee1 + "\'";
            Query result = session.createSQLQuery(sql);
            String password = result.getResultList().get(0).toString();
            System.out.println(password);
            return password;
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return "CANNOT FIND";
    }

    public List<String> getNameEmployee(){

        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        
        try{
            session.getTransaction().begin();

            String sql ="SELECT e.nameEmployee FROM employee e";

            List<String> list = session.createSQLQuery(sql).getResultList();
            return list;

        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return null;

    }
}
