package Model.Class;

import javax.persistence.*;

@Entity
@Table(name = "book_has_genre")
public class Book_has_Genre {
    @Id
    @Column(name = "idBook_has_genre")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idBook_has_genre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "book_idBook")
    private Book book;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "genre_idGenre")
    private Genre genre;

    public Book_has_Genre(Book book, Genre genre) {
        this.book = book;
        this.genre = genre;
    }

    public int getIdBook_has_genre() {
        return idBook_has_genre;
    }

    public void setIdBook_has_genre(int idBook_has_genre) {
        this.idBook_has_genre = idBook_has_genre;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
