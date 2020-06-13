package Class;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @Column(name="idDiscount")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int idDiscount;

    @Column (name = "startDate")
    private Date startDate;

    @Column (name = "endDate")
    private Date endDate;

    @Column (name = "discount")
    private int discount;

    @ManyToOne
    @JoinColumn(name = "idStore", nullable = false)
    private Store store;

    public int getIdDiscount() {
        return idDiscount;
    }

    public void setIdDiscount(int idDiscount) {
        this.idDiscount = idDiscount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
