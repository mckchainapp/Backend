package pe.upc.mckchain.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MineralRequest {

    //Atributos
    private String zonaexploracionMineral;
    private String fechaextraccionMineral;
    private String nombreMineral;
    private String tipomuestraMineral;

    //Constructores
    public MineralRequest() {
    }

    public MineralRequest(String zonaexploracionMineral, String fechaextraccionMineral, String nombreMineral,
                          String tipomuestraMineral) {
        this.zonaexploracionMineral = zonaexploracionMineral;
        this.fechaextraccionMineral = fechaextraccionMineral;
        this.nombreMineral = nombreMineral;
        this.tipomuestraMineral = tipomuestraMineral;
    }
}
