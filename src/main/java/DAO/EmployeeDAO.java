package DAO;
import Class.*;
import Main.*;

import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.List;

public class EmployeeDAO {
    public EmployeeDAO(){
    }

    public Boolean checkLogin(String name, String pass){

        // get global session
        Session session = Main.getSession();

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
}
