package pe.upc.mckchain.validations;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MineralValidation {

    //Atributos
    private UUID idZonaExploracion;
    private String nombreMineral;

    //Constructores
    public MineralValidation() {
    }

    public MineralValidation(UUID idZonaExploracion, String nombreMineral) {
        this.idZonaExploracion = idZonaExploracion;
        this.nombreMineral = nombreMineral;
    }
}
