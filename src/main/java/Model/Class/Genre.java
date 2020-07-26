package Model.Class;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genre")
public class Genre {
    @Id
    @Column(name = "idGenre")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int idGenre;

    @Column(name = "nameGenre")
    private String nameGenre;

    @ManyToMany(mappedBy = "genreList")
    private Set<Book> bookList = new HashSet<>();

    public int getIdGenre() {
        return idGenre;
    }

    public void setIdGenre(int idGenre) {
        this.idGenre = idGenre;
    }

    public String getNameGenre() {
        return nameGenre;
    }

    public void setNameGenre(String nameGenre) {
        this.nameGenre = nameGenre;
    }
}
