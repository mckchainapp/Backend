package pe.upc.mckchain.dto.response.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    //Atributos
    private String message;

    //Constructores
    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }
}
