package Class;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "storage_has_item")
public class StorageHasItem {
    @Id
    @Column(name = "storage_idStorage")
    private int idStorage;

    @Id
    @Column(name = "item_idItem")
    private int idItem;

    public int getIdStorage() {
        return idStorage;
    }

    public void setIdStorage(int idStorage) {
        this.idStorage = idStorage;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }
}
