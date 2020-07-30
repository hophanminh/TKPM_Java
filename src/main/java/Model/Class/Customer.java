package Model.Class;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "idCustomer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCustomer;

    @Column(name = "nameCustomer")
    private String nameCustomer;

    @Column(name = "dobCustomer")
    private Date dobCustomer;

    @Column(name = "emailCustomer")
    private String emailCustomer;

    @Column(name = "identifyIDCustomer")
    private String identifyIDCustomer;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private Set<Bill>billList =new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "idStore", nullable = false)
    private Store store;

    public Customer(){

    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public Date getDobCustomer() {
        return dobCustomer;
    }

    public void setDobCustomer(Date dobCustomer) {
        this.dobCustomer = dobCustomer;
    }

    public String getEmailCustomer() {
        return emailCustomer;
    }

    public void setEmailCustomer(String emailCustomer) {
        this.emailCustomer = emailCustomer;
    }

    public String getIdentifyIDCustomer() {
        return identifyIDCustomer;
    }

    public void setIdentifyIDCustomer(String identifyIDCustomer) {
        this.identifyIDCustomer = identifyIDCustomer;
    }
}
