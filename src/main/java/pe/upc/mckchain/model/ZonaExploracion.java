package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "zonaexploracion")
@Getter
@Setter
public class ZonaExploracion implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_zonaexploracion")
    private UUID idZonaExploracion;

    @Column(name = "nombre_zonaexploracion")
    private String nombreZonaExploracion;

    @Column(name = "descripcion_zonaexploracion", columnDefinition = "text")
    private String descripcionZonaExploracion;

    @Column(name = "estado_zonaexploracion", length = 30)
    private String estadoZonaExploracion;

    @Column(name = "fecharegistro_zonaexploracion")
    private LocalDateTime fecharegistroZonaExploracion;

    @ManyToOne
    @JoinTable(name = "minera_zonasexploracion",
            joinColumns = @JoinColumn(name = "id_zonaexploracion", referencedColumnName = "id_zonaexploracion"),
            inverseJoinColumns = @JoinColumn(name = "id_minera", referencedColumnName = "id_usuario"))
    private Usuario mineraZonaExploracion;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "zonaexploracionMineral")
    Set<Mineral> mineralesZonaExploracion;

    //Constructores
    public ZonaExploracion() {
    }

    public ZonaExploracion(String nombreZonaExploracion, String descripcionZonaExploracion, String estadoZonaExploracion,
                           LocalDateTime fecharegistroZonaExploracion, Usuario mineraZonaExploracion) {
        this.nombreZonaExploracion = nombreZonaExploracion;
        this.descripcionZonaExploracion = descripcionZonaExploracion;
        this.estadoZonaExploracion = estadoZonaExploracion;
        this.fecharegistroZonaExploracion = fecharegistroZonaExploracion;
        this.mineraZonaExploracion = mineraZonaExploracion;
    }
}
