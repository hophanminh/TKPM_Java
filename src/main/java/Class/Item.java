package Class;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@Inheritance(strategy=InheritanceType.JOINED)
public class Item {
    @Id
    @Column(name = "idItem")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int idItem;

    @Column(name = "nameItem")
    protected String nameItem;

    @Column(name = "quantityItem")
    protected int quantityItem;

    @Column(name = "priceItem")
    protected float priceItem;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "store_has_item",
            joinColumns = {@JoinColumn(name = "item_idItem")},
            inverseJoinColumns = {@JoinColumn(name = "store_idStore")}
    )
    Set<Store> storeList = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "storage_has_item",
            joinColumns = {@JoinColumn(name = "item_idItem")},
            inverseJoinColumns = {@JoinColumn(name = "storage_idStore")}
    )
    Set<Storage> storageList = new HashSet<>();

    @Transient
    IntegerProperty quantity = new SimpleIntegerProperty(1);


    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getNameItem() {
        return nameItem;
    }

    public void setNameItem(String nameItem) {
        this.nameItem = nameItem;
    }

    public float getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(float priceItem) {
        this.priceItem = priceItem;
    }

    public int getQuantityItem() {
        return quantityItem;
    }

    public void setQuantityItem(int quantityItem) {
        this.quantityItem = quantityItem;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    @Override
    public String toString() {
        return idItem +
                " - " + nameItem +
                " - " + priceItem +
                " - Remain: " + quantityItem;
    }

}