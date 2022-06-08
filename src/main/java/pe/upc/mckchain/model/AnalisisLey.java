package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analisisley")
@Getter
@Setter
public class AnalisisLey implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_analisisley")
    private UUID idAnalisisLey;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "analisisleyPedido")
    private Pedido pedidoAnalisisLey;

    @ManyToOne
    @JoinTable(name = "encargadolaboratorio_analisisley",
            joinColumns = @JoinColumn(name = "id_analisisley", referencedColumnName = "id_analisisley"),
            inverseJoinColumns = @JoinColumn(name = "id_encargadolaboratorio", referencedColumnName = "id_usuario"))
    private Usuario encargadolaboratorioAnalisisLey;

    @Column(name = "peso_analisisley")
    private String pesoAnalisisLey;

    @Column(name = "distribuciongranulometrica_analisisley")
    private String distribuciongranulometricaAnalisisLey;

    @Column(name = "humedad_analisisley")
    private String humedadAnalisisLey;

    @Column(name = "densidadpulpa_analisisley")
    private String densidadpulpaAnalisisLey;

    @Column(name = "caudal_analisisley")
    private String caudalAnalisisLey;

    @Column(name = "fecharegistro_analisisley")
    private LocalDateTime fecharegistroAnalisisLey;

    @Column(name = "estado_analisisley")
    private String estadoAnalisisLey;

    //Constructores
    public AnalisisLey() {
    }

    public AnalisisLey(Pedido pedidoAnalisisLey, Usuario encargadolaboratorioAnalisisLey, String pesoAnalisisLey,
                       String distribuciongranulometricaAnalisisLey, String humedadAnalisisLey,
                       String densidadpulpaAnalisisLey, String caudalAnalisisLey, LocalDateTime fecharegistroAnalisisLey,
                       String estadoAnalisisLey) {
        this.pedidoAnalisisLey = pedidoAnalisisLey;
        this.encargadolaboratorioAnalisisLey = encargadolaboratorioAnalisisLey;
        this.pesoAnalisisLey = pesoAnalisisLey;
        this.distribuciongranulometricaAnalisisLey = distribuciongranulometricaAnalisisLey;
        this.humedadAnalisisLey = humedadAnalisisLey;
        this.densidadpulpaAnalisisLey = densidadpulpaAnalisisLey;
        this.caudalAnalisisLey = caudalAnalisisLey;
        this.fecharegistroAnalisisLey = fecharegistroAnalisisLey;
        this.estadoAnalisisLey = estadoAnalisisLey;
    }
}
