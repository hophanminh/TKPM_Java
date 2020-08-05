package Model.DAO;

import Main.App;
import Model.Class.Bill;
import Model.Class.Store;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BillDAO {
    public void createBll(Bill temp) {
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

    public void deleteBill(Bill customer){
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

    public void updateBill(Bill customer){
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

    public List<Bill> getBillByStore(Store store){
        Session session = App.getSession();
        List<Bill> resultList = null;
        try {
            session.getTransaction().begin();
            Query<Bill> query = session.createQuery("SELECT b " +
                    "FROM Bill as b " +
                    "WHERE b.store = :store ", Bill.class);
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
