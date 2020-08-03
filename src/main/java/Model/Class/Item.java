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

    @Column(name = "priceItem")
    protected float priceItem;

    @Column(name = "costItem")
    protected float costItem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL)
    private Set<Item_Store> itemStoreList = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item", cascade = CascadeType.ALL)
    private Set<Item_Storage> itemStorageList = new HashSet<>();

//    public Item(String nameItem, int priceItem){
//        this.nameItem = nameItem;
//        this.priceItem = priceItem;
//    }
//

    @Transient                          // temperary count
    private int quantityItem = 0;

    public Item(){

    }

    public Item(String nameItem, float priceItem, float costItem) {
        this.nameItem = nameItem;
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

    public Set<Item_Store> getItemStoreList() {
        return itemStoreList;
    }

    public void setItemStoreList(Set<Item_Store> itemStoreList) {
        this.itemStoreList = itemStoreList;
    }

    public Set<Item_Storage> getItemStorageList() {
        return itemStorageList;
    }

    public void setItemStorageList(Set<Item_Storage> itemStorageList) {
        this.itemStorageList = itemStorageList;
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