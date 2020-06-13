package Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="book")
public class Book extends Item {
    @Id
    @Column(name = "idBook")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idBook;

    @Column(name = "authorBook")
    private String authorBook;

    @Column(name = "descriptionBook")
    private String descriptionBook;

    @Column(name = "pulisherBook")
    private String pulisherBook;

    @Column(name = "yearBook")
    private int year;

    @ManyToOne
    @JoinColumn(name = "idItem", nullable = false)
    private Item item;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "book_has_genre",
            joinColumns = {@JoinColumn(name = "book_idBook")},
            inverseJoinColumns = {@JoinColumn(name = "genre_idGenre")}
    )
    Set<Genre> genreList = new HashSet<>();

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
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
}
