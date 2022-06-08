package pe.upc.mckchain.dto.request.usuario.signup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupCompradorRequest {

    //Atributos
    private String nombreUsuario;
    private String apellidoUsuario;
    private String rucUsuario;
    private String emailUsuario;
    private String usernameUsuario;
    private String passwordUsuario;

    //Constructores
    public SignupCompradorRequest() {
    }

    public SignupCompradorRequest(String nombreUsuario, String apellidoUsuario, String rucUsuario, String emailUsuario,
                                  String usernameUsuario, String passwordUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.rucUsuario = rucUsuario;
        this.emailUsuario = emailUsuario;
        this.usernameUsuario = usernameUsuario;
        this.passwordUsuario = passwordUsuario;
    }
}
