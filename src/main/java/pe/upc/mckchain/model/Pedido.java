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
@Table(name = "pedido")
@Getter
@Setter
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_pedido")
    private UUID idPedido;

    @ManyToOne
    @JoinTable(name = "comprador_pedidos",
            joinColumns = @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "id_comprador", referencedColumnName = "id_usuario"))
    private Usuario compradorPedido;

    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "pedidoMineral")
    private Mineral mineralPedido;

    @Column(name = "estado_pedido")
    private String estadoPedido;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @OneToOne
    @JoinTable(name = "analisisley_pedido",
            joinColumns = @JoinColumn(name = "id_pedido", referencedColumnName = "id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "id_analisisley", referencedColumnName = "id_analisisley"))
    private AnalisisLey analisisleyPedido;

    //Constructores
    public Pedido() {
    }

    public Pedido(Usuario compradorPedido, String estadoPedido, LocalDateTime fechaPedido) {
        this.compradorPedido = compradorPedido;
        this.estadoPedido = estadoPedido;
        this.fechaPedido = fechaPedido;
    }
}
