package pe.upc.mckchain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FaseMineralRequest {

    //Atributos
    private String faseMineral;

    //Constructores
    public FaseMineralRequest() {
    }

    public FaseMineralRequest(String faseMineral) {
        this.faseMineral = faseMineral;
    }
}
