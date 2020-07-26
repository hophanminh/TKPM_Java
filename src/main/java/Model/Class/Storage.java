package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @Column(name = "idStorage")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idStorage;

    @Column(name = "nameStorage")
    private String nameStorage;

    @Column(name = "addressStorage")
    private String addressStorage;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "storage")
    private Set<Employee> employeeList = new HashSet<>();

    @ManyToMany(mappedBy = "storageList")
    private Set<Store> storeList = new HashSet<>();

    @ManyToMany(mappedBy = "storageList")
    private Set<Item> itemList = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;

    public int getIdStorage() {
        return idStorage;
    }

    public void setIdStorage(int idStorage) {
        this.idStorage = idStorage;
    }

    public String getNameStorage() {
        return nameStorage;
    }

    public void setNameStorage(String nameStorage) {
        this.nameStorage = nameStorage;
    }

    public Set<Employee> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(Set<Employee> employeeList) {
        this.employeeList = employeeList;
    }

    public Set<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(Set<Store> storeList) {
        this.storeList = storeList;
    }

    public Set<Item> getItemList() {
        return itemList;
    }

    public void setItemList(Set<Item> itemList) {
        this.itemList = itemList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getAddressStorage() {
        return addressStorage;
    }

    public void setAddressStorage(String addressStorage) {
        this.addressStorage = addressStorage;
    }
}
