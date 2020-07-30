package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "item")
@Inheritance(strategy=InheritanceType.JOINED)
public class Item {
    @Id
    @Column(name = "idItem")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int idItem;

    @Column(name = "nameItem")
    protected String nameItem;

    @Column(name = "quantityItem")
    protected int quantityItem;

    @Column(name = "priceItem")
    protected float priceItem;

    @Column(name = "costItem")
    protected float costItem;

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

//    public Item(String nameItem, int priceItem){
//        this.nameItem = nameItem;
//        this.priceItem = priceItem;
//    }
//
    public Item(){

    }

    public Item(String nameItem, int quantityItem, float priceItem, float costItem) {
        this.nameItem = nameItem;
        this.quantityItem = quantityItem;
        this.priceItem = priceItem;
        this.costItem = costItem;
    }

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

    public float getCostItem() {
        return costItem;
    }

    public void setCostItem(float costItem) {
        this.costItem = costItem;
    }

    public Set<Store> getStoreList() {
        return storeList;
    }

    public void setStoreList(Set<Store> storeList) {
        this.storeList = storeList;
    }

    public Set<Storage> getStorageList() {
        return storageList;
    }

    public void setStorageList(Set<Storage> storageList) {
        this.storageList = storageList;
    }

    public int getQuantityItem() {
        return quantityItem;
    }

    public void setQuantityItem(int quantityItem) {
        this.quantityItem = quantityItem;
    }

    @Override
    public String toString() {
        return idItem +
                " - " + nameItem +
                " - Giá: " + priceItem;
    }

}