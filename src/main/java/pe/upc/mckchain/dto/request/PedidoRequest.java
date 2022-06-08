package pe.upc.mckchain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoRequest {

    //Atributos
    private String mineral;

    //Constructores
    public PedidoRequest() {
    }

    public PedidoRequest(String mineral) {
        this.mineral = mineral;
    }
}
