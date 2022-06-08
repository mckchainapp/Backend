package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "imagen")
@Getter
@Setter
public class Imagen implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_imagen")
    private UUID idImagen;

    @Column(name = "nombre_imagen")
    private String nombreImagen;

    @Column(name = "tipoarchivo_imagen", length = 20)
    private String tipoarchivoImagen;

    @Column(name = "url_imagen")
    private String urlImagen;

    @Column(name = "archivo_imagen")
    private byte[] archivoImagen;

    @OneToOne
    @JoinTable(name = "usuario_imagen",
            joinColumns = @JoinColumn(name = "id_imagen", referencedColumnName = "id_imagen"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"))
    private Usuario usuarioImagen;

    //Constructores
    public Imagen() {
    }

    public Imagen(String nombreImagen, String tipoarchivoImagen, String urlImagen, byte[] archivoImagen,
                  Usuario usuarioImagen) {
        this.nombreImagen = nombreImagen;
        this.tipoarchivoImagen = tipoarchivoImagen;
        this.urlImagen = urlImagen;
        this.archivoImagen = archivoImagen;
        this.usuarioImagen = usuarioImagen;
    }
}
