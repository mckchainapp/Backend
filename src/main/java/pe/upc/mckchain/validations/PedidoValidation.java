package pe.upc.mckchain.validations;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PedidoValidation {

    //Atributos
    private UUID idComprador;
    private UUID idMineral;

    //Constructores
    public PedidoValidation() {
    }

    public PedidoValidation(UUID idComprador, UUID idMineral) {
        this.idComprador = idComprador;
        this.idMineral = idMineral;
    }
}
