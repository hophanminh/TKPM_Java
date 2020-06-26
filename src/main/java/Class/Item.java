package Class;

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

    @Column(name = "priceItem")
    protected int priceItem;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "store_has_item",
            joinColumns = {@JoinColumn(name = "store_idStore")},
            inverseJoinColumns = {@JoinColumn(name = "item_idItem")}
    )
    Set<Genre> storeList = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "storage_has_item",
            joinColumns = {@JoinColumn(name = "storage_idStore")},
            inverseJoinColumns = {@JoinColumn(name = "item_idItem")}
    )
    Set<Genre> storageList = new HashSet<>();
//
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "item")
//    protected Set<Book> bookList = new HashSet<>();

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

    public int getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(int priceItem) {
        this.priceItem = priceItem;
    }
}
