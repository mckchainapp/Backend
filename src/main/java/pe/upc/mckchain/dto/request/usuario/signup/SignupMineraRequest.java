package pe.upc.mckchain.dto.request.usuario.signup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupMineraRequest {

    //Atributos
    private String utilitytokenUsuario;
    private String usernameUsuario;
    private String passwordUsuario;

    //Constructores
    public SignupMineraRequest() {
    }

    public SignupMineraRequest(String utilitytokenUsuario, String usernameUsuario, String passwordUsuario) {
        this.utilitytokenUsuario = utilitytokenUsuario;
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
    }
}
