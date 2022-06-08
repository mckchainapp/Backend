package pe.upc.mckchain.dto.request.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {

    //Atributos
    private String usernameUsuario;
    private String passwordUsuario;

    //Constructores
    public SigninRequest() {
    }

    public SigninRequest(String usernameUsuario, String passwordUsuario) {
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
    }
}
