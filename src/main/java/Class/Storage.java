package Class;

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

    @Column(name = "addressStorage")
    private String addressStorage;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "storage")
    private Set<Employee> employeeList = new HashSet<>();

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

    public String getAddressStorage() {
        return addressStorage;
    }

    public void setAddressStorage(String addressStorage) {
        this.addressStorage = addressStorage;
    }
}
