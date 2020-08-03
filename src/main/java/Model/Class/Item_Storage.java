package Model.Class;

import javax.persistence.*;

@Entity
@Table(name = "Item_Storage")
public class Item_Storage {
    @EmbeddedId
    private Item_StorageID ID;

    @ManyToOne
    @MapsId("item_ID")
    @JoinColumn(name = "item_ID")
    private Item item;

    @ManyToOne
    @MapsId("storage_ID")
    @JoinColumn(name = "storage_ID")
    private Storage storage;

    @Column(name = "count")
    private int count;

    public Item_Storage() {
    }

    public Item_Storage(Item_StorageID ID, Item item, Storage storage, int count) {
        this.ID = ID;
        this.item = item;
        this.storage = storage;
        this.count = count;
    }

    public Item_StorageID getID() {
        return ID;
    }

    public void setID(Item_StorageID ID) {
        this.ID = ID;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
