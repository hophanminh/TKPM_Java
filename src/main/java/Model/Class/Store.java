package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store")
public class Store {
    @Id
    @Column(name = "idStore")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idStore;

    @Column(name = "nameStore")
    private String nameStore;

    @Column(name = "addressStore")
    private String addressStore;

    @Column(name = "revenueStore")
    private int revenueStore;

    @Column(name = "outcomeStore")
    private int outcomeStore;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "store")
    private Set<Discount> discountList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    private Set<Customer> customerList = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "store")
    private Set<Employee> employeeList = new HashSet<>();

    @ManyToMany(mappedBy = "storeList")
    private Set<Item> itemList = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "store_has_storage",
            joinColumns = {@JoinColumn(name = "store_id")},
            inverseJoinColumns = {@JoinColumn(name = "storage_id")}
    )
    Set<Store> storageList = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "idCompany")
    private Company company;

    public Store() {

    }

    public Store(String nameStore, String addressStore) {
        this.nameStore = nameStore;
        this.addressStore = addressStore;
    }

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public String getNameStore() {
        return nameStore;
    }

    public void setNameStore(String nameStore) {
        this.nameStore = nameStore;
    }

    public String getAddressStore() {
        return addressStore;
    }

    public void setAddressStore(String addressStore) {
        this.addressStore = addressStore;
    }

    public int getRevenueStore() {
        return revenueStore;
    }

    public void setRevenueStore(int revenueStore) {
        this.revenueStore = revenueStore;
    }

    public int getOutcomeStore() {
        return outcomeStore;
    }

    public void setOutcomeStore(int outcomeStore) {
        this.outcomeStore = outcomeStore;
    }

    public Set<Discount> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(Set<Discount> discountList) {
        this.discountList = discountList;
    }

    public Set<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(Set<Customer> customerList) {
        this.customerList = customerList;
    }

    public Set<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(Set<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Set<Item> getItemList() {
        return itemList;
    }

    public void setItemList(Set<Item> itemList) {
        this.itemList = itemList;
    }

    public Set<Store> getStorageList() {
        return storageList;
    }

    public void setStorageList(Set<Store> storageList) {
        this.storageList = storageList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return idStore + " - " + addressStore;
    }
}
