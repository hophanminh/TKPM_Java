package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book")
    Set<Book_has_Genre> book_has_genres = new HashSet<>();

    public Book(String nameItem, float costItem, float priceItem, int quantityItem, String authorBook, String descriptionBook, String pulisherBook, int yearBook) {
        super(nameItem, costItem, priceItem, quantityItem);
        this.authorBook = authorBook;
        this.pulisherBook = pulisherBook;
        this.year = yearBook;
    }

//    public Book(String nameBook, int priceBook){
//        super(nameBook, priceBook);
//    }

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
