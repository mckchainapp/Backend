package pe.upc.mckchain.dto.request.usuario.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailRequest {

    //Atributos
    private String emailUsuario;

    //Constructores
    public EmailRequest() {
    }

    public EmailRequest(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
