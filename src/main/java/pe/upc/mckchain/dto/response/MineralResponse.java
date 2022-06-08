package pe.upc.mckchain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MineralResponse {

    //Atributos
    private UUID idMineral;
    private String nombreMineral;
    private String tipomuestraMineral;
    private LocalDate fechaextraccionMineral;
    private String faseMineral;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idzonaexploracionMineral;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombrezonaexploracionMineral;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID idMinera;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombreMinera;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rucMinera;

    //Constructores
    public MineralResponse() {
    }

    //--Perspectiva de Minera
    public MineralResponse(UUID idMineral, String nombreMineral, String tipomuestraMineral,
                           LocalDate fechaextraccionMineral, String faseMineral, UUID idzonaexploracionMineral,
                           String nombrezonaexploracionMineral) {
        this.idMineral = idMineral;
        this.nombreMineral = nombreMineral;
        this.tipomuestraMineral = tipomuestraMineral;
        this.fechaextraccionMineral = fechaextraccionMineral;
        this.faseMineral = faseMineral;
        this.idzonaexploracionMineral = idzonaexploracionMineral;
        this.nombrezonaexploracionMineral = nombrezonaexploracionMineral;
    }

    //--Perspectiva de Comprador
    public MineralResponse(UUID idMineral, String nombreMineral, String tipomuestraMineral,
                           LocalDate fechaextraccionMineral, String faseMineral, UUID idMinera,
                           String nombreMinera, String rucMinera) {
        this.idMineral = idMineral;
        this.nombreMineral = nombreMineral;
        this.tipomuestraMineral = tipomuestraMineral;
        this.fechaextraccionMineral = fechaextraccionMineral;
        this.faseMineral = faseMineral;
        this.idMinera = idMinera;
        this.nombreMinera = nombreMinera;
        this.rucMinera = rucMinera;
    }
}
