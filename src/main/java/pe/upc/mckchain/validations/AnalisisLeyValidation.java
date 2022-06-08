package pe.upc.mckchain.validations;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnalisisLeyValidation {

    //Atributos
    private UUID idEncargadoLaboratorio;
    private UUID idPedido;

    //Constructores
    public AnalisisLeyValidation() {
    }

    public AnalisisLeyValidation(UUID idEncargadoLaboratorio, UUID idPedido) {
        this.idEncargadoLaboratorio = idEncargadoLaboratorio;
        this.idPedido = idPedido;
    }
}
