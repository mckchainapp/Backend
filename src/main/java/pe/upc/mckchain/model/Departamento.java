package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "departamento")
@Getter
@Setter
public class Departamento implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_departamento")
    private Long idDepartamento;

    @Column(name = "nombre_departamento")
    private String nombreDepartamento;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "departamentoProvincia")
    private Set<Provincia> provinciasDepartamento;

    //Constructores
    public Departamento() {
    }

    public Departamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }
}
