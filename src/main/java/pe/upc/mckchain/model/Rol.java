package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import pe.upc.mckchain.enums.RolNombre;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "rol")
@Getter
@Setter
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private int idRol;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 30)
    private RolNombre nombreRol;

    @ManyToMany(mappedBy = "rolesUsuario")
    Set<Usuario> usuariosRoles;

    //Constructores
    public Rol() {
    }

    public Rol(RolNombre nombreRol) {
        this.nombreRol = nombreRol;
    }
}
