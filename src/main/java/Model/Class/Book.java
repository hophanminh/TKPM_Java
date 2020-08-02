package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name ="book")
public class Book extends Item {
    @Column(name = "authorBook")
    private String authorBook;

    @Column(name = "descriptionBook")
    private String descriptionBook;

    @Column(name = "pulisherBook")
    private String pulisherBook;

    @Column(name = "yearBook")
    private int year;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_has_genre",
            joinColumns = {@JoinColumn(name = "book_idItem")},
            inverseJoinColumns = {@JoinColumn(name = "genre_idGenre")}
    )
    Set<Genre> genreList = new HashSet<>();

    public Book(){
      super();
    }

    public Book(String nameItem, int quantityItem, float priceItem,
                float costItem, String authorBook, String descriptionBook, String pulisherBook, int year, Set<Genre> genreList) {
        super(nameItem, quantityItem, priceItem, costItem);
        this.authorBook = authorBook;
        this.descriptionBook = descriptionBook;
        this.pulisherBook = pulisherBook;
        this.year = year;
        this.genreList = genreList;
    }

    public String getAuthorBook() {
        return authorBook;
    }

    public void setAuthorBook(String authorBook) {
        this.authorBook = authorBook;
    }

    public String getDescriptionBook() {
        return descriptionBook;
    }

    public void setDescriptionBook(String descriptionBook) {
        this.descriptionBook = descriptionBook;
    }

    public String getPulisherBook() {
        return pulisherBook;
    }

    public void setPulisherBook(String pulisherBook) {
        this.pulisherBook = pulisherBook;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {

        return  idItem +
                " - " + nameItem +
                " - Giá: " + priceItem +
                " - Tác giả: " + authorBook;
    }
}
