package pe.upc.mckchain.dto.response.profile;

import lombok.Getter;
import lombok.Setter;
import pe.upc.mckchain.dto.response.general.ImagenResponse;

import java.util.UUID;

@Getter
@Setter
public class UsuarioProfileResponse {

    //Atributos
    private UUID idUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String telefonoUsuario;
    private String emailUsuario;
    private ImagenResponse imagenUsuario;
    private String razonsocialMinera;
    private ImagenResponse logoMinera;

    //Constructores
    public UsuarioProfileResponse() {
    }

    public UsuarioProfileResponse(UUID idUsuario, String nombreUsuario, String apellidoUsuario, String telefonoUsuario,
                                  String emailUsuario, ImagenResponse imagenUsuario, String razonsocialMinera,
                                  ImagenResponse logoMinera) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.emailUsuario = emailUsuario;
        this.imagenUsuario = imagenUsuario;
        this.razonsocialMinera = razonsocialMinera;
        this.logoMinera = logoMinera;
    }
}
