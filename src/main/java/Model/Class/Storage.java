package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "storage")
public class Storage {
    @Id
    @Column(name = "idStorage")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idStorage;

    @Column(name = "nameStorage")
    private String nameStorage;

    @Column(name = "addressStorage")
    private String addressStorage;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "storage")
    private Set<Employee> employeeList = new HashSet<>();

    @ManyToMany(mappedBy = "storageList")
    private Set<Store> storeList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "storage", cascade = CascadeType.ALL)
    protected Set<Item_Storage> itemStorageList = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "idCompany")
    private Company company;

    public Storage(){

    }

    public Storage(String nameStorage, String addressStorage) {
        this.nameStorage = nameStorage;
        this.addressStorage = addressStorage;
    }

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

    public Set<Item_Storage> getItemStorageList() {
        return itemStorageList;
    }

    public void setItemStorageList(Set<Item_Storage> itemStorageList) {
        this.itemStorageList = itemStorageList;
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

    @Override
    public String toString() {
        return nameStorage + " - " + addressStorage;
    }

    @Override
    public boolean equals(Object other){
        if (this == other) {
            return true;
        }

        if ( !(other instanceof Storage) ) {
            return false;
        }

        final Storage storage = (Storage) other;
        if (this.idStorage != storage.getIdStorage() ||
            !storage.getNameStorage().equals(this.nameStorage) ||
            !storage.getAddressStorage().equals(this.addressStorage)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + this.idStorage;
        result = 31 * result + this.nameStorage.hashCode();
        result = 31 * result + this.addressStorage.hashCode();
        return result;
    }
}
