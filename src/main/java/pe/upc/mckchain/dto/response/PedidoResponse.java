package pe.upc.mckchain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PedidoResponse {

    //Atributos
    private UUID idPedido;
    private String fechaPedido;
    private UUID idMineral;
    private String nombreMineral;
    private String faseMineral;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombreMinera;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rucMinera;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idComprador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombreComprador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idAnalisisLey;

    //Constructores
    public PedidoResponse() {
    }

    //--Perspectiva Comprador
    public PedidoResponse(UUID idPedido, String fechaPedido, UUID idMineral, String nombreMineral, String faseMineral,
                          String nombreMinera, String rucMinera) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.idMineral = idMineral;
        this.nombreMineral = nombreMineral;
        this.faseMineral = faseMineral;
        this.nombreMinera = nombreMinera;
        this.rucMinera = rucMinera;
    }

    //--Perspectiva Comprador: Pedido Culminado
    public PedidoResponse(UUID idPedido, String fechaPedido, UUID idMineral, String nombreMineral, String faseMineral,
                          String nombreMinera, String rucMinera, UUID idAnalisisLey) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.idMineral = idMineral;
        this.nombreMineral = nombreMineral;
        this.faseMineral = faseMineral;
        this.nombreMinera = nombreMinera;
        this.rucMinera = rucMinera;
        this.idAnalisisLey = idAnalisisLey;
    }

    //--Perspectiva Encargado Laboratorio
    public PedidoResponse(UUID idPedido, String fechaPedido, UUID idMineral, String nombreMineral, String faseMineral,
                          UUID idComprador, String nombreComprador) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.idMineral = idMineral;
        this.nombreMineral = nombreMineral;
        this.faseMineral = faseMineral;
        this.idComprador = idComprador;
        this.nombreComprador = nombreComprador;
    }
}
