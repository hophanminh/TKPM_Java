package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "store")
public class Store {
    @Id
    @Column(name = "idStore")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStore;

    @Column(name = "nameStore")
    private String nameStore;

    @Column(name = "addressStore")
    private String addressStore;

    @Column(name = "revenueStore")
    private int revenueStore;

    @Column(name = "outcomeStore")
    private int outcomeStore;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    private Set<Customer> customerList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    private Set<Employee> employeeList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
    private Set<Bill> billList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "store", cascade = CascadeType.ALL)
    protected Set<Item_Store> itemStoreList = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "store_has_storage",
            joinColumns = {@JoinColumn(name = "store_id")},
            inverseJoinColumns = {@JoinColumn(name = "storage_id")}
    )
    Set<Storage> storageList = new HashSet<>();

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

    public Set<Item_Store> getItemStoreList() {
        return itemStoreList;
    }

    public void setItemStoreList(Set<Item_Store> itemStoreList) {
        this.itemStoreList = itemStoreList;
    }

    public Set<Storage> getStorageList() {
        return storageList;
    }

    public void setStorageList(Set<Storage> storageList) {
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
        return nameStore + " - " + addressStore;
    }
}
