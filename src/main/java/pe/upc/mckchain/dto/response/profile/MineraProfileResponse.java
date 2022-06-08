package pe.upc.mckchain.dto.response.profile;

import lombok.Getter;
import lombok.Setter;
import pe.upc.mckchain.dto.response.general.ImagenResponse;

import java.util.UUID;

@Getter
@Setter
public class MineraProfileResponse {

    //Atributos
    private UUID idUsuario;
    private String rucUsuario;
    private String codigounicoMinera;
    private String ubicacionMinera;
    private String nombreUsuario;
    private String telefonoUsuario;
    private String emailUsuario;
    private String webUsuario;
    private ImagenResponse imagenUsuario;

    //Constructores
    public MineraProfileResponse() {
    }

    public MineraProfileResponse(UUID idUsuario, String rucUsuario, String codigounicoMinera, String ubicacionMinera,
                                 String nombreUsuario, String telefonoUsuario, String emailUsuario, String webUsuario,
                                 ImagenResponse imagenUsuario) {
        this.idUsuario = idUsuario;
        this.rucUsuario = rucUsuario;
        this.codigounicoMinera = codigounicoMinera;
        this.ubicacionMinera = ubicacionMinera;
        this.nombreUsuario = nombreUsuario;
        this.telefonoUsuario = telefonoUsuario;
        this.emailUsuario = emailUsuario;
        this.webUsuario = webUsuario;
        this.imagenUsuario = imagenUsuario;
    }
}
