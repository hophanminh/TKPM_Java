package Class;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store_has_item")
public class StoreHasItem {
    @Id
    @Column(name = "store_idStore")
    private int idStore;

    @Id
    @Column(name = "item_idItem")
    private int idItem;

    public int getIdStore() {
        return idStore;
    }

    public void setIdStore(int idStore) {
        this.idStore = idStore;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }
}
