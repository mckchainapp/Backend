package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "distrito")
@Getter
@Setter
public class Distrito implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_distrito")
    private Long idDistrito;

    @Column(name = "nombre_distrito")
    private String nombreDistrito;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "distritoMinera")
    private Set<Minera> minerasDistrito;

    @ManyToOne
    @JoinTable(name = "provincia_distritos",
            joinColumns = @JoinColumn(name = "id_distrito", referencedColumnName = "id_distrito"),
            inverseJoinColumns = @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia"))
    private Provincia provinciaDistrito;

    //Constructores
    public Distrito() {
    }

    public Distrito(String nombreDistrito, Provincia provinciaDistrito) {
        this.nombreDistrito = nombreDistrito;
        this.provinciaDistrito = provinciaDistrito;
    }
}
