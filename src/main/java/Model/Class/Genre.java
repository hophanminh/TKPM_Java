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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "genre")
    private Set<Book_has_Genre> book_has_genres = new HashSet<>();

    public Genre(String nameGenre){
        this.nameGenre = nameGenre;
    }
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
