package Class;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "book_has_genre")
public class BookHasGenre {
    @Id
    @Column(name = "book_idBook")
    private int idBook;

    @Id
    @Column(name = "genre_idGenre")
    private int idGenre;

    public int getIdBook() {
        return idBook;
    }

    public void setIdBook(int idBook) {
        this.idBook = idBook;
    }

    public int getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(int idGenre) {
        this.idGenre = idGenre;
    }
}
