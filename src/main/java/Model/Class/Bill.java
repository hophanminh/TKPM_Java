package Model.Class;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @Column(name = "idBill")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBill;

    @Column(name = "paidBill")          // final price
    private float paidBill;

    @Column(name = "totalBill")         // total price without discount
    private float totalBill;

    @Column(name = "totalDiscountBill")     // total discount
    private float totalDiscountBill;

    @Column(name = "dateBill")
    private LocalDateTime dateBill;

    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "idStore")
    private Store store;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bill", cascade = CascadeType.ALL)
    protected Set<Bill_Item> billItemList = new HashSet<>();

    public Bill(){

    }

    public Bill(float paidBill, float totalBill, float totalDiscountBill, LocalDateTime dateBill, Customer customer, Employee employee, Store store, Set<Bill_Item> billItemList) {
        this.paidBill = paidBill;
        this.totalBill = totalBill;
        this.totalDiscountBill = totalDiscountBill;
        this.dateBill = dateBill;
        this.customer = customer;
        this.employee = employee;
        this.store = store;
        this.billItemList = billItemList;
    }

    public Bill(float paidBill, float totalBill, float totalDiscountBill, LocalDateTime dateBill,
                Customer customer, Employee employee, Store store) {
        this.paidBill = paidBill;
        this.totalBill = totalBill;
        this.totalDiscountBill = totalDiscountBill;
        this.dateBill = dateBill;
        this.customer = customer;
        this.employee = employee;
        this.store = store;
    }

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public float getPaidBill() {
        return paidBill;
    }

    public void setPaidBill(float paidBill) {
        this.paidBill = paidBill;
    }

    public float getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(float totalBill) {
        this.totalBill = totalBill;
    }

    public float getTotalDiscountBill() {
        return totalDiscountBill;
    }

    public void setTotalDiscountBill(float totalDiscountBill) {
        this.totalDiscountBill = totalDiscountBill;
    }

    public LocalDateTime getDateBill() {
        return dateBill;
    }

    public void setDateBill(LocalDateTime dateBill) {
        this.dateBill = dateBill;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Set<Bill_Item> getBillItemList() {
        return billItemList;
    }

    public void setBillItemList(Set<Bill_Item> billItemList) {
        this.billItemList = billItemList;
    }
}
