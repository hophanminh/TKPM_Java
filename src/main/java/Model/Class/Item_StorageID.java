package Model.Class;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Item_StorageID implements Serializable {      // this class is for when a table has 2 primary key
    @Column(name = "item_ID")
    private int item_ID;

    @Column(name = "storage_ID")
    private int storage_ID;

    public Item_StorageID() {

    }

    public Item_StorageID(int item_ID, int storage_ID) {
        this.item_ID = item_ID;
        this.storage_ID = storage_ID;
    }

    public int getItem_ID() {
        return item_ID;
    }

    public void setItem_ID(int item_ID) {
        this.item_ID = item_ID;
    }

    public int getStorage_ID() {
        return storage_ID;
    }

    public void setStorage_ID(int storage_ID) {
        this.storage_ID = storage_ID;
    }
}