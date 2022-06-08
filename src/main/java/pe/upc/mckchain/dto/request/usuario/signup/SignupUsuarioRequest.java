package pe.upc.mckchain.dto.request.usuario.signup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupUsuarioRequest {

    //Atributos
    private String utilitytokenUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String usernameUsuario;
    private String passwordUsuario;

    //Constructores
    public SignupUsuarioRequest() {
    }

    public SignupUsuarioRequest(String utilitytokenUsuario, String nombreUsuario, String apellidoUsuario,
                                String usernameUsuario, String passwordUsuario) {
        this.utilitytokenUsuario = utilitytokenUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
    }
}
