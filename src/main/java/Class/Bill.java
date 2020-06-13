package Class;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bill")
public class Bill {
    @Id
    @Column(name = "idBill")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idBill;

    @Column(name = "totalBill")
    private int totalBill;

    @Column(name = "dateBill")
    private Date dateBill;

    @Column(name = "madeBill")
    private int madeBill;

    @Column(name ="noteBill")
    private String noteBill;

    @ManyToOne
    @JoinColumn(name = "idCustomer", nullable = false)
    private Customer customer;

    public int getIdBill() {
        return idBill;
    }

    public void setIdBill(int idBill) {
        this.idBill = idBill;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public Date getDateBill() {
        return dateBill;
    }

    public void setDateBill(Date dateBill) {
        this.dateBill = dateBill;
    }

    public int getMadeBill() {
        return madeBill;
    }

    public void setMadeBill(int madeBill) {
        this.madeBill = madeBill;
    }

    public String getNoteBill() {
        return noteBill;
    }

    public void setNoteBill(String noteBill) {
        this.noteBill = noteBill;
    }
}
