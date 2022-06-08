package pe.upc.mckchain.dto.request.usuario.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMineraInfoRequest {

    //Atributos
    private String telefonoUsuario;
    private String webUsuario;

    //Constructores
    public UpdateMineraInfoRequest() {
    }

    public UpdateMineraInfoRequest(String telefonoUsuario, String webUsuario) {
        this.telefonoUsuario = telefonoUsuario;
        this.webUsuario = webUsuario;
    }
}
