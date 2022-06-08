package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "provincia")
@Getter
@Setter
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_provincia")
    private Long idProvincia;

    @Column(name = "nombre_provincia")
    private String nombreProvincia;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "provinciaDistrito")
    private Set<Distrito> distritosProvincia;

    @ManyToOne
    @JoinTable(name = "departamento_provincias",
            joinColumns = @JoinColumn(name = "id_provincia", referencedColumnName = "id_provincia"),
            inverseJoinColumns = @JoinColumn(name = "id_departamento", referencedColumnName = "id_departamento"))
    private Departamento departamentoProvincia;

    //Constructores
    public Provincia() {
    }

    public Provincia(String nombreProvincia, Departamento departamentoProvincia) {
        this.nombreProvincia = nombreProvincia;
        this.departamentoProvincia = departamentoProvincia;
    }
}
