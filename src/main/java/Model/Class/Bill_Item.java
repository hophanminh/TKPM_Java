package Model.Class;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.ReadOnlyFloatWrapper;
import javafx.beans.property.SimpleFloatProperty;

import javax.persistence.*;

@Entity
@Table(name = "Bill_Item")
public class Bill_Item {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @ManyToOne
    @JoinColumn(name = "item_ID")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "bill_ID")
    private Bill bill;

    @Column(name = "count")
    private int count;

    @Column(name = "discount")      // percent
    private float discount;

    @Column(name = "total")
    private float total;

    // for bill calculate only
    @Transient
    private final FloatProperty tempCount  = new SimpleFloatProperty();

    @Transient
    private final FloatProperty tempPrice  = new SimpleFloatProperty();

    @Transient
    private final FloatProperty tempDiscount  = new SimpleFloatProperty();

    @Transient
    private final ReadOnlyFloatWrapper tempTotal  = new ReadOnlyFloatWrapper();

    public Bill_Item() {
    }


    public Bill_Item(Item item, int count, float discount) {
        this.item = item;
        this.count = count;
        this.discount = discount;
        this.total = count * item.getPriceItem() * (100 - discount)/100;

        setTempCount(count);
        setTempPrice(item.getPriceItem());
        setTempDiscount(discount);
        this.tempTotal.bind(                                                    // total =
                this.tempCount.multiply(this.tempPrice).multiply(               //          count * price *
                        this.tempDiscount.negate().add(100).divide(100)));   //                            (100 - discount)/100
    }

    public Bill_Item(Item item, Bill bill, int count, float discount, float total) {
        this.item = item;
        this.bill = bill;
        this.count = count;
        this.discount = discount;
        this.total = total;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTempCount() {
        return tempCount.get();
    }

    public FloatProperty tempCountProperty() {
        return tempCount;
    }

    public void setTempCount(float tempCount) {
        this.tempCount.set(tempCount);
    }

    public float getTempPrice() {
        return tempPrice.get();
    }

    public FloatProperty tempPriceProperty() {
        return tempPrice;
    }

    public void setTempPrice(float tempPrice) {
        this.tempPrice.set(tempPrice);
    }

    public float getTempDiscount() {
        return tempDiscount.get();
    }

    public FloatProperty tempDiscountProperty() {
        return tempDiscount;
    }

    public void setTempDiscount(float tempDiscount) {
        this.tempDiscount.set(tempDiscount);
    }

    public float getTempTotal() {
        return tempTotal.get();
    }

    public ReadOnlyFloatWrapper tempTotalProperty() {
        return tempTotal;
    }

    public void setTempTotal(float tempTotal) {
        this.tempTotal.set(tempTotal);
    }
}
