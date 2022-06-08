package pe.upc.mckchain.dto.request.usuario.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCompradorInfoRequest {

    //Atributos
    private String nombreUsuario;
    private String apellidoUsuario;
    private String telefonoUsuario;
    private String webUsuario;

    //Constructores
    public UpdateCompradorInfoRequest() {
    }

    public UpdateCompradorInfoRequest(String nombreUsuario, String apellidoUsuario, String telefonoUsuario,
                                      String webUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.webUsuario = webUsuario;
    }
}
