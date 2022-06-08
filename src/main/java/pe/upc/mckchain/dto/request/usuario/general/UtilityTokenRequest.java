package pe.upc.mckchain.dto.request.usuario.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtilityTokenRequest {

    //Atributos
    private String utilityToken;

    //Constructores
    public UtilityTokenRequest() {
    }

    public UtilityTokenRequest(String utilityToken) {
        this.utilityToken = utilityToken;
    }
}
