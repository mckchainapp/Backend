package pe.upc.mckchain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "mineral")
@Getter
@Setter
public class Mineral implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_mineral")
    private UUID idMineral;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "fechaextraccion_mineral")
    private LocalDate fechaextraccionMineral;

    @Column(name = "nombre_mineral")
    private String nombreMineral;

    @Column(name = "tipomuestra_mineral")
    private String tipomuestraMineral;

    @Column(name = "estado_mineral")
    private String estadoMineral;

    @Column(name = "fase_mineral")
    private String faseMineral;

    @Column(name = "habilitacionpedido_mineral")
    private boolean habilitacionpedidoMineral;

    @ManyToOne
    @JoinTable(name = "zonaexploracion_minerales",
            joinColumns = @JoinColumn(name = "id_mineral", referencedColumnName = "id_mineral"),
            inverseJoinColumns = @JoinColumn(name = "id_zonaexploracion", referencedColumnName = "id_zonaexploracion"))
    private ZonaExploracion zonaexploracionMineral;

    @OneToOne
    @JoinTable(name = "pedido_mineral",
            joinColumns = @JoinColumn(name = "id_mineral", referencedColumnName = "id_mineral"),
            inverseJoinColumns = @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido"))
    private Pedido pedidoMineral;

    //Constructores
    public Mineral() {
    }

    public Mineral(LocalDate fechaextraccionMineral, String nombreMineral, String tipomuestraMineral,
                   String estadoMineral, String faseMineral, ZonaExploracion zonaexploracionMineral,
                   boolean habilitacionpedidoMineral) {
        this.fechaextraccionMineral = fechaextraccionMineral;
        this.nombreMineral = nombreMineral;
        this.tipomuestraMineral = tipomuestraMineral;
        this.estadoMineral = estadoMineral;
        this.faseMineral = faseMineral;
        this.zonaexploracionMineral = zonaexploracionMineral;
        this.habilitacionpedidoMineral = habilitacionpedidoMineral;
    }
}
