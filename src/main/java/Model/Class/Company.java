package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @Column(name = "idCompany")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idCompany;

    @Column(name = "nameCompany")
    private String nameCompany;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    private Set<Store> storeList = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    private Set<Storage> storageList = new HashSet<>();

    public int getIdCompany() {
        return idCompany;
    }

    public void setIdCompany(int idCompany) {
        this.idCompany = idCompany;
    }

    public String getNameCompany() {
        return nameCompany;
    }

    public void setNameCompany(String nameCompany) {
        this.nameCompany = nameCompany;
    }
}
