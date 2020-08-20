package Model.DAO;

import Main.App;
import Model.Class.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class BillDAO {
    public void insert(Bill bill, List<Bill_Item> list) {
        Session session = App.getSession();
        try{
            session.getTransaction().begin();
            session.save(bill);
            for (Bill_Item o : list) {
                Bill_Item temp = new Bill_Item(o.getItem(), bill, o.getCount(), o.getDiscount(), o.getTotal());
                session.save(temp);
            }
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            session.getTransaction().rollback();
        }
    }

    public void delete(Bill customer){
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

    public void update(Bill customer){
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

    public List<Object[]> calculateSaleByYear(int year, int idStore) {
        Session session = App.getSession();
        List<Object[]> resultList = null;
        try {
            session.getTransaction().begin();

            Query nativeQuery = session.createNativeQuery(
                    "select year(dateBill),month(dateBill), sum(paidBill) " +
                    "from bill as b " +
                    "where year(dateBill) = :year and idStore = :idStore " +
                    "group by year(dateBill),month(dateBill) " +
                    "order by year(dateBill),month(dateBill); "
            );
            nativeQuery.setParameter("year", year).setParameter("idStore", idStore);
            resultList = nativeQuery.list();
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        return resultList;
    }
}
