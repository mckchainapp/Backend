package pe.upc.mckchain.dto.request.usuario.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUsuarioInfoRequest {

    //Atributos
    private String nombreUsuario;
    private String apellidoUsuario;
    private String telefonoUsuario;

    //Constructores
    public UpdateUsuarioInfoRequest() {
    }

    public UpdateUsuarioInfoRequest(String nombreUsuario, String apellidoUsuario, String telefonoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.telefonoUsuario = telefonoUsuario;
    }
}
