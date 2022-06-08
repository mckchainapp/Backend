package pe.upc.mckchain.validations;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ZonaExploracionValidation {

    //Atributos
    private UUID idMinera;
    private String nombreZonaExploracion;

    //Constructores
    public ZonaExploracionValidation() {
    }

    public ZonaExploracionValidation(UUID idMinera, String nombreZonaExploracion) {
        this.idMinera = idMinera;
        this.nombreZonaExploracion = nombreZonaExploracion;
    }
}
