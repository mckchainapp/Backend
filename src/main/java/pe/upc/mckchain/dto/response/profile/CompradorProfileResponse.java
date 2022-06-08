package pe.upc.mckchain.dto.response.profile;

import lombok.Getter;
import lombok.Setter;
import pe.upc.mckchain.dto.response.general.ImagenResponse;

import java.util.UUID;

@Getter
@Setter
public class CompradorProfileResponse {

    //Atributos
    private UUID idUsuario;
    private String rucUsuario;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String telefonoUsuario;
    private String emailUsuario;
    private String webUsuario;
    private ImagenResponse imagenUsuario;

    //Constructores
    public CompradorProfileResponse() {
    }

    public CompradorProfileResponse(UUID idUsuario, String rucUsuario, String nombreUsuario, String apellidoUsuario,
                                    String telefonoUsuario, String emailUsuario, String webUsuario,
                                    ImagenResponse imagenUsuario) {
        this.idUsuario = idUsuario;
        this.rucUsuario = rucUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.emailUsuario = emailUsuario;
        this.webUsuario = webUsuario;
        this.imagenUsuario = imagenUsuario;
    }
}
