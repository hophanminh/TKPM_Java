package Model.Class;

import javax.persistence.*;

@Entity
@Table(name = "Item_Store")
public class Item_Store {
    @EmbeddedId
    private Item_StoreID ID;

    @ManyToOne
    @MapsId("item_ID")
    @JoinColumn(name = "item_ID")
    private Item item;

    @ManyToOne
    @MapsId("store_ID")
    @JoinColumn(name = "store_ID")
    private Store store;

    @Column(name = "count")
    private int count;

    public Item_Store() {
    }

    public Item_Store(Item_StoreID ID, Item item, Store store, int count) {
        this.ID = ID;
        this.item = item;
        this.store = store;
        this.count = count;
    }

    public Item_StoreID getID() {
        return ID;
    }

    public void setID(Item_StoreID ID) {
        this.ID = ID;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
