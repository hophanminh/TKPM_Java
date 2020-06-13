package Class;

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

    @ManyToOne
    @JoinColumn(name = "idCompany", nullable = false)
    private Company company;

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
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
}
