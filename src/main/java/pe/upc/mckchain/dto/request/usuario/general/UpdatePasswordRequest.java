package pe.upc.mckchain.dto.request.usuario.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePasswordRequest {

    //Atributos
    private String utilitytokenUsuario;
    private String passwordUsuario;

    //Constructores
    public UpdatePasswordRequest() {
    }

    public UpdatePasswordRequest(String utilitytokenUsuario, String passwordUsuario) {
        this.utilitytokenUsuario = utilitytokenUsuario;
        this.passwordUsuario = passwordUsuario;
    }
}
