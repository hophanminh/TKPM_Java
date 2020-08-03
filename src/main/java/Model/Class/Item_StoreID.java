package Model.Class;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class Item_StoreID implements Serializable {      // this class is for when a table has 2 primary key
    @Column(name = "item_ID")
    private int item_ID;

    @Column(name = "store_ID")
    private int store_ID;

    public Item_StoreID() {

    }

    public Item_StoreID(int item_ID, int store_ID) {
        this.item_ID = item_ID;
        this.store_ID = store_ID;
    }

    public int getItem_ID() {
        return item_ID;
    }

    public void setItem_ID(int item_ID) {
        this.item_ID = item_ID;
    }

    public int getStore_ID() {
        return store_ID;
    }

    public void setStore_ID(int store_ID) {
        this.store_ID = store_ID;
    }
}