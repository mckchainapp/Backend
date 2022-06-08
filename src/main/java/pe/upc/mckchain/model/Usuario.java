package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter
@Setter
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_usuario")
    private UUID idUsuario;

    @Column(name = "nombre_usuario", length = 100)
    private String nombreUsuario;

    @Column(name = "apellido_usuario", length = 50)
    private String apellidoUsuario;

    @Column(name = "ruc_usuario", length = 20)
    private String rucUsuario;

    @Column(name = "email_usuario", length = 50)
    private String emailUsuario;

    @Column(name = "username_usuario", length = 30)
    private String usernameUsuario;

    @Column(name = "password_usuario")
    private String passwordUsuario;

    @Column(name = "estado_usuario", length = 20)
    private String estadoUsuario;

    @Column(name = "telefono_usuario", length = 20)
    private String telefonoUsuario;

    @Column(name = "web_usuario")
    private String webUsuario;

    @Column(name = "fecharegistro_usuario")
    private LocalDate fecharegistroUsuario;

    @ManyToMany
    @JoinTable(name = "usuarios_roles",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol", referencedColumnName = "id_rol"))
    private Set<Rol> rolesUsuario = new HashSet<>();

    @ManyToOne
    @JoinTable(name = "mckchain_mineras",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_mckchain", referencedColumnName = "id_usuario"))
    private Usuario mckchainMinera;

    @ManyToOne
    @JoinTable(name = "minera_usuarios",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_minera", referencedColumnName = "id_usuario"))
    private Usuario mineraUsuario;

    @ManyToOne
    @JoinTable(name = "mckchain_compradores",
            joinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_mckchain", referencedColumnName = "id_usuario"))
    private Usuario mckchainComprador;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "mckchainMinera")
    private Set<Usuario> minerasMckchain;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "mineraUsuario")
    private Set<Usuario> usuariosMinera;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "mckchainComprador")
    private Set<Usuario> compradoresMckchain;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "usuarioUtilityToken")
    private Set<UtilityToken> utilitytokensUsuario;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "mineraZonaExploracion")
    private Set<ZonaExploracion> zonasexploracionMinera;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "compradorPedido")
    private Set<Pedido> pedidosComprador;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "encargadolaboratorioAnalisisLey")
    private Set<AnalisisLey> analisisleyEncargadoLaboratorio;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "usuarioImagen")
    private Imagen imagenUsuario;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "mineraData")
    private Minera dataMinera;

    //Constructores
    public Usuario() {
    }

    //---Signup Single Request
    public Usuario(String emailUsuario, String estadoUsuario) {
        this.emailUsuario = emailUsuario;
        this.estadoUsuario = estadoUsuario;
    }

    //---Signup Comprador Request
    public Usuario(String nombreUsuario, String apellidoUsuario, String rucUsuario, String emailUsuario,
                   String usernameUsuario, String passwordUsuario, String estadoUsuario, Usuario mckchainComprador) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.rucUsuario = rucUsuario;
        this.emailUsuario = emailUsuario;
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
        this.estadoUsuario = estadoUsuario;
        this.mckchainComprador = mckchainComprador;
    }
}
